(ns rogue-presets.synth1-conv
  (:import java.io.File)
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]
            [rogue-presets.synth1 :refer :all]))

(defn load-preset
  [path]
  (let [lines (string/split-lines (slurp path))
        name (first lines)
        params (for [line lines :when (.contains line ",")]
                 (map #(Long/parseLong %) 
                      (string/split line #"\,")))]
    (reduce (fn [acc [k v]]
              (assoc-in acc (synth1-params k) v))
            {:name name}
            params)))

(defn pprint->string
  [preset]
  (with-out-str
    (binding [pprint/*print-right-margin* 150]
      (pprint/pprint preset))))

(def osc1type '{0 saw 1 pulse 2 tri 3 noise})

(def osc2type '{1 saw 2 pulse 3 tri 4 noise})

(def filter-type '{0 lp_12db 1 lp_24db 2 hp_24db 3 hp24_db})

(defn midi->num
  [v]
  (double (/ v 128)))

(defn convert-env
  [{:keys [attack decay sustain release]}]
  (list :attack attack
        :decay decay
        :sustain sustain
        :release release))

(defn convert-lfo
  [{:keys [switch wave speed]}]
  (list :on switch
        :type (osc2type wave)
        :freq speed))

(defn convert
  [preset]
  (let [{:keys [name osc1 osc2 filter menv env lfo1 lfo2]} preset]
    (list 'defpreset name
          (list 'osc1 :type (osc2type (:wave osc2))
                      :coarse (:pitch osc2)
                      :fine (:fine osc2)
                      :tracking (:track osc2)
                      :width (midi->num (:pw osc2)))
                      ; TODO mix sync ring transpose 
          (list 'osc2 :type (osc1type (:wave osc1)))
                      ; TODO mix 
          (list 'filter1 :type (filter-type (:type filter))
                         :freq (:freq filter)
                         :q (midi->num (:res filter)))
          (cons 'env1 (convert-env env))
          (cons 'env2 (convert-env menv))
          (cons 'env3 (convert-env filter))
          (cons 'lfo1 (convert-lfo lfo1))
          (cons 'lfo2 (convert-lfo lfo2)))))
          ; TODO modulations

(defn dump-folder
  [path]
  (doseq [file (file-seq (File. path))]
    (when (.endsWith (.getName file) ".sy1")
      (let [clj (File. (.getParentFile file) 
                       (str (.getName file) ".clj"))
            txt (try 
                  (pprint->string (load-preset file))
                  (catch Exception e ""))]
        (println (.getPath clj))
        (spit clj txt)))))
