(ns rogue-presets.utils)

(defmacro defall
  [& args]
  (cons 'do
        (for [[arg idx] (zipmap args (range))]
          (list 'def arg idx))))        

(defn- base
  [id options]
  (into {} (for [[k v] options]
             [(str id "_" (name k)) v])))
  
(defn- osc
  [id & {:as values}]
  (base (str "osc" id) 
        (merge {:on 1} values)))

(def osc1 (partial osc 1))
(def osc2 (partial osc 2))
(def osc3 (partial osc 3))
(def osc4 (partial osc 4))
  
(defall va_saw va_tri va_pulse
        pd_saw pd_square pd_pulse pd_double_sine pd_saw_pulse pd_res1 pd_res2 pd_res3 pd_half_sine
        saw double_saw tri pulse pulse_saw slope alpha1 alpha2 exp
        fm1 fm2 fm3 fm4 fm5 fm6 fm7 fm8
        as_saw as_square as_tri
        supersaw supersquare supersaw2 supersquare2
        noise pink_noise lp_noise bp_noise)

(defn- dcf
  [id & {:as values}]
  (base (str "filter" id) 
        (merge {:on 1} values)))

(def filter1 (partial dcf 1))
(def filter2 (partial dcf 2))

(defall lp_24db hp_24db bp_24db lp_12db hp_12db bp_12db 
        moog_24db
        svf_lp svf_hp svf_bp svf_notch
        comb)

(defn- lfo
  [id & {:as values}]
  (base (str "lfo" id) 
        (merge {:on 1} values)))

(def lfo1 (partial lfo 1))
(def lfo2 (partial lfo 2))
(def lfo3 (partial lfo 3))
(def lfo4 (partial lfo 4))

(defall lfo_sin lfo_tri lfo_saw lfo_pulse lfo_sh lfo_noise)

(defn- env
  [id & {:as values}]
  (base (str "env" id) 
        (merge {:on 1} values)))

(def env1 (partial env 1))
(def env2 (partial env 2))
(def env3 (partial env 3))
(def env4 (partial env 4))

(defn bus
  [id & {:as values}]
  (base id values))

(def bus_a (partial bus "bus_a"))
(def bus_b (partial bus "bus_b"))

; mod sources
(defall mod_none mod_mod mod_press mod_key mod_velo
        mod_lfo1 mod_lfo1+ mod_lfo2 mod_lfo2+ mod_lfo3 mod_lfo3+ mod_lfo4 mod_lfo4+
        mod_env1 mod_env2 mod_env3 mod_env4)

; mod targets
(defall mod_no_target
        mod_osc1_pitch mod_osc1_mod mod_osc1_pwm mod_osc1_amp
        mod_osc2_pitch mod_osc2_mod mod_osc2_pwm mod_osc2_amp
        mod_osc3_pitch mod_osc3_mod mod_osc3_pwm mod_osc3_amp
        mod_osc4_pitch mod_osc4_mod mod_osc4_pwm mod_osc4_amp
        
        mod_flt1_freq mod_flt1_q mod_flt1_pan mod_flt1_amp
        mod_flt2_freq mod_flt2_q mod_flt2_pan mod_flt2_amp
        
        mod_lfo1_sp mod_lfo1_amp
        mod_lfo2_sp mod_lfo2_amp
        mod_lfo3_sp mod_lfo3_amp
        mod_lfo4_sp mod_lfo4_amp
        
        mod_env1_sp mod_env1_amp
        mod_env2_sp mod_env2_amp
        mod_env3_sp mod_env3_amp
        mod_env4_sp mod_env4_amp
        
        mod_bus_a_pan mod_bus_b_pan)

(defn modulations
  [& mods]
  (apply merge 
         (for [[[src target amount] idx] (zipmap mods (range 1 20))]
           {(str "mod" idx "_src") src
            (str "mod" idx "_target") target
            (str "mod" idx "_amount") amount})))                 
        
; effects

(defn chorus-fx
  [& {:as values}]
  (base "chorus"
        (merge {:on 1} values)))

(defn phaser-fx
  [& {:as values}]
  (base "phaser"
        (merge {:on 1} values)))

(defn delay-fx
  [& {:as values}]
  (base "delay"
        (merge {:on 1} values)))

(defn reverb-fx
  [& {:as values}]
  (base "reverb"
        (merge {:on 1} values)))

; stubs

(def defaults 
  (let [text (slurp "resources/rogue.ttl")
        keys (map second (re-seq #"lv2:symbol \"([\w_]+)\"" text))
        values (map second (re-seq #"lv2:default ([\d\.]+)" text))]
    (merge 
      (zipmap (drop 3 keys) values)
      {"osc1_level" 0.0
       "osc1_level_a" 0.0
       "filter1_on" 0.0
       "filter1_level" 0.0
       "lfo1_on" 0.0})))

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

(def two-oscs
  (merge defaults
         (osc1 :level 1 :level_a 0.5)
         (osc2 :level 1 :level_a 0.5)))

(def four-oscs
  (merge defaults
         (osc1 :level 1 :level_a 0.25)
         (osc2 :level 1 :level_a 0.25)
         (osc3 :level 1 :level_a 0.25)
         (osc4 :level 1 :level_a 0.25)))

(def four-oscs-ab
  (merge defaults
         (osc1 :level 1 :level_a 0.25)
         (osc2 :level 1 :level_a 0.25)
         (osc3 :level 1 :level_b 0.25)
         (osc4 :level 1 :level_b 0.25)))

