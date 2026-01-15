(ns todos.nrepl
  (:require
    [nrepl.server]))

(defonce args nil)

(defn -main
  [args]
  (println "# In Clojure: todos.nrepl/main")

  (alter-var-root #'args (constantly args))

  (nrepl.server/start-server :port 4005)
  (println "# In Clojure: nREPL started")

  ;; Keep the REPL runngin
  ;(future @(promise))
  @(promise)
  )
