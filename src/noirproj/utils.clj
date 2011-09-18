(ns noirproj.utils 
    (:require [noir.session :as session]))


(defn get-user []
  (session/get :user))

(defn logged-in? []
    (not (nil? (get-user))))




