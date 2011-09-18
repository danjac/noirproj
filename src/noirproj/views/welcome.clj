(ns noirproj.views.welcome
  (:require [noirproj.views.common :as common]
            [noirproj.db :as db]
            [noirproj.utils :as utils]
            [noir.content.pages :as pages]
            [noir.response :as res]
            [noir.session :as session]
            [noir.statuses :as statuses]
            [noir.validation :as vali])
  (:use noir.core
        hiccup.core
        hiccup.form-helpers
        hiccup.page-helpers))

(defn valid-post? [{:keys [title author details]}]
    (vali/rule (vali/has-value? title)
        [:title "Title is missing"])
    (vali/rule (vali/has-value? author)
        [:author "Your name is missing"])
    (vali/rule (vali/has-value? details)
        [:details "You need to add some details"])
    (not (vali/errors? :title :author :details)))

(defn valid-login? [login]
    true)

;(statuses/set-page! 404 
;  (common/layout [:p "Sorry, the page you are looking for cannot be found"]))

(defpartial post-fields [{:keys [title author details]}]
    [:dl
        [:dd
          (vali/on-error :title common/render-error)
          (label :title "Title of your post")
          (text-field :title title)]
        [:dd
          (vali/on-error :details common/render-error)
          (label :details "Details of your post")
          (text-area :details details)]
        [:dd
          (vali/on-error :author common/render-error)
          (label :author "Your name")
          (text-field :author author)]])


(defpartial login-fields [{:keys [email password]}]
    [:dl
        [:dd
            (vali/on-error :email common/render-error)
            (label :email "Your email address")
            (text-field :email email)]
        [:dd
            (vali/on-error :password common/render-error)
            (label :password "Your password")
            (password-field :password)]])

    

(defpartial post-item [post]
  [:li (link-to (str "/post/" (:id post)) (str (:title post)", " (:author post)))])


(defpage "/" []
     (common/layout
       [:p "Welcome to my site"]

       [:ul {:class "posts"}
          (map post-item (db/latest-posts 6))]

       (link-to "/new" "Add a new post")))


(pre-route [:any "/new"] {}
           (when-not (utils/logged-in?)
             (res/redirect "/login")))

(defpage "/new" {:as post}
    (common/layout
       [:p "Add a new post"]
       [:p 
          (link-to "/" "Cancel")]
       (form-to [:post "/new"]
          (post-fields post) 
          (submit-button {:id "foo"} "Submit"))))

(defpage [:post "/new"] {:as post}
    (if (valid-post? post)
        (do 
          (db/add-post! post)
          (session/flash-put! "Thanks for posting")
          (res/redirect "/"))
        (render "/new" post)))
     

(defpage "/post/:id" {:keys [id]}
    (if-let [post (db/get-post id)]
        (common/layout 
           [:h2 (:title post)]
            [:h3 (:author post)]
            [:p (:details post)])))


(defpage "/logout" []
  (session/clear!)
  (res/redirect "/"))


(defpage "/login" {:as login}
    (common/layout 
        [:h2 "Login"]
        (form-to [:post "/login"]
            (login-fields login)
            (submit-button "Login"))))


(defpage [:post "/login"] {:as login}
    (if (valid-login? login)
      (if-let [user (db/auth-user (:email login) (:password login))]
         (do 
            (session/put! :user-id (:id user))
            (session/flash-put! (format "Welcome back, %s" (:name user)))
            (res/redirect "/")))))

;   (render "/login" login))
     
          
