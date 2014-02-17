(ns bbcljbot.core
  (:require [xmpp-clj :as xmpp]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :refer [blank?]]
            [clj-time.core :refer [after? now seconds plus]]
            [bbcljbot.handler :refer [handle-message]])
  (:gen-class))

(def state (atom {}))

(defn init []
  (println "running...")
  (swap! state assoc :running true))

(defn wait []
  (while (:running @state)
    (println "type stop to stop >")
    (if (= "stop" (read-line))
      (do (println "stopping")
          (swap! state assoc :running false)))))

(defn start-bot [username password]
  (xmpp/start-bot :username username
                  :password password
                  :host "chat.hipchat.com"
                  :domain "conf.hipchat.com"
                  :handler handle-message))

; seems to get all old messages in a group chat
; so wait 10 seconds
(def ten-second-hack (plus (now) (seconds 10)))

(defn handle-room-message [m]
  (let [body (:body m)]
    (if (and
          body
          (after? (now) ten-second-hack) 
          (re-find #"^@ClojureBot" body))
      (handle-message m)
      nil)))

(defn start-bot-muc [username password]
  (xmpp/start-bot-muc :username username
                      :password password
                      :nick "Clojure Bot"
                      :host "chat.hipchat.com"
                      :room "15400_clojure@conf.hipchat.com"
                      :domain "conf.hipchat.com"
                      :handler handle-room-message))

(def cli-options
  [["-u" "--username USERNAME" "google talk login"
    :default nil]
    ;:validate [#(contains? (set %) \@) "USERNAME should be an email address"]]
   ["-p" "--password PASSWORD" "google talk password"
    :default nil
    :validate [#(seq %) "PASSWORD must not be blank"]]])

(defn run [username password]
  (start-bot username password)
  (start-bot-muc username password)
  (init)
  (wait)
  (println "done")
  (shutdown-agents))

(defn -main [& args]
  (let [params (parse-opts args cli-options)
        username (-> params :options :username)
        password (-> params :options :password)
        errors (-> params :errors)]
    (cond
      errors (doseq [e errors] (println e))
      (not username) (println "USERNAME is required")
      (not password) (println "PASSWORD is required")
      :else
        (run username password))))
