(ns rogue-presets.core
  (:require [rogue-presets.utils :refer :all]
            [rogue-presets.serializers :refer :all]))

(def defaults 
  (let [text (slurp "resources/rogue.ttl")
        keys (map second (re-seq #"lv2:symbol \"([\w_]+)\"" text))
        values (map second (re-seq #"lv2:default ([\d\.]+)" text))]
    (zipmap (drop 3 keys) values)))

(defmacro defpreset
  [name label & contents]
  (list 'def name
        (list 'with-meta 
              (cons 'merge contents)
               {:name (str name) :label label})))

(defn basic-osc
  [osc-type]
  (merge defaults 
         (bus_a :level 0.5)
         (osc1 :type osc-type :level 1 :level_a 1)))

; basic 

(defpreset basic-saw "Basic Saw"
  (basic-osc saw))

(defpreset basic-pulse "Basic Pulse"
  (basic-osc pulse))

(defpreset basic-tri "Basic Tri"
  (basic-osc tri))

(defpreset pd-saw "PD Saw"
  ; TODO higher width
  (basic-osc pd_saw))

(defpreset pd-square "PD Square"
  (basic-osc pd_square))

(defpreset pd-pulse "PD Pulse"
  (basic-osc pd_pulse))

; leads 

(def two_oscs
  (merge defaults
         (osc1 :level 1 :level_a 0.5)
         (osc2 :level 1 :level_a 0.5)))

(def four-oscs
  (merge defaults
         (osc1 :level 1 :level_a 0.25)
         (osc2 :level 1 :level_a 0.25)
         (osc3 :level 1 :level_a 0.25)
         (osc4 :level 1 :level_a 0.25)))

(def four-oscs-ab
  (merge defaults
         (osc1 :level 1 :level_a 0.25)
         (osc2 :level 1 :level_a 0.25)
         (osc3 :level 1 :level_b 0.25)
         (osc4 :level 1 :level_b 0.25)))

(defpreset lead-pulse1 "Pulse Lead 1"
  two_oscs
  (osc1 :type pulse :width 0.25 :fine -0.05)
  (osc2 :type pulse :width 0.75 :fine 0.05)
  (filter1 :freq 440 :type lp_12db :q 0.2 :level 1)
  (env1 :attack 0.01 :curve 0.7)
  (env2 :attack 0.1 :sustain 0.25 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 0.6])
  (reverb-fx))

(defpreset lead-pulse2 "Pulse Lead 2"
  four-oscs-ab
  (osc1 :type pulse :width 0.4 :fine -0.2)
  (osc2 :type pulse :width 0.5 :fine -0.1)
  (osc3 :type pulse :width 0.6 :fine 0.1)
  (osc4 :type pulse :width 0.7 :fine 0.2)
  (filter1 :type svf_lp :freq 440 :q 0.7 :level 0.5 :pan 0.4)
  (filter2 :type svf_lp :freq 880 :q 0.7 :level 0.5 :pan 0.6)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7)
  (env2 :attack 0 :sustain 0.3 :release 0.5 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1]
               [mod_env2 mod_flt2_freq 1]))

; stacked

(defpreset stacked-saws "Stacked Saws"
  four-oscs
  (osc1 :type tri :width 0.95 :fine -0.2)
  (osc2 :type tri :width 0.96 :fine -0.1)
  (osc3 :type tri :width 0.97 :fine 0.1)
  (osc4 :type tri :width 0.98 :fine 0.2)
  (bus_a :level 0.5)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7))

(defpreset stacked-pulses "Stacked Pulses"
  four-oscs
  (osc1 :type pulse :width 0.4 :fine -0.2)
  (osc2 :type pulse :width 0.5 :fine -0.1)
  (osc3 :type pulse :width 0.6 :fine 0.1)
  (osc4 :type pulse :width 0.7 :fine 0.2)
  (bus_a :level 0.5)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7))
  
; pads

(defpreset pad-arctic "Arctic Pad"
  defaults
  (osc1 :type fm1 :level 1)
  (osc2 :type saw :level 1 :coarse 12 :out_mod 2 :level_a 1) ; RM modulated
  (filter1 :type svf_lp :freq 220 :q 0.3 :level 1)
  (env1 :attack 0.3 :sustain 0.6 :release 1.2 :curve 0.7)
  (env2 :attack 1 :sustain 1 :release 1.22 :curve 0.7)
  (lfo1 :type lfo_tri :freq 2)
  (reverb-fx :bandwidth 0.75 :tail 1 :damping 0.25 :blend 0.75)
  (modulations [mod_lfo1 mod_osc1_pitch 0.2]
               [mod_lfo1 mod_flt1_freq 0.2]
               [mod_lfo1 mod_osc2_amp 0.2]
               [mod_env2 mod_flt1_freq 0.8]))

; basses fm percussion effects 

(defn -main
  []
  (dump "../rogue" 
        basic-saw basic-pulse basic-tri pd-saw pd-square pd-pulse
        lead-pulse1 lead-pulse2
        stacked-saws stacked-pulses
        pad-arctic))
