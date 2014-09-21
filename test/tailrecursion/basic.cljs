(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}))]
  (add test-bo :foo {:type :object :bar 1 :behaviors [:blah]})
  (add test-bo :baz {:type :object :car 2 :behaviors [:blah]})
  (add test-bo :blah {:type :behavior :triggers [:some-thing]
                      :action (fn [this input] (println input))})
  (raise test-bo :some-thing 5))

