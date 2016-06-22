(ns megabyte.evolution.simple
  (:gen-class)
  (:use [megabyte.evolution.core]))

(def population-size 100)
(def chromosome-size 15)
(def x-range [-10 10])
(def mutation-prob 0.01)
(def crossover-prob 0.5)
(def iterations 100)

(defn f
  [x]
  (if (or (< x -0.3) (> x 0.3))
    (/ (Math/cos (- (* 3 x) 15)) (Math/abs x))
    0))

(defn generate-chromosome
  []
  (take chromosome-size
        (repeatedly #(rand-int 2))))

(defn chromosome-to-int
  [c]
  (reduce
    (fn [res x] (+ (* res 2) x))
    0 c))

(defn chromosome-to-fun-arg
  [chromosome]
  (let [n (chromosome-to-int chromosome)]
    (+ (first x-range)
       (* n (float
             (/
              (-  (second x-range) (first x-range))
              (bit-shift-left 1 (count chromosome))))))))

(defn fitness
  [chromosome]
  (f (chromosome-to-fun-arg chromosome)))

(defn cross
  [c1 c2]
  (let [prefix-len ((comp inc rand-int dec) chromosome-size)
        cross-len (fn [c1 c2 prefix-len]
                    (concat
                     (take prefix-len c1)
                     (drop prefix-len c2)))]
    (if (<= (rand) crossover-prob)
      [(cross-len c1 c2 prefix-len)
       (cross-len c2 c1 prefix-len)]
      [])))

(defn invert
  [c k]
  (concat (take k c) 
          [(- 1 (nth c k))]
          (drop (+ k 1) c)))

(defn mutate
  [c progress]
  (if (<= (rand) mutation-prob)
    (invert c (rand-int (count c)))
    c))

(defn run-simple
  []
  (let [last-population (loop [i 0
                               population (generate-population generate-chromosome population-size)]
                          (if (< i iterations)
                            (recur (+ i 1) (evolve population (/ i iterations) fitness
                                                   mutate cross population-size))
                            population))
        best-chromosome (apply max-key fitness last-population)]
    (println "x: " (chromosome-to-fun-arg best-chromosome))
    (println "y: " (fitness best-chromosome))))
