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
  
(defn osc
  [id & {:as values}]
  (base (str "osc" id) 
        (merge {:on 1 :level 1.0} values)))

(def osc1 (partial osc 1))
(def osc2 (partial osc 2))
(def osc3 (partial osc 3))
(def osc4 (partial osc 4))
  
(defall 
  va_saw va_tri va_pulse
  pd_saw pd_square pd_pulse pd_double_sine pd_saw_pulse pd_res1 pd_res2 pd_res3 pd_half_sine
  saw double_saw tri pulse pulse_saw slope alpha1 alpha2 exp
  fm1 fm2 fm3 fm4 fm5 fm6 fm7 fm8
  as_saw as_square as_impulse
  noise pink_noise lp_noise bp_noise)

(defn dcf
  [id & {:as values}]
  (base (str "filter" id) 
        (merge {:on 1 :level 1.0} values)))

(def filter1 (partial dcf 1))
(def filter2 (partial dcf 2))

(defall
  lp_24db lp_18db lp_12db lp_6db hp24db
  bp_12db bp_18db notch
  svf_lp svf_hp svf_bp svf_notch)

(defn lfo
  [id & {:as values}]
  (base (str "lfo" id) 
        (merge {:on 1} values)))

(def lfo1 (partial lfo 1))
(def lfo2 (partial lfo 2))
(def lfo3 (partial lfo 3))
(def lfo4 (partial lfo 4))

(defall lfo_sin lfo_tri lfo_saw lfo_pulse lfo_sh lfo_noise)

(defn env
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

; TODO mod sources

; TODO mod targets

; TODO effects
