(ns rogue-presets.ext.noisemaker
  (:require [rogue-presets.utils :refer :all]))

(def SR 44100.0)

(defn calc-combobox
  [items v]
  ; (int)floorf(value * (numItems - 1.0f) + 1.0f + 0.5f)
  (long (Math/floor (+ (* v (- items 1.0)) 1.5))))

(defn attack
  [v] ; =MIN((2.1*A1)^6.5, 1.928)  
  (Math/max 
    (Math/min (Math/pow (* 2.1 v) 6.5) 1.928)
    0.01))

(defn decay
  [v] 
  (Math/max v 0.01)) ; TODO

(defn release
  [v] ; =MIN((2*A1)^6.5), 9.613)
  (Math/max  
    (Math/min (Math/pow (* 2 v) 6.5) 7.613)
    0.01))
  
(def filter-type
  (comp {1 lp_24db 2 lp_18db 3 lp_12db 4 lp_6db 5 hp_12db
         6 bp_12db 7 n_24db  
         8 svf_lp 9 svf_hp 10 svf_bp}
        (partial calc-combobox 12)))  

(def osc1-waveform
  (comp {1 va_saw 2 va_pulse 3 noise}
        (partial calc-combobox 3)))

(def osc2-waveform
  (comp {1 va_saw 2 va_pulse 3 va_tri 4 sin 5 noise}
        (partial calc-combobox 5)))

(def lfo-waveform
  (comp {1 lfo_sin 2 lfo_tri 3 lfo_saw 4 lfo_pulse 5 lfo_sh 6 lfo_noise}
        (partial calc-combobox 5))) 

(def mod_osc12_pitch mod_osc1_pitch) ; TODO

(def lfo1-destination
  (comp {1 mod_no_target 2 mod_flt1_freq 3 mod_osc1_pitch 4 mod_osc2_pitch 
         5 mod_osc12_pitch 6 mod_osc1_pwm 7 mod_osc1_mod 8 mod_lfo2_sp}
        (partial calc-combobox 8)))

(def lfo2-destination
  (comp {1 mod_no_target 2 mod_flt1_freq 3 mod_osc1_pitch 4 mod_osc2_pitch 
         5 mod_osc12_pitch 6 mod_bus_a_pan 7 mod_volume 8 mod_lfo1_sp}
        (partial calc-combobox 8)))

(def freead-destination
  (comp {1 mod_no_target 2 mod_flt1_freq 3 mod_osc1_pitch 
         4 mod_osc2_pitch 5 mod_osc1_pwm 6 mod_osc1_mod}
        (partial calc-combobox 6)))

(def transpose
  (comp {0 -12.0 1 0.0 2 12.0 3 24.0}
        long 
        (partial * 3.00001)))

(defn log-scaled-value 
  [v]
  (/ (- (Math/exp (* v (Math/log 20.0))) 1.0) 19.0))

(defn log-scaled-value-filter
  [v]
  (/ (- (Math/exp (* v (Math/log 8.0))) 1.0) 7.0))

(defn log-scaled-value-centered
  [v]
  (let [v (* (- v 0.5) 2.0)]
    (* v v v)))

(defn filter-cutoff
  [v]
  (* (log-scaled-value-filter v) SR 0.5))

(defn log-scaled-volume
  [v max]
  (/ (- (Math/exp (* v max (Math/log 20.0))) 1.0) 19.0))

(defn log-scaled-rate
  [v]
  (/ (- (Math/exp (+ 0.2 (* v 3.0 (Math/log 20.0)))) 1.0) 19.0))

(defn volume
  [v]
  (log-scaled-volume v 2.0))

(defn osc-volume
  [v]
  (log-scaled-volume v 1.0))
  
(defn osc-tune
  [v]
  (double (long (- (* v 48.0) 24.0))))

(defn osc-finetune
  [v]
  (* (- v 0.5) 2.0))

(defn sustain
  [v]
  (log-scaled-volume v 1.0))

(def mod-amount log-scaled-value-centered)

(defn delaytime-to-bpm
  [v] 
  (let [scaled (+ 0.01 (* 0.99 (log-scaled-value v)))]
    (/ 60.0 (* 4.0 scaled))))
  
(comment 
  ; modulations
  mod1   velocity
  mod2   env2 -> filter1
  mod3   env3
  mod4   lfo1
  mod5   lfo2)

; TODO check modulations

(defn transform
  [m] ; map of keyword to value
  (into {} (map (fn [[k v]] [(name k) v])
  {; programname
   :volume       (volume (:volume m))
   
   ; filter
   :filter1_on   true
   :filter1_level 1.0
   :filter1_type (filter-type (:filtertype m))
   :filter1_freq (filter-cutoff (:cutoff m)) 
   :filter1_q    (:resonance m)
   :filter1_key_to_f (:keyfollow m)
   
   ; osc
   ; oscmastertune
   ; oscsync
   
   ; osc1
   :osc1_on      (> (:osc1volume m) 0.0)
   :osc1_level_a (> (:osc1volume m) 0.0)
   :osc1_level   (osc-volume (:osc1volume m)) 
   :osc1_type    (osc1-waveform (:osc1waveform m))
   :osc1_coarse  (+ (osc-tune (:osc1tune m)) 
                    (transpose (:transpose m)))
   :osc1_fine    (osc-finetune (:osc1finetune m))
   :osc1_width   (:osc1pw m)
   :osc1_start   (:osc1phase m)

   ; osc2
   :osc2_on      (> (:osc2volume m) 0.0) 
   :osc2_level_a (> (:osc2volume m) 0.0) 
   :osc2_level   (osc-volume (:osc2volume m)) 
   :osc2_type    (osc2-waveform (:osc2waveform m))
   :osc2_coarse  (+ (osc-tune (:osc2tune m))
                    (transpose (:transpose m)))
   :osc2_fine    (osc-finetune (:osc2finetune m))
   ; osc2fm TODO
   :osc2_start   (:osc2phase m)
   
   ; osc3
   :osc3_on      (> (:osc3volume m) 0.0)
   :osc3_level_a (> (:osc3volume m) 0.0)
   :osc3_level   (osc-volume (:osc3volume m)) 
   :osc3_type    va_pulse
   :osc3_coarse  (- (transpose (:transpose m)) 24.0)
   
   ; envs
   
   ; env1
   :env1_attack  (attack (:ampattack m))
   :env1_decay   (decay (:ampdecay m))
   :env1_sustain (:ampsustain m)
   :env1_release (release (:amprelease m))
   
   ; env2
   :env2_on      (not= (:filtercontour m) 0.5)
   :env2_attack  (attack (:filterattack m)) 
   :env2_decay   (decay (:filterdecay m))
   :env2_sustain (sustain (:filtersustain m)) 
   :env2_release (release (:filterrelease m))
   
   :mod2_src     mod_env2
   :mod2_target  mod_flt1_freq
   :mod2_amount  (mod-amount (:filtercontour m))
   
   ; env3
   :env3_on      (> (:freeaddestination m) 0)
   :env3_attack  (attack (:freeadattack m))
   :env3_decay   (decay (:freeaddecay m))
   :env3_sustain 0.0 ; TODO
   :env3_release 1.0 ; TODO
   
   :mod3_src     (if (> (:freeaddestination m) 0) mod_env3 0.0)
   :mod3_target  (freead-destination (:freeaddestination m)) 
   :mod3_amount  (if (> (:freeaddestination m) 0)
                   (mod-amount (:freeadamount m))
                   0.0)
   
   ; lfos
   
   ; lfo1
   :lfo1_on      (> (:lfo1destination m) 0)
   :lfo1_type    (lfo-waveform (:lfo1waveform m))
   :lfo1_freq    (log-scaled-rate (:lfo1rate m))
   :lfo1_start   (:lfo1phase m)
   ; lfo1sync
   ; lfo1keytrigger
   
   :mod4_src     (if (> (:lfo1destination m) 0) mod_lfo1 0.0)
   :mod4_target  (lfo1-destination (:lfo1destination m))
   :mod4_amount  (if (> (:lfo1destination m) 0)   
                   (mod-amount (:lfo1amount m))
                   0.0)
   
   ; lfo2
   :lfo2_on      (> (:lfo2destination m) 0)
   :lfo2_type    (lfo-waveform (:lfo2waveform m))
   :lfo2_freq    (log-scaled-rate (:lfo2rate m))
   :lfo2_start   (:lfo2phase m)
   ; lfo2sync
   ; lfo2keytrigger
   
   :mod5_src     (if (> (:lfo2destination m) 0) mod_lfo2 0.0)
   :mod5_target  (lfo2-destination (:lfo2destination m))
   :mod5_amount  (if (> (:lfo2destination m) 0)
                   (mod-amount (:lfo2amount m))
                   0.0)
         
   ; velocity
  
   ; velocityvolume  
   ; velocitycontour 
   ; velocitycutoff 
  
   ; various
  
   ; portamento 
   ; keyfollow 
   ; voices 
   ; portamentomode 
   ; pitchwheelcutoff 
   ; pitchwheelpitch 
  
   ; highpass
   ; detune
   ; vintagenoise 
   ; ringmodulation 

   ; fx
  
   :chorus_on    (> (+ (:chorus1enable m) 
                       (:chorus2enable m)) 0.0)
   ; chorus1enable
   ; chorus2enable 

   :reverb_on    (> (:reverbwet m) 0.0)
   ; reverbwet   TODO
   ; reverbdecay
   ; reverbpredelay TODO
   ; reverbhighcut 
   ; reverblowcut 

   ; oscbitcrusher 
  
   ; filterdrive 
    
   :delay_on     (> (:delaywet m) 0.0)
   :delay_depth  (:delaywet m)
   ;:delay_bpm    (delaytime-to-bpm (:delaytime m)) FIXME
   ; delaysync
   :delay_divider_l (if (> (:delayfactorl m) 0) 0.5 1.0) 
   :delay_divider_r (if (> (:delayfactorr m) 0) 0.5 1.0) 
   ; delayhighshelf TODO
   ; delaylowshelf TODO
   :delay_feedback (Math/min (:delayfeedback m) 0.99)
  
   ; envelopeeditordest1  
   ; envelopeeditorspeed 
   ; envelopeeditoramount 
   ; envelopeoneshot 
   ; envelopefixtempo  

   ; tab1open 
   ; tab2open 
   ; tab3open 
   ; tab4open
  })))

 