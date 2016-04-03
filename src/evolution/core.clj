(ns evolution.core
  (:gen-class))

(def population-size 100)
(def max-population-size 200)
(def chromosome-size 15)
(def x-range [-10 10])
(def mutation-prob 0.1)
(def iterations 100)

(defn f
  [x]
  (if (or (< x -0.3) (> x 0.3))
    (/ (Math/cos (- (* 3 x) 15)) (Math/abs x))
    0))

(defn generate-chromosome
  [size]
  (take size
        (repeatedly #(rand-int 2))))

(defn generate-population
  [size]
  (take size
        (repeatedly #(generate-chromosome chromosome-size))))

(defn chromosome-to-int
  [c]
  (reduce
    (fn [res x] (+ (* res 2) x))
    0 c))

(defn chromosome-to-fun-arg
  [chromosome]
  (def n (chromosome-to-int chromosome))
  (+ (first x-range)
     (* n (float
            (/
              (-  (second x-range) (first x-range))
              (bit-shift-left 1 (count chromosome)))))))

(defn fitness
  [chromosome]
  (f (chromosome-to-fun-arg chromosome)))

(defn cross
  [c1 c2]
  (def pref-len (inc (rand-int (dec chromosome-size))))
  (concat
    (take pref-len c1)
    (drop pref-len c2)))

(defn invert
  [c k]
  (concat (take k c) 
          [(- 1 (nth c k))]
          (drop (+ k 1) c)))

(defn mutate
  [c prob]
  (if (<= (rand) prob)
    (invert c (rand-int (count c)))
    c))

(defn reduce-pop
  [population size]
  (take size (reverse (sort-by fitness population))))

(defn evolve
  [population]
  (def new-generation
    (take (- max-population-size (count population))
          (repeatedly #(cross (nth population (rand-int (count population)))
                              (nth population (rand-int (count population)))))))
    (reduce-pop 
      (map #(mutate % mutation-prob)
         (concat population new-generation))
      population-size))

(defn -main
  [& args]
  (def last-population
    (loop [i 0
           population (generate-population population-size)]
      (if (< i iterations)
        (recur (+ i 1) (evolve population))
        population)))
  (def best-chromosome
    (first (reverse (sort-by fitness last-population))))
  (println "x: " (chromosome-to-fun-arg best-chromosome))
  (println "y: " (fitness best-chromosome)))

