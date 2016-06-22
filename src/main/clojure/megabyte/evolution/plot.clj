(ns megabyte.evolution.plot
  (:require [quil.core :as q])
  (:use [megabyte.evolution.core]))

(def black [0 0 0])
(def red [255 0 0])

(defn scale
  [[x y] conf]
  (let [scale-one (fn [f t target v]
                    (* (- v f)
                       (/ target (- t f))))]
    [(apply scale-one (conj (:x-range conf) (q/width) x)) 
     (scale-one (- (second (:y-range conf))) (- (first (:y-range conf))) (q/height) (- y))]))

(defn draw-plot
  [conf]
  (q/stroke-weight 1)
  (apply q/stroke black)
  (doseq [two-points (->> (apply range (conj (:x-range conf) (:step conf)))
                          (map (fn [x] [x ((:f conf) x)]))
                          (map #(scale % conf))
                          (partition 2 1))]
    (apply q/line two-points)))

(defn draw-pop
  [population conf]
  (q/stroke-weight 5)
  (apply q/stroke red)
  (doseq [point (->> population
                     (map (:chromosome-to-fun-arg conf))
                     (map (fn [x] [x ((:f conf) x)]))
                     (map #(scale % conf)))]
    (apply q/point point)))

(defn write-stats
  [population conf]
  (apply q/fill black)
  (q/text (str "step: " (q/frame-count) "\n"
               "max fitness: " (->> population
                                    (map (:fitness conf))
                                    sort
                                    last))
          0 20))

(defn update-pop
  [[population conf]]
  (let [progress (/ (q/frame-count) (:iterations-count conf))]
    [(apply evolve population progress ((juxt :fitness :mutate :cross :population-size) conf)) conf]))

(defn draw
  [[population conf]]
  (q/background 255)
  (draw-plot conf)
  (draw-pop population conf)
  (write-stats population conf))
