(ns evolution.plot
  (:require [quil.core :as q]
            [quil.middleware :as m]))
(use 'evolution.core)

(def black [0 0 0])
(def red [255 0 0])
(def y-range [-4 2])

(defn scale [[x y]]
  (defn scale-one
    [f t target v]
    (* (- v f) 
       (/ target (- t f))))
  [(apply scale-one (conj x-range (q/width) x)) 
   (scale-one (- (second y-range)) (- (first y-range)) (q/height) (- y))])

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

(defn write-stats [population]
  (apply q/fill black)
  (q/text (str "step: " (q/frame-count) "\n"
               "max value: " (->> population
                               (map fitness)
                               (sort)
                               (last)))
          0 20))

(defn setup []
  (q/frame-rate 1)
  (q/text-font (q/create-font "DejaVu Sans" 14 true))
  (generate-population population-size))

(defn update-pop [population]
  (evolve population))

(defn draw [population]
  (q/background 255)
  (draw-plot f 0.01)
  (draw-pop population)
  (write-stats population))

(q/sketch
  :size [600 600]
  :setup setup
  :update update-pop
  :draw draw
  :middleware [m/fun-mode])
