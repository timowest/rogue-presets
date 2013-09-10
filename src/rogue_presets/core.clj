(ns rogue-presets.core
  (:require [rogue-presets.utils :refer :all]
            [rogue-presets.serializers :refer :all]))

 (defmacro defpreset
  [name label & contents]
  (list 'def name
        (list 'with-meta 
              (cons 'merge contents)
               {:name (str name) :label label})))

 ; basic leads pads basses fm reeds brasses strings effects
 
(defpreset basic-saw 
  "Basic Saw"
  (bus_a :level 1)
  (osc1 :type saw :level 1 :level_a 1))

(comment
  (serialize basic-saw))         
  