(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)

(let [test-bo (bo/BO. (atom {}) (atom {:pc nil :frame []}))]
  (bo/add test-bo :foo {:type :object :data 1 :behaviors [:baz]})
  (bo/add test-bo :bar {:type :object :data 0})
  (bo/update-in! test-bo :bar [:data] 2)
  (println (bo/get-in-object test-bo :bar [:data]))
  (bo/add test-bo :baz {:type :behavior
                        :events [:e]
                        :action (fn [this event-name input obj-name]
                                  (println (str "got " input " for " obj-name " from " event-name)))
                       })
  (bo/raise test-bo :e 5))

