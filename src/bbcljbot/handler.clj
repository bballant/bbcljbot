(ns bbcljbot.handler
  (:require [clojure.string :as string]))

(defn say-hi [hi] (str (string/replace hi #"(?i)^@ClojureBot" "")  " back"))

(defn say [mstr s] s)

(def myself
  (str "Well, gosh.. really? "
       "It's a long story. "
       "May as well just go here: "
       "https://github.com/bballant/bbcljbot"))

(def handlers
  [[#"yourself" #(say % myself)]
   [#"cool\?" #(say % "cool.")]
   [#"(?i)^hi" #(say-hi %)]
   [#"(?i)^@ClojureBot hi" #(say-hi %)]
   [#"handsome" #(say % "You're too kind.")]])

(defn handle-message [m]
  (println m)
  (let [body (:body m)]
    (if (nil? body)
      nil
      (loop [hs handlers]
        (if (empty? hs)
          nil
          (let [h (first hs)]
            (if (re-seq (first h) body)
              ((last h) body)
              (recur (rest hs)))))))))
