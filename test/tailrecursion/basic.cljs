(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}) (atom {:pc nil :frame []}))]
  (bo/add test-bo :foo {:type :object :data 1})
  (bo/add test-bo :bar {:type :object :data 0})
  (bo/update-in! test-bo :bar [:data] 2)
  (println (bo/get-in-object test-bo :bar [:data]))
  (bo/load! test-bo :start)
  (bo/add test-bo :start {:type :behavior
                           :action (fn [this input]
                               (bo/push! this input)
                               (println (str "pushing " input))
                               (bo/load! this :end))
                            })
  (bo/add test-bo :end {:type :behavior
                        :action (fn [this input]
                               (println input)
                               (println (str "popping " (bo/pull! this))))
                            })
  (bo/trigger test-bo 5)
  (bo/trigger test-bo 42))

