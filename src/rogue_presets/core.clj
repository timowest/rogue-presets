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
         (osc1 :level_a 0.5)
         (osc2 :level_a 0.5)))

(def four-oscs
  (merge defaults
         (osc1 :level_a 0.25)
         (osc2 :level_a 0.25)
         (osc3 :level_a 0.25)
         (osc4 :level_a 0.25)))

(defpreset pulse-lead1 "Pulse Lead 1"
  two_oscs
  (osc1 :type pulse :width 0.25 :fine -0.05)
  (osc2 :type pulse :width 0.75 :fine 0.05)
  (filter1 :freq 220 :type lp_12db :q 0.2)
  (env1 :attack 0.01 :curve 0.7)
  (env2 :attack 0.1 :sustain 0.25 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 0.6])
  (reverb-fx))

(defpreset pulse-lead2 "Pulse Lead 2"
  (osc1 :type pulse :width 0.4 :fine -0.2)
  (osc2 :type pulse :width 0.5 :fine -0.1)
  (osc3 :type pulse :width 0.6 :fine 0.1)
  (osc4 :type pulse :width 0.7 :fine 0.2)
  (bus_a :level 0.5)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7))

(defpreset saw-lead1 "Saw Lead 1"
  (osc1 :type tri :width 0.95 :fine -0.2)
  (osc2 :type tri :width 0.96 :fine -0.1)
  (osc3 :type tri :width 0.97 :fine 0.1)
  (osc4 :type tri :width 0.98 :fine 0.2)
  (bus_a :level 0.5)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7))
    
; pads basses fm percussion effects 

(defn dump-resets
  []
  (dump "../rogue" 
        basic-saw basic-pulse basic-tri pd-saw pd-square pd-pulse
        pulse-lead1 pulse-lead2 saw-lead1))
