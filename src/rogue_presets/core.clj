(ns rogue-presets.core
  (:require [rogue-presets.utils :refer :all]
            [rogue-presets.serializers :refer :all]))

; basic leads pads basses fm reeds brasses strings effects 

(defmacro defpreset
  [name & contents]
  (list 'def name (cons 'merge contents)))

(defpreset basic-saw
  (bus_a :level 1)
  (osc1 :type saw :level 1 :level_a 1))

(comment
  (serialize "basic-saw" "Basic Saw" basic-saw))
         
  