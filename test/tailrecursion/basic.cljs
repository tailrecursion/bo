(ns tailrecursion.bo.test
  (:require [tailrecursion.bo :as bo]))

(enable-console-print!)
(let [test-bo (bo/BO. (atom {}) (atom [:start]))]
  (bo/add test-bo :foo {:type :object :data 1})
  (bo/add test-bo :bar {:type :object :data 0})
  (bo/update-in! test-bo :bar [:data] 2)
  (println (bo/get-in-object test-bo :bar [:data]))
  (bo/add test-bo :start {:type :behavior
                           :action (fn [this input]
                               (let [stack-object (bo/stack this)
                                     _ (bo/set-stack! this (conj stack-object :end))]
                               (println (str "starting with " input))))
                            })
  (bo/add test-bo :end {:type :behavior
                        :action (fn [this input]
                               (println (str "ending with " input)))
                            })
  (bo/trigger test-bo 0)
  (bo/trigger test-bo 42))

