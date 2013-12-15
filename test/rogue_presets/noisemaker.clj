(ns rogue-presets.noisemaker
  (:require [rogue-presets.utils :refer :all]))

(def filter-type
  {1 lp_24db 2 lp_18db 3 lp_12db 4 lp_6db
   5 hp_24db 6 bp_24db 7 n_24db})   

(defn osc1-waveform
  [v]
  (cond (< v 0.5) va_saw
        (< v 1.0) va_pulse
        :else     noise))

(defn osc2-waveform
  [v]
  (cond (< v 0.333) va_saw
        (< v 0.666) va_pulse
        (< v 1.0)   va_tri
        :else       sin))

(defn lfo-waveform
  [v]
  ({0 lfo_sin 1 lfo_tri 2 lfo_saw 3 lfo_pulse 4 lfo_sh 5 lfo_noise}
   (long (* v  5.000001)))) 

(def lfo1-destination
  {1 mod_no_target 2 mod_flt1_freq 3 mod_osc1_pitch 4 mod_osc2_pitch 
   5 mod_osc1_pwm 6 mod_osc1_mod 7 mod_lfo2_sp})

(def lfo2-destination
  {1 mod_no_target 2 mod_flt1_freq 3 mod_osc1_pitch 4 mod_osc2_pitch 
   5 mod_bus_a_pan 6 mod_volume 7 mod_lfo1_sp})

(defn transpose
  [v]
  ({0 -12.0 1 0.0 2 12.0 3 24.0}
   (long (* v 3.0))))

(defn log-scaled-value 
  [v]
  (/ (- (Math/exp (- v (Math/log 20.0))) 1.0) 19.0))

(defn log-scaled-value-filter
  [v]
  (/ (- (Math/exp (- v (Math/log 8.0))) 1.0) 7.0))

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
  
(defn transform
  [m] ; map of keyword to value
  {; programname
   :volume       (volume (:volume m))
   
   ; filter
   :filter1_type (filter-type (:filtertype m))
   :filter1_freq (log-scaled-value-filter (:cutoff m))
   :filter1_q    (:resonance m)
   
   ; osc
   ; oscmastertune
   ; oscsync
   
   ; osc1
   :osc1_on      (> (:osc1volume m) 0.0)
   :osc1_level   (osc-volume (:osc1volume m))
   :osc1_type    (osc1-waveform (:osc1waveform m))
   :osc1_coarse  (+ (osc-tune (:osc1tune m)) 
                    (transpose (:transpose m)))
   :osc1_fine    (osc-finetune (:osc1finetune m))
   :osc1_width   (:osc1pw m)
   :osc1_start   (:osc1phase m)

   ; osc2
   :osc2_on      (> (:osc2volume m) 0.0) 
   :osc2_level   (osc-volume (:osc2volume m))
   :osc2_type    (osc2-waveform (:osc2waveform m))
   :osc2_coarse  (+ (osc-tune (:osc2tune m))
                    (transpose (:transpose m)))
   :osc2_fine    (osc-finetune (:osc2finetune m))
   ; osc2fm TODO
   :osc2_start   (:osc2phase m)
   
   ; osc3
   :osc3_on      (> (:osc3volume m) 0.0)
   :osc3_level   (osc-volume (:osc3volume m))
   
   ; envs
   
   ; env1
   :env1_attack  (:ampattack m)
   :env1_decay   (:ampdecay m)
   :env1_sustain (:ampsustain m)
   :env1_release (:amprelease m)
   
   ; env2
   ; filtercontour TODO
   :env2_attack  (:filterattack m)
   :env2_decay   (:filterdecay m)
   :env2_sustain (:filtersustain m)
   :env2_release (:filterrelease m)
   
   ; env3
   :env3_on      (> (:freeadamount m) 0.0)
   :env3_attack  (:freeadattack m)
   :env3_decay   (:freeaddecay m)
   ; freeadamount TODO
   ; freeaddestination TODO
   
   ; lfos
   
   ; lfo1
   :lfo1_on      (> (:lfo1amount m) 0.0)
   :lfo1_type    (lfo-waveform (:lfo1waveform m))
   :lfo1_freq    (log-scaled-value (:lfo1rate m))
   ; lfo1amount  
   ; lfo1destination
   :lfo1_start   (:lfo1phase m)
   ; lfo1sync
   ; lfo1keytrigger
   
   ; lfo2
   :lfo2_on       (> (:lfo2amount m) 0.0)
   :lfo2_type    (lfo-waveform (:lfo2waveform m))
   :lfo2_rate    (log-scaled-rate (:lfo2rate m))
   ; lfo2amount
   ; lfo2destination
   :lfo2_start   (:lfo2phase m)
   ; lfo2sync
   ; lfo2keytrigger
      
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
  
  ; highpass] 
  ; detune] 
  ; vintagenoise] 
  ; ringmodulation] 

  ; fx
  
  ; chorus1enable] 
  ; chorus2enable] 

  ; reverbwet
  ; reverbdecay
  ; reverbpredelay 
  ; reverbhighcut 
  ; reverblowcut 

  ; oscbitcrusher 
  
  ; filterdrive 
  
  ; delaywet 
  ; delaytime 
  ; delaysync
  ; delayfactorl  
  ; delayfactorr 
  ; delayhighshelf 
  ; delaylowshelf
  ; delayfeedback
  
  ; envelopeeditordest1  
  ; envelopeeditorspeed 
  ; envelopeeditoramount 
  ; envelopeoneshot 
  ; envelopefixtempo  

  ; tab1open 
  ; tab2open 
  ; tab3open 
  ; tab4open
  })