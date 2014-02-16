(ns bbcljbot.core
  (:require [xmpp-clj :as xmpp]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :refer [blank?]]
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
                  :host "talk.google.com"
                  :domain "gmail.com"
                  :handler handle-message))

(def cli-options
  [["-u" "--username USERNAME" "google talk login"
    :default nil
    :validate [#(contains? (set %) \@) "USERNAME should be an email address"]]
   ["-p" "--password PASSWORD" "google talk password"
    :default nil
    :validate [#(seq %) "PASSWORD must not be blank"]]])

(defn run [username password]
  (start-bot username password)
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
