(ns noirproj.server
  (:require [noir.server :as server]
            [noir.session :as session]))

(server/load-views "src/noirproj/views/")

(defn authenticate [handler]
  "Looks up user ID in session, pulls data from DB into session"
    (fn [req]
        (let [response (handler req)]
          (session/remove! :user)
          (if-let [user-id (session/get :user-id)]
              (do
                (session/put! :user {:name "danjac"}))) 
            response)))

(server/add-middleware authenticate)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'noirproj})))

