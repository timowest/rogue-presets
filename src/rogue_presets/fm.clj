(ns rogue-presets.fm
  (:require [rogue-presets.utils :refer :all]))

(def fm-base
  (merge defaults
         (osc1 :type fm1 :level 1)
         (osc1 :type fm2 :level 1)
         (osc1 :type fm3 :level 1)
         (osc1 :type fm4 :level 1)
         (bus_a :level 0.5)))

(defpreset fm-bass "Bass FM"
  defaults
  (osc1 :type fm1 :level 1 :fine 0.01 :ratio 0.5)
  (osc2 :type fm1 :level 1 :fine -0.01 :level_a 1 :pm 1)
  (bus_a :level 0.5)
  (env1 :attack 0.02 :sustain 0.2 :curve 0.6)
  (env2 :attack 0.02 :decay 0.2 :sustain 0.1 :curve 0.7)
  (modulations [mod_env2 mod_osc1_amp 1]))