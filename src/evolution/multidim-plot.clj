(ns evolution.multidim.plot
  (:gen-class)
  (:use [evolution.multidim]
        [evolution.core])
  (:import [org.jzy3d.analysis AnalysisLauncher AbstractAnalysis]
           [org.jzy3d.chart Chart ChartLauncher]
           [org.jzy3d.chart.factories AWTChartComponentFactory]
           [org.jzy3d.colors Color ColorMapper]
           [org.jzy3d.colors.colormaps ColorMapRainbow]
           [org.jzy3d.maths Coord3d Range]
           [org.jzy3d.plot3d.builder Builder Mapper]
           [org.jzy3d.plot3d.builder.concrete OrthonormalGrid]
           [org.jzy3d.plot3d.primitives Shape Sphere]
           [org.jzy3d.plot3d.rendering.canvas Quality]
           [java.util List]))

(defn make-sphere
  [p]
  (doto (Sphere. (Coord3d. (first p) (second p) (f p))
                 0.03 8 Color/RED)
    (.setWireframeColor Color/RED)
    (.setWireframeDisplayed true)
    (.setWireframeWidth 1)
    (.setFaceDisplayed true)))

(defn list-remove [#^List list index]
  (.remove list (int index)))

(defn leave-one
  [l]
  (dotimes [i (- (.size l) 1)]
    (list-remove l (- (.size l) 1))))

(defn plot-multidim
  []
  (let [chart (atom nil)]
    (AnalysisLauncher/open
     (proxy [AbstractAnalysis] []
       (^Chart getChart []
        @chart)
       
       (^String getCanvasType []
        "newt")
       
       (init []
         (let [mapper (proxy [Mapper] []
                        (^double f [^double x ^double y]
                         (evolution.multidim/f [x y])))
               range (Range. (- (first x-range) 3) (+ (second x-range) 3))
               steps 80
               surface (Builder/buildOrthonormal
                        (OrthonormalGrid. range steps range steps) mapper)
               bounds (.getBounds surface)]
           (doto surface
             (.setColorMapper (ColorMapper. (ColorMapRainbow.)
                                            (.getZmin bounds)
                                            (.getZmax bounds)
                                            (Color. 1.0 1.0 1.0 0.5)))
             (.setFaceDisplayed true)
             (.setWireframeDisplayed false))
           (reset! chart (doto (AWTChartComponentFactory/chart
                                Quality/Advanced (.getCanvasType this))
                           (.add surface)))
           (future
             (loop [population (generate-population #(generate-chromosome 2)
                                                    population-size)
                    i 0]
               (let [spheres (map make-sphere population)]
                 (doto (.getScene @chart)
                   (-> .getGraph .getAll leave-one)
                   (-> (.add spheres))))          
               (Thread/sleep 1000)
               (if (< i iterations)
                 (recur (evolve population (/ i iterations) fitness mutate
                                cross population-size)
                        (+ i 1)))))))))))
