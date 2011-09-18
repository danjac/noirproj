(ns noirproj.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers)
  (:require [noir.session :as session]
            [noirproj.utils :as utils]))

(defpartial render-flash []
        (if-let [flash-message (session/flash-get)]
            [:div.span-24.notice flash-message]))


(defpartial user-links [user]
    [:li "You are logged in as " [:b (:name user)]]
    [:li (link-to "/logout" "Log out")])

(defpartial navbar []
  [:ul.span-24
    (if-let [user (utils/get-user)]
      (user-links user))])

(defpartial layout [& content]
  (html5
    [:head
     [:title "My first project"]
     (include-css "/css/blueprint/screen.css")]
    [:body
     [:div.container
        (render-flash)
        (navbar)
        [:div.span-24 {:id "content"}
      content]]]))


(defpartial render-error [[error]]
  [:p.error error])


