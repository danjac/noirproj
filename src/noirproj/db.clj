(ns noirproj.db
  (:require [clojureql.core :as cql]))
  


(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//localhost:3306/noirproj"
         :user "****"
         :password "*****"})


(def posts (cql/table db :posts))


(defn fetch-results [query]
    "Returns results of query as vector"
    (cql/with-results [item query] (into [] item)))


(defn fetch-one [query]
    "Returns results of first row only"
    (first (fetch-results query)))


(defn latest-posts [num]
    "Retrieves num latest posts"
    (fetch-results (cql/take (cql/sort posts [:id#desc]) num))) 


(defn get-post [post-id]
    (fetch-one (cql/select posts (cql/where (= :id post-id)))))

(defn auth-user [email password]
    {:id 1 :name "danjac"})

(defn add-post! [post]
  (cql/conj! posts post))
