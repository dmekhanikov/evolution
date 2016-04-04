(ns evolution.plot
  (:require [quil.core :as q]
            [quil.middleware :as m]))
(use 'evolution.core)

(def black [0 0 0])
(def red [255 0 0])
(def y-range [-2 2])

(defn scale [[x y]]
  (defn scale-one
    [f t target v]
    (* (- v f) 
       (/ target (- t f))))
  [(apply scale-one (conj x-range (q/width) x)) 
   (apply scale-one (conj y-range (q/height) (- y)))])

(defn draw-plot [f step]
  (q/stroke-weight 1)
  (apply q/stroke black)
  (doseq [two-points (->> (apply range (conj x-range step))
                          (map (fn [x] [x (f x)]))
                          (map scale)
                          (partition 2 1))]
    (apply q/line two-points)))

(defn draw-pop [population]
  (q/stroke-weight 5)
  (apply q/stroke red)
  (doseq [point (->> population
                     (map chromosome-to-fun-arg)
                     (map (fn [x] [x (f x)]))
                     (map scale))]
    (apply q/point point)))

(defn setup []
  (q/frame-rate 1)
  (generate-population population-size))

(defn update-pop [population]
  (evolve population))

(defn draw [population]
  (q/background 255)
  (draw-plot f 0.01)
  (draw-pop population))

(q/defsketch trigonometry
  :size [600 600]
  :setup setup
  :update update-pop
  :draw draw
  :middleware [m/fun-mode])