# BO

A minimal implementation of LightTable's [BOT Architecture](http://www.chris-granger.com/2013/01/24/the-ide-as-data/).

BO can be used to glue different parts within the application with behaviors.
It can help augment the Data Flow in javelin with

* Data Store
* Control Flow

# See

* [Entity Systems](http://entity-systems.wikidot.com/rdbms-with-code-in-systems)

# Protocol

```clojure
(defprotocol IBO
  (add [this object-name object-map])
  (get-object [this object-name])
  (get-in-object [this object-name object-keys])
  (update! [this object-name object-map])
  (update-in! [this object-name object-keys value])
  (rm [this object-name])
  (objects [this])
  (by-prop [this meta-info matcher])
  (raise [this event-name input]))
```

# Example

```clojure
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
```

```
2
got 5 for :foo from :e
```

# Testing

```
$ lein cljsbuild auto
$ node target/test.js
```

# Status

Alpha.

## License

Copyright (c) Sreeharsha Mudivarti. All rights reserved.

The use and distribution terms for this software are
covered by the Eclipse Public License 1.0
(http://opensource.org/licenses/eclipse-1.0.php) which can be
found in the file epl-v10.html at the root of this
distribution. By using this software in any fashion, you are
agreeing to be bound by the terms of this license. You must not
remove this notice, or any other, from this software.

