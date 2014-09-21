(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}))]
  (bo/add test-bo :foo {:type :object :bar 1 :behaviors [:blah]})
  (bo/add test-bo :baz {:type :object :car 2 :behaviors [:blah]})
  (bo/update-in! test-bo :baz [:car] 2)
  (println (bo/get-object test-bo :baz))
  (bo/add test-bo :blah {:type :behavior :triggers [:some-thing]
                      :action (fn [this input] (println input))})
  (bo/raise test-bo :some-thing 5))

