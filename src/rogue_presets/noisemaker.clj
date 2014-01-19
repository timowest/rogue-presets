(ns rogue-presets.noisemaker
  (:require [rogue-presets.utils :refer :all]
            [rogue-presets.ext.noisemaker :as nm]
            [clojure.xml :as xml]))

(defn parse-nm 
  [file]
  (let [data (xml/parse (java.io.File. file))
        attrs (-> data :content first :content first :attrs)]
    (->> (for [[k v] attrs]
               [k (if (= k :programname) v (Double/parseDouble v))])
         (into {})
         nm/transform)))

(defpreset "KB Big Synth FN"
  defaults
  (parse-nm "dev-resources/KB Big Synth FN.nm"))

(defpreset "KB Chimp Organ TUC"
  defaults
  (parse-nm "dev-resources/KB Chimp Organ TUC.nm"))

(defpreset "KB Drops TAL"
  defaults
  (parse-nm "dev-resources/KB Drops TAL.nm"))

(defpreset "KB E-Piano TAL"
  defaults
  (parse-nm "dev-resources/KB E-Piano TAL.nm"))

(defpreset "KB Ghostly Glomp AS"
  defaults
  (parse-nm "dev-resources/KB Ghostly Glomp AS.nm"))

(defpreset "KB Glass Star TUC"
  defaults
  (parse-nm "dev-resources/KB Glass Star TUC.nm"))

(defpreset "KB Glockenschlag FN"
  defaults
  (parse-nm "dev-resources/KB Glockenschlag FN.nm"))

(defpreset "KB Piano House TAL"
  defaults
  (parse-nm "dev-resources/KB Piano House TAL.nm"))

(defpreset "KB Pop Pluck TUC"
  defaults
  (parse-nm "dev-resources/KB Pop Pluck TUC.nm"))

(defpreset "KB Sawberry Milkshake TU"
  defaults
  (parse-nm "dev-resources/KB Sawberry Milkshake TU.nm"))

(defpreset "KB Screetcher TUC"
  defaults
  (parse-nm "dev-resources/KB Screetcher TUC.nm"))

(defpreset "KB Screetcher TUC"
  defaults
  (parse-nm "dev-resources/KB Screetcher TUC.nm"))

(defpreset "KB Smooth Sine TAL"
  defaults
  (parse-nm "dev-resources/KB Smooth Sine TAL.nm"))

(defpreset "KB White Noise FN"
  defaults
  (parse-nm "dev-resources/KB White Noise FN.nm"))


(comment
  (doseq [[k v] big-synth]
     (when (nil? v)
       (println k))))
  