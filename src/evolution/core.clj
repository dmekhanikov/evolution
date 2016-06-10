(ns evolution.core
  (:gen-class))

(defn reduce-pop
  [population fitness size]
  (take size (reverse (sort-by fitness population))))

(defn select 
  [population fitness]
  (let [fit (map fitness population)
        min-fit (reduce min fit)
        sum-fit (reduce +
                        (map #(- % min-fit) fit))
        norm-fit (if (zero? sum-fit)
                   (repeat (count population) (float (/ 1 (count population))))
                   (into []
                         (map #(/ (- % min-fit) sum-fit)
                              fit)))
        roulette (reductions + norm-fit)
        zipped-roulette (map vector roulette population)
        select-one (fn []
                     (let [q (rand)]
                       (->> zipped-roulette
                            (filter (fn [[r _]] (>= r q)))
                            first
                            second)))]
    (take (count population)
          (repeatedly #(select-one)))))

(defn make-pairs
  [population]
  (let [index-pairs (partition 2
                               (shuffle (range (count population))))]
    (map (fn [[f s]] [(nth population f) (nth population s)])
         index-pairs)))

(defn evolve
  [population fitness mutate cross population-size]
  (let [parent-pool (select population fitness)
        new-generation (->> parent-pool
                            make-pairs
                            (map #(apply cross %))
                            (reduce concat)
                            (map mutate))]
    (reduce-pop (concat parent-pool new-generation) fitness population-size)))
