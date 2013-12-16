(ns rogue-presets.noisemaker
  (:require [rogue-presets.utils :refer :all]
            [rogue-presets.ext.noisemaker :as nm]))

(def big-synth-orig
 {:programname "KB Big Synth FN" :volume 0.316000015 :filtertype 0.363636374
  :cutoff 0.15200001 :resonance 0.30400002 :osc1volume 0.792000055
  :osc2volume 0.792000055 :osc3volume 0 :osc1waveform 0 :osc2waveform 0
  :oscsync 0 :oscmastertune 0.5 :osc1tune 0.49000001 :osc2tune 0.756000042
  :osc1finetune 0.524000049 :osc2finetune 0.480000019 :portamento 0
  :keyfollow 0 :filtercontour 0.5 :filterattack 0 :filterdecay 0
  :filtersustain 1 :filterrelease 0 :ampattack 0 :ampdecay 0.512000024
  :ampsustain 0.156000003 :amprelease 0.616000056 :voices 1
  :portamentomode 0 :lfo1waveform 0 :lfo2waveform 0 :lfo1rate 0
  :lfo2rate 0 :lfo1amount 0 :lfo2amount 0 :lfo1destination 0
  :lfo2destination 0 :lfo1phase 0 :lfo2phase 0 :osc1pw 0 :osc2fm 0
  :osc1phase 0 :osc2phase 0 :transpose 0.668000042 :freeadattack 0
  :freeaddecay 0.131999999 :freeadamount 0.736000061 :freeaddestination 0.600000024
  :lfo1sync 0 :lfo1keytrigger 0 :lfo2sync 0 :lfo2keytrigger 0
  :velocityvolume 0 :velocitycontour 0 :velocitycutoff 0 :pitchwheelcutoff 0.252000004
  :pitchwheelpitch 0 :highpass 0 :detune 0 :vintagenoise 0
  :ringmodulation 0 :chorus1enable 1 :chorus2enable 0 :reverbwet 0.328000009
  :reverbdecay 0.396000028 :reverbpredelay 0.172000006 :reverbhighcut 0.272000015
  :reverblowcut 1 :oscbitcrusher 1 :filterdrive 0 :delaywet 0
  :delaytime 0.5 :delaysync 0 :delayfactorl 0 :delayfactorr 0
  :delayhighshelf 0 :delaylowshelf 0 :delayfeedback 0.5 :envelopeeditordest1 0
  :envelopeeditorspeed 0 :envelopeeditoramount 0 :envelopeoneshot 0
  :envelopefixtempo 0 :tab1open 1 :tab2open 0 :tab3open 0 :tab4open 1})

; TODO function to read noisemaker xml preset into clojure map

(defpreset big-synth "KB Big Synth FN"
  defaults
  (nm/transform big-synth-orig))
  