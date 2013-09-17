(ns rogue-presets.synth1
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

(def mappings
  { 0	[:osc1 :wave]   ; OSCILLATOR 1 WAVE
   45	[:osc1 :fm]     ; FM
   76	[:osc1 :detune] ; DETUNE
   
    1	[:osc2 :wave]   ; OSCILLATOR 2 WAVE
    2	[:osc2 :pitch]  ; PITCH
    3	[:osc2 :fine]   ; FINE
    4	[:osc2 :track]  ; TRACK
    5	[:osc2 :mix]    ; MIX
    6	[:osc2 :sync]   ; SYNC
    7	[:osc2 :ring]   ; RING
    8	[:osc2 :pw]     ; PW
    9	[:osc2 :transpose] ; TRANSPOSE
   
   10	[:menv :switch] ; M. ENV SWITCH
   11	[:menv :amount] ; AMOUNT
   12	[:menv :attack] ; ATTACK
   13	[:menv :decay]  ; DECAY
   71	[:menv :dest]   ; DEST
   72	[:menv :tune]   ; TUNE
   
   14	[:filter :type] ; FILTER TYPE
   15	[:filter :attack] ; ATTACK
   16	[:filter :decay] ; DECAY
   17	[:filter :sustain] ; SUSTAIN
   18	[:filter :release] ; RELEASE
   19	[:filter :freq] ; FREQUENCY
   20	[:filter :res]  ; RESONANCE
   21	[:filter :amount] ; AMOUNT
   22	[:filter :track] ; TRACK
   23	[:filter :saturation] ; SATURATION
   24	[:filter :vel-amount] ; VELOCITY AMOUNT
   
   25	[:env :attack]  ; ATTACK
   26	[:env :decay]   ; DECAY
   27	[:env :sustain] ; SUSTAIN
   28	[:env :release] ; RELEASE
   29	[:env :gain]    ; GAIN
   30	[:env :vel-amount] ; VELOCITY AMOUNT
   
   59	[:arp :switch]  ; ARP SWITCH
   31	[:arp :type]    ; TYPE
   32	[:arp :range]   ; RANGE
   33	[:arp :beat]    ; BEAT
   34	[:arp :gate]    ; GATE
   
   65	[:delay :switch] ; DELAY SWITCH
   82	[:delay :type]  ; TYPE
   35	[:delay :time]  ; TIME
   83	[:delay :spread] ; SPREAD
   36	[:delay :feedback] ; FEEDBACK
   37	[:delay :drywet] ; DRY/WET
   
   66	[:chorus :switch] ; CHORUS SWITCH
   64	[:chorus :type] ; TYPE
   52	[:chorus :time] ; TIME
   53	[:chorus :depth] ; DEPTH
   54	[:chorus :rate] ; RATE
   55	[:chorus :feedback] ; FEEDBACK
   56	[:chorus :level] ; LEVEL
   
   60	[:eq :tone]     ; EQ TONE
   61	[:eq :freq]     ; FREQUENCY
   62	[:eq :level]    ; LEVEL
   63	[:eq :q]        ; Q
   90	[:eq :lr]       ; L-R
   
   77	[:effect :switch] ; EFFECT SWITCH
   78	[:effect :type] ; TYPE
   79	[:effect :ctrl1] ; CTRL1
   80	[:effect :ctrl2] ; CTRL2
   81	[:effect :level] ; LEVEL
   
   38	[:play-mode]    ; PLAY MODE
   39	[:portamento]   ; PORTAMENTO
   74	[:auto]         ; AUTO
   
   40	[:pb-range]     ; PB RANGE
   
   73	[:unison]       ; UNISON
   75	[:detune]       ; DETUNE
   84	[:spread]       ; SPREAD
   85	[:pitch]        ; PITCH
   
   50	[:lfo1-wheel-sens] ; LFO1 WHEEL SENS
   51	[:lfo1-wheel-speed] ; SPEED
   
   57	[:lfo1 :switch] ; LFO1 SWITCH
   41	[:lfo1 :dest]   ; DEST
   42	[:lfo1 :wave]   ; WAVEFORM
   43	[:lfo1 :speed]  ; SPEED
   44	[:lfo1 :amount] ; AMOUNT
   67	[:lfo1 :tempo-sync] ; TEMPO SYNC
   68	[:lfo1 :key-sync] ; KEY SYNC
   
   58	[:lfo2 :switch] ; LFO2 SWITCH
   46	[:lfo2 :dest]   ; DEST
   47	[:lfo2 :wave]   ; WAVEFORM
   48	[:lfo2 :speed]  ; SPEED
   49	[:lfo2 :amount] ; AMOUNT
   69	[:lfo2 :tempo-sync] ; TEMPO SYNC
   70	[:lfo2 :key-sync] ; KEY SYNC
   })

(defn transform
  [path]
  (let [lines (string/split-lines (slurp path))
        name (first lines)
        params (into {} 
                     (for [line lines :when (.contains line ",")]
                       (vec (map #(Long/parseLong %) 
                                 (string/split line #"\,")))))]
    (reduce (fn [acc [k v]]
              (assoc-in acc (mappings k) v))
            {:name name}
            params)))

(defn convert-folder
  [path]
  (doseq [file (file-seq (java.io.File. path))]
    (when (.endsWith (.getName file) ".sy1")
      (let [clj (java.io.File. (.getParentFile file) 
                              (str (.getName file) ".clj"))
            txt (with-out-str
                  (binding [pprint/*print-right-margin* 150]
                    (pprint/pprint (transform file))))]
        (println (.getPath clj))
        (spit clj txt)))))

(comment
  (binding [pprint/*print-right-margin* 150]
    (pprint/pprint (transform "dev-resources/001.sy1"))))
                 
