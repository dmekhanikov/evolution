(ns megabyte.evolution.multidim
  (:use [megabyte.evolution.core]))

(def population-size 100)
(def dim 3)
(def x-range [-5 5])
(def mutation-prob 0.01)
(def crossover-prob 0.5)
(def iterations 20)
(def b 1.0)
(def alpha 0.5)

(defn f
  [xs]
  (+ (- (->> xs
             (map #(/ (* % %) 4000))
             (reduce +))
        (->> xs
             (map-indexed (fn [i x] (/ x
                                       (Math/sqrt (+ i 1)))))
             (map #(Math/cos %))
             (reduce *)))
     1))

(defn generate-chromosome
  [dim]
  (take dim (repeatedly #(+ (rand
                             (- (second x-range) (first x-range)))
                            (first x-range)))))

(defn fitness
  [chromosome]
  (- (f chromosome)))

(defn delta
  [progress y]
  (let [r (rand)]
    (* y
       (- 1
          (Math/pow r
           (Math/pow (- 1 progress)
                     b))))))

(defn modify
  [c progress]
  (let [a (rand-int 1)]
    (if (= a 0)
      (+ c (delta progress (- (second x-range) c)))
      (- c (delta progress (- c (first x-range)))))))

(defn replace-nth
  [col i x]
  (concat (take i col)
          [x]
          (drop (+ i 1) col)))

(defn mutate
  [chromosome progress]
  (let [k (rand-int (count chromosome))
        ck (nth chromosome k)]
    (replace-nth chromosome k (modify ck progress))))

(defn cross-allele
  [a1 a2]
  (let [a-min (min a1 a2)
        a-max (max a1 a2)
        i (- a-max a-min)
        l (- a-min (* i alpha))
        r (+ a-max (* i alpha))]
    (+ (rand (- r l)) l)))

(defn cross
  [c1 c2]
  (let [child (map (partial apply cross-allele)
                   (map vector c1 c2))]
    [child]))

(defn run-multidim
  []
  (let [last-population (loop [i 0
                               population (generate-population
                                           (partial generate-chromosome dim)
                                           population-size)]
                          (let [max-value (f (apply max-key fitness population))]
                            (println "max value: " max-value))
                          (if (< i iterations)
                            (recur (+ i 1) (evolve population (/ i iterations) fitness
                                                   mutate cross population-size))
                            population))
        best-chromosome (apply max-key fitness last-population)]
    (println "best value: " (f best-chromosome))))
