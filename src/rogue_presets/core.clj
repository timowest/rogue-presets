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
  (basic-osc pd_saw))

(defpreset pd-square "PD Square"
  (basic-osc pd_square))

(defpreset pd-pulse "PD Pulse"
  (basic-osc pd_pulse))

; leads pads basses fm percussion reeds brasses strings effects

(comment
  (serialize basic-saw))         

(comment
  (dump "../rogue" basic-saw basic-pulse basic-tri pd-saw pd-square pd-pulse))

  