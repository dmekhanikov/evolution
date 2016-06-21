(defproject evolution "0.1.0-SNAPSHOT"
  :description "Evolutional Computations Course Tasks"
  :repositories {"jzy3d-snapshots" "http://maven.jzy3d.org/snapshots"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil "2.4.0"]
                 [org.jzy3d/jzy3d-api "1.0.0-SNAPSHOT"]]
  :main ^:skip-aot evolution.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
