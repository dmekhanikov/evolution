(ns evolution.core
  (:gen-class))

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
  (defn cross-len
    [c1 c2 pref-len]
    (concat
      (take pref-len c1)
      (drop pref-len c2)))
  [(cross-len c1 c2 pref-len)
   (cross-len c2 c1 pref-len)])

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

(defn select 
  [population]
  (def fit (map fitness population))
  (def min-fit (reduce min fit))
  (def sum-fit (reduce +
                       (map #(- % min-fit) fit)))
  (def norm-fit
    (if (zero? sum-fit)
      (repeat (count population) (float (/ 1 (count population))))
      (into []
            (map #(/ (- % min-fit) sum-fit)
                 fit))))
  (def roulette (reductions + norm-fit))
  (def zipped-roulette (map vector roulette population))
  (defn select-one
    []
    (def q (rand))
    (second (first
            (filter (fn [[f _]] (>= f q))
                    zipped-roulette))))

  (take (count population)
        (repeatedly #(select-one))))

(defn make-pairs
  [population]
  (def rand-indices
    (shuffle (range (count population))))
  (def pairs-count (/ (count population) 2))
  (def index-pairs
    (map vector 
         (take pairs-count rand-indices)
         (drop pairs-count rand-indices)))
  (map (fn [[f s]] [(nth population f) (nth population s)])
       index-pairs))

(defn evolve
  [population]
  (def parent-pool (select population))
  (def new-generation
    (map #(mutate % mutation-prob)
         (reduce concat
           (map #(if (<= (rand) crossover-prob) (apply cross %)) 
                (make-pairs parent-pool)))))
  (reduce-pop (concat parent-pool new-generation) population-size))

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

