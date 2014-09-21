(defproject tailrecursion/bo "0.1.0"
  :description "A minimal implementation of LightTable's BOT Architecture"
  :url "https://github.com/tailrecursion/bo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src"]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]]
  :cljsbuild {:builds
              {:test
               {:source-paths ["src" "test"]
                :compiler {:output-to "target/test.js"
                           :optimizations :simple
                           :warnings true}
                :jar false}}})
