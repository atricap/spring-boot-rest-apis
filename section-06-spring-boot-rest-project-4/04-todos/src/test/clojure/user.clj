(ns user
  (:require
    [todos.main :as main])
  (:import
    [java.time Instant]
    [com.luv2code.springboot.todos TodosApplication]
    [com.luv2code.springboot.todos.repository UserRepository]))

(println "# Hello from ns user!")

;; Placeholder for context
(defonce ctx nil)

(defn optional->nilable
  [this]
  (when (.isPresent this)
    (.get this)))

(defn user->map
  [user]
  (-> user
      bean
      (update :authorities
              #(->> %
                    (map (fn [auth] (-> auth bean :authority)))))))

(comment
  (inc 42)
  String
  TodosApplication

  (alter-var-root #'ctx (constantly (main/start)))
  (str ctx)
  (.getDisplayName ctx)
  (-> (.getStartupDate ctx)
      Instant/ofEpochMilli
      str)
  (.isRunning ctx)
  (main/stop ctx)

  (-> ctx
      (.getBean "userRepository")
      (.findByEmail "eric@example.com")
      optional->nilable
      user->map)
  )
