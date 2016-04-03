(defproject evolution "0.1.0-SNAPSHOT"
  :description "Evolutional Computations Course Tasks"
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot evolution.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
