(ns rogue-presets.leads
  (:require [rogue-presets.utils :refer :all]))

(defpreset "Pulse Lead 1"
  two-oscs
  (osc1 :type pulse :width 0.25 :fine -0.05)
  (osc2 :type pulse :width 0.75 :fine 0.05)
  (filter1 :freq 440 :type lp_12db :q 0.2 :level 1)
  (env1 :attack 0.01 :curve 0.7)
  (env2 :attack 0.1 :sustain 0.25 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 0.6])
  (reverb-fx))

(defpreset "Pulse Lead 2"
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

; acoustic (TODO move to own ns)

(defpreset "Brass"
  defaults
  (osc1 :type saw :level 1 :level_a 0.5)
  (osc2 :type saw :fine 0.01 :level 1 :level_a 1)
  (filter1 :type lp_24db :freq 880 :key_to_f 1 :level 1)
  (env1 :attack 0.01 :decay 1 :sustain 0.5 :release 0.5 :curve 0.7)
  (env2 :attack 0.1  :release 0.5 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1]                 
               [mod_env2 mod_flt1_freq 1]))

(defpreset "Brass 2"
  defaults
  (osc1 :type saw :level 1 :level_a 0.5)
  (osc2 :type saw :level 1 :level_a 0.5 :fine 0.1)
  (osc3 :type saw :level 1 :level_b 0.5)
  (osc4 :type saw :level 1 :level_b 0.5 :fine -0.1)
  (filter1 :type lp_24db :level 1 :pan 0)
  (filter2 :type lp_24db :level 1 :pan 1 :source 1) ; bus b input
  (env1 :attack 0.03 :curve 0.7)
  (env2 :attack 0.1 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1]
               [mod_env2 mod_flt2_freq 1])
  (chorus-fx)
  (reverb-fx))

(defpreset "Harpsichord"
  defaults
  (osc1 :type pulse :width 0.4 :level 1 :level_a 0.6)
  (osc2 :type saw :coarse 12 :level 1 :level_a 0.4)
  (filter1 :type lp_24db :freq 2400 :key_to_f 0.5 :level 1)
  (env1 :attack 0.01 :decay 1 :sustain 0 :release 1 :curve 0.7)
  (env2 :attack 0 :decay 1 :sustain 0 :release 1 :curve 0.7)
  (modulations [mod_env2 mod_flt1_freq 1])
  (chorus-fx)
  (reverb-fx))


  