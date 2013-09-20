(ns rogue-presets.basic
  (:require [rogue-presets.utils :refer :all]))

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

; double

(defpreset double-pulse "Double Pulse"
  two-oscs
  (osc1 :type pulse)
  (osc2 :type pulse)
  (filter1 :type svf_lp :freq 440 :q 0.7 :level 1)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7)
  (env2 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1]))

(defpreset double-saw "Double Saw"
  two-oscs
  (osc1 :type saw)
  (osc2 :type saw)
  (filter1 :type svf_lp :freq 440 :q 0.7 :level 1)
  (env1 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7)
  (env2 :attack 0.03 :sustain 0.5 :release 1 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1]))

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