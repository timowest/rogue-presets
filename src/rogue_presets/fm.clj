(ns rogue-presets.fm
  (:require [rogue-presets.utils :refer :all]))

(def fm-base
  (merge defaults
         (osc1 :type fm1 :level 1)
         (osc1 :type fm2 :level 1)
         (osc1 :type fm3 :level 1)
         (osc1 :type fm4 :level 1)
         (bus_a :level 0.5)))