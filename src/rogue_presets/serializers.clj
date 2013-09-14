(ns rogue-presets.serializers
  (:require [clojure.string :as string]))

(defn- header
  [lname label]
  (str "@prefix atom: <http://lv2plug.in/ns/ext/atom#> .
@prefix lv2: <http://lv2plug.in/ns/lv2core#> .
@prefix pset: <http://lv2plug.in/ns/ext/presets#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix state: <http://lv2plug.in/ns/ext/state#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .

<http://www.github.com/timowest/rogue#" lname ">
  a pset:Preset ;
  lv2:appliesTo <http://www.github.com/timowest/rogue> ;
  rdfs:label \"" label "\" ;
  lv2:port\n  "))

(def presets-intro 
  "@prefix lv2: <http://lv2plug.in/ns/lv2core#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix pset: <http://lv2plug.in/ns/ext/presets#> .
@prefix base: <http://www.github.com/timowest/>.")

(defn preset
  [symbol]
  (str "<http://www.github.com/timowest/rogue#" symbol ">
  a pset:Preset ;
  lv2:appliesTo base:rogue ;
  rdfs:seeAlso  <presets/" symbol ".ttl> ."))

(defn- port
  [name value]
  (str "[ lv2:symbol \"" name "\" ; pset:value " value " ]"))

(defn serialize
  ([preset]
    (serialize (:name (meta preset))
               (:label (meta preset))
               preset))
  ([name label values]
    (str 
      (header name label)
      (string/join " ,\n  " (for [[k v] values]
                              (port k v)))
      ".")))
                 
  (defn dump
    [path & presets]
    (let [presets-txt (str presets-intro
                           "\n\n"
                           (string/join "\n\n"
                                        (map (comp preset :name meta) presets)))]
      (spit (str path "/presets.ttl") presets-txt)
      (doseq [preset presets]
        (spit (str path "/presets/" (:name (meta preset)) ".ttl") 
              (serialize preset)))))
          
    