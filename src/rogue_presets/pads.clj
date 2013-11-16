(ns rogue-presets.pads
  (:require [rogue-presets.utils :refer :all]))

(defpreset pad-arctic "Arctic Pad"
  defaults
  (osc1 :type fm1 :level 1)
  (osc2 :type saw :level 1 :coarse 12 :out_mod 2 :level_a 1) ; RM modulated
  (filter1 :type lp_24db :freq 220 :q 0.3 :level 1)
  (env1 :attack 0.3 :sustain 0.6 :release 1.2 :curve 0.7)
  (env2 :attack 1 :sustain 1 :release 1.22 :curve 0.7)
  (lfo1 :type lfo_tri :freq 2)
  (reverb-fx) ; TODO tune
  (modulations [mod_lfo1 mod_osc1_pitch 0.2]
               [mod_lfo1 mod_flt1_freq 0.2]
               [mod_lfo1 mod_osc2_amp 0.2]
               [mod_env2 mod_flt1_freq 0.8]))