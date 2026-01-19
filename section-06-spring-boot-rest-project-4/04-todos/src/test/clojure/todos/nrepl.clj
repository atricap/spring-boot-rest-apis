(ns todos.nrepl
  (:require
    [nrepl.server]
    [cider.nrepl.middleware]
    [refactor-nrepl.middleware]))

(defonce args nil)

(defn -main
  [args]
  (println "# In Clojure: todos.nrepl/main")

  (alter-var-root #'args (constantly args))

  (def custom-nrepl-handler
    "We build our own custom nrepl handler, mimicking CIDER's."
    (apply nrepl.server/default-handler
           (conj cider.nrepl.middleware/cider-middleware
                 'refactor-nrepl.middleware/wrap-refactor)))

  (nrepl.server/start-server :port 4005
                             :handler custom-nrepl-handler)
  (println "# In Clojure: nREPL started")

  ;; Keep the REPL runngin
  ;(future @(promise))
  @(promise)
  )
