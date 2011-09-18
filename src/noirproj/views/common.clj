(ns noirproj.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers)
  (:require [noir.session :as session]
            [noir.server :as server]))

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'noirproj})))

(defpartial render-flash []
        (if-let [flash-message (session/flash-get)]
            [:div.span-24.notice flash-message]))


(defpartial layout [& content]
  (html5
    [:head
     [:title "My first project"]
     (include-css "/css/blueprint/screen.css")]
    [:body
     [:div.container
        (render-flash)
        [:div.span-24 {:id "content"}
      content]]]))


(defpartial render-error [[error]]
  [:p.error error])


