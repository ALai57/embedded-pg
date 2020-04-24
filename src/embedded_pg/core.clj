(ns embedded-pg.core
  (:require [clojure.java.jdbc :as jdbc])
  (:import (io.zonky.test.db.postgres.embedded EmbeddedPostgres)))


;; Run migrations if you have them
#_(defn pg-db->migratus-config [db-spec]
    {:migration-dirs "migrations"
     :store :database
     :db db-spec})

(def test-pg (-> (EmbeddedPostgres/builder)
                 .start))

(def ^:dynamic db-spec {:classname "org.postgresql.Driver"
                        :subprotocol "postgresql"
                        :subname (str "//localhost:" (.getPort test-pg) "/postgres")
                        :user "postgres"})

;; Migratus again...
#_(m/migrate (pg-db->migratus-config db-spec))

(jdbc/with-db-transaction [db db-spec]
  (jdbc/query db-spec "SELECT * from fruit"))
;;ERROR: relation "fruit" does not exist

(jdbc/with-db-transaction [db db-spec]
  (jdbc/query db-spec "SELECT version()"))
;; => nil;;({:version PostgreSQL 10.6 on x86_64-apple-darwin, compiled by
;;i686-apple-darwin11-llvm-gcc-4.2 (GCC) 4.2.1 (Based on Apple Inc. build 5658)
;;(LLVM build 2336.11.00), 64-bit})

(jdbc/with-db-transaction [db db-spec]
  (jdbc/db-set-rollback-only! db)
  (binding [db-spec db]
    (let [fruit-table-ddl (jdbc/create-table-ddl :fruit
                                                 [[:name "varchar(32)"]
                                                  [:appearance "varchar(32)"]
                                                  [:cost :int]
                                                  [:grade :real]])]
      (jdbc/db-do-commands db-spec [fruit-table-ddl])
      (jdbc/insert! db-spec :fruit {:name "apple"
                                    :appearance "Awesome"
                                    :cost 1
                                    :grade 10.0})
      (jdbc/query db-spec "SELECT * FROM fruit"))))
;; => ({:name "apple", :appearance "Awesome", :cost 1, :grade 10.0})


(jdbc/with-db-transaction [db db-spec]
  (jdbc/query db-spec "SELECT * from fruit"))
;; => ;;ERROR: relation "fruit" does not exist
