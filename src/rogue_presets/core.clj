(ns rogue-presets.core
  (:require [rogue-presets.serializers :refer :all]
            [rogue-presets basic fm leads noisemaker pads]))

(defn ns-contents
  [ns]
  (filter (comp :name meta)
          (map deref (vals (ns-publics ns)))))

(defn -main
  []
  (dump "../rogue" 
        (mapcat ns-contents '[rogue-presets.basic
                              rogue-presets.fm
                              rogue-presets.leads
                              rogue-presets.noisemaker
                              rogue-presets.pads])))

