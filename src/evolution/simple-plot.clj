(ns evolution.simple.plot
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:use [evolution.core]
        [evolution.simple]
        [evolution.plot]))

(def conf {:f f
           :x-range x-range
           :y-range [-4 2]
           :chromosome-to-fun-arg chromosome-to-fun-arg
           :fitness fitness
           :cross cross
           :mutate mutate
           :step 0.01
           :population-size population-size
           :iterations-count 100})

(defn setup
  []
  (q/frame-rate 1)
  (q/text-font (q/create-font "DejaVu Sans" 14 true))
  [(generate-population generate-chromosome population-size) conf])

(defn plot-simple
  []
  (q/sketch
   :size [600 600]
   :setup setup
   :update update-pop
   :draw draw
   :middleware [m/fun-mode]))
