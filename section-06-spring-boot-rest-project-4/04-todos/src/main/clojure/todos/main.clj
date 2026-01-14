(ns todos.main
  (:import
    [com.luv2code.springboot.todos TodosApplication]))

(defn start
  [& args]
  (TodosApplication/run (into-array String args)))

(defn stop
  [context]
  (.stop context))

(defn -main
  [& args]
  (println "# In Clojure: (-main)")
  (let [ctx (apply start args)]
    (println "# Context: " (str ctx)))
  (println "# In Clojure: (-main) RETURN"))
