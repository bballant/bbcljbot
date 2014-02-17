(defproject bbcljbot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [name.benjaminpeter/xmpp-clj "0.3.3"]
                 [clj-time "0.6.0"]
                 [org.clojure/tools.cli "0.3.1"]]
  :main ^:skip-aot bbcljbot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
