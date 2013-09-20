(ns rogue-presets.leads
  (:require [rogue-presets.utils :refer :all]))

(defpreset lead-pulse1 "Pulse Lead 1"
  two-oscs
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