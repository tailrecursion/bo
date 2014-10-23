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
  (push! [this element])
  (pull! [this])
  (load! [this label])
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
  (push! [this element] (reset! ->stack (assoc (stack this) :frame (conj (get (stack this) :frame) element))))
  (pull! [this] (let [element (last (get (stack this) :frame))]
                  (reset! ->stack (assoc (stack this) :frame (reverse (rest (reverse (get (stack this) :frame))))))
                  element))
  (load! [this label] (reset! ->stack (assoc (stack this) :pc label)))
  (raise [this event-name input]
    (loop [left-keys (keys (objects this))]
      (if (not (empty? left-keys))
        (let [object-name (first left-keys)
              object-map (get (objects this) object-name)]
          (when (= (get object-map :type) :object)
            (loop [behaviors (get object-map :behaviors)]
              (when (not (empty? behaviors))
                (let [behavior (get (objects this) (first behaviors))]
                  (if (some #(= event-name %) (get behavior :events))
                    ((get behavior :action) this input object-name)))
                (recur (rest behaviors))))
            (recur (rest left-keys)))))))
  (trigger [this input]
    (let [stack-object (stack this)
          transition-name (get stack-object :pc)]
      (when (not (nil? transition-name))
        (let [transition (get (objects this) transition-name)]
          (if (= (get transition :type) :transition)
            ((get transition :action) this input)))))))

