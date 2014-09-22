(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}))]
  (bo/add test-bo :foo {:type :object :data 1 :behaviors [:specific]})
  (bo/add test-bo :bar {:type :object :data 0 :behaviors [:specific]})
  (bo/update-in! test-bo :bar [:data] 2)
  (println (bo/get-in-object test-bo :bar [:data]))
  (bo/add test-bo :general {:type :behavior
                            :action (fn [this input]
                                      (println (str "Input : " input))
                                      (println (str "Total Objects: " (count (bo/objects this)))))
                            })

  (bo/add test-bo :specific {:type :behavior
                             :triggers [:some-transition]
                             :action (fn [this input object-name] (println input object-name))
                             })
  (bo/trigger test-bo :general 42)
  (bo/raise test-bo :some-transition "Do something with "))

