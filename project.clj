(defproject embedded-pg "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.zonky.test/embedded-postgres "1.2.6" :scope "test"]
                 [org.clojure/java.jdbc "0.7.8"]]

  :managed-dependencies [[io.zonky.test.postgres/embedded-postgres-binaries-darwin-amd64 "10.6.0" :scope "test"]]

  :main ^:skip-aot embedded-pg.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
