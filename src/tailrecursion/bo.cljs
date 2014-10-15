(ns tailrecursion.bo)

(defprotocol IBO
  (add [this object-name object-map])
  (get-object [this object-name])
  (get-in-object [this object-name object-keys])
  (update! [this object-name object-map])
  (update-in! [this object-name object-keys value])
  (rm [this object-name])
  (objects [this])
  (by-prop [this meta-info matcher])
  (stack [this])
  (set-stack! [this stack-object])
  (raise [this behavior-name input])
  (trigger [this input]))

(deftype BO [->objects ->stack]
  IBO
  (add [this object-name object-map]
    (if (contains? object-map :type)
      (do
        (reset! ->objects (assoc (objects this) object-name object-map))
        true)
      false))
  (get-object [this object-name] (get (objects this) object-name))
  (get-in-object [this object-name object-keys]
    (get-in (get (objects this) object-name) object-keys))
  (update! [this object-name object-map]
    (if (get (objects this) object-name)
      (do
        (reset! ->objects (assoc (objects this) object-name object-map))
        true)
      false))
  (update-in! [this object-name object-keys value]
    (if (get (objects this) object-name)
      (do
        (reset! ->objects (assoc-in (objects this) (into [object-name] object-keys) value))
        true)
      false))
  (rm [this object-name] (reset! ->objects (dissoc (objects this) object-name)))
  (objects [this] @->objects)
  (by-prop [this meta-info] (by-prop this meta-info =))
  (by-prop [this meta-info matcher]
    (loop [left-keys (keys (objects this))
            matches []]
      (if (empty? left-keys)
        matches
        (let [object-name (first left-keys)
              obj (get (objects this) object-name)
              match-bool
              (loop [meta-keys (keys meta-info)
                      current-bool true]
                (if (or (empty? meta-keys) (= current-bool false))
                  current-bool
                  (let [meta-key (first meta-keys)
                        meta-value (get meta-info meta-key)]
                    (if (contains? obj meta-key)
                      (if (matcher (get obj meta-key) meta-value)
                        (recur (rest meta-keys)
                                true))
                      (recur (rest meta-keys)
                              false)))))]
          (if match-bool
            (recur (rest left-keys)
                    (conj matches {:name object-name :value obj}))
            (recur (rest left-keys)
                    matches))))))
  (stack [this] @->stack)
  (set-stack! [this new-stack] (reset! ->stack new-stack))
  (raise [this behavior-name input]
    (let [behavior (get (objects this) behavior-name)]
            (if (= (get behavior :type) :behavior)
              ((get behavior :action) this input))))
  (trigger [this input]
    (let [stack-object (stack this)
          behavior-name (last stack-object)]
      (when (not (nil? behavior-name))
        (set-stack! this (pop stack-object))
        (raise this behavior-name input)))))

