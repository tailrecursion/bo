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
  (update! [this object-name object-map])
  (update-in! [this object-name object-keys value])
  (rm [this object-name])
  (objects [this])
  (by-prop [this meta-info matcher])
  (trigger [this behavior-name input])
  (raise [this trigger-name input]))
```

# Example

```clojure
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

