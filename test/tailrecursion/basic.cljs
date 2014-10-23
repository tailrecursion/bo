(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}) (atom {:pc nil :frame []}))]
  (bo/add test-bo :foo {:type :object :data 1 :behaviors [:baz]})
  (bo/add test-bo :bar {:type :object :data 0})
  (bo/update-in! test-bo :bar [:data] 2)
  (println (bo/get-in-object test-bo :bar [:data]))
  (bo/load! test-bo :start)
  (bo/add test-bo :baz {:type :behavior
                        :events [:e]
                        :action (fn [this input obj-name]
                                  (println (str "got " input " for " obj-name)))
                       })
  (bo/add test-bo :cux {:type :transition
                        :action (fn [this input]
                               (println input)
                               (println (str "popping " (bo/pull! this))))
                       })
  (bo/raise test-bo :e 5)
  (bo/load! test-bo :cux)
  (bo/trigger test-bo 42))

