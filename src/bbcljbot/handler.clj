(ns bbcljbot.handler)

(defn say-hi [hi] (str hi " back"))
(defn say [mstr s] s)

(def handlers
  [[#"yourself" #(say % "well, gosh.. really?")]
   [#"cool\?" #(say % "cool.")]
   [#"(?i)^hi" #(say-hi %)]])

(defn handle-message [m]
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
