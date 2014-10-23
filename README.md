# BO

A minimal implementation of LightTable's [BOT Architecture](http://www.chris-granger.com/2013/01/24/the-ide-as-data/).

# Patterns

* [Interceptor Pattern](https://en.wikipedia.org/wiki/Interceptor_pattern)
* [Entity Systems](http://entity-systems.wikidot.com/rdbms-with-code-in-systems)

BO can be used to glue different parts within the application with behaviors.

It can help augment the Data Flow in javelin with

* Data Store
* Control Flow

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
  (stack [this])
  (push! [this element])
  (pull! [this])
  (load! [this label])
  (raise [this event-name input])
  (trigger [this input]))
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
```

```
2
got :foo 5
42
popping
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

