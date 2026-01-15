(ns user
  (:require
    [todos.main :as main]
    [todos.nrepl :as nrepl]
    [cheshire.core :as json])
  (:import
    [java.time Instant]
    [com.luv2code.springboot.todos TodosApplication]
    [com.luv2code.springboot.todos.repository UserRepository]
    [com.luv2code.springboot.todos.request AuthenticationRequest]
    [com.luv2code.springboot.todos.response AuthenticationResponse
                                            UserResponse]))

(println "# Hello from ns user!")

;; Start via `./mvnw-nrepl`!

;; Placeholder for context
(defonce ctx nil)

(defn optional->nilable
  [this]
  (when (.isPresent this)
    (.get this)))

(defn login
  [rest-client credentials]
  (-> rest-client
      .post
      (.uri "/api/auth/login" (into-array []))
      (.header "Content-Type" (into-array ["application/json"]))
      (.body credentials)
      .exchange
      (.returnResult AuthenticationResponse)
      .getResponseBody
      .getToken))

(defn user->map
  [user]
  (-> user
      bean
      (update :authorities
              #(->> %
                    (map (fn [auth] (-> auth bean :authority)))))))

(defn user-map->string
  [map]
  (str (:fullName map)
       " with ID " (:id map)
       " accepts emails at " (:email map)
       " and has roles " (apply str (->> (:authorities map)
                                    (interpose " and ")))))

(defn get-user-info
  [rest-client token]
  (-> rest-client
      .get
      (.uri "/api/users/info" (into-array nil))
      (.header "Authorization" (into-array [(str "Bearer " token)]))
      .exchange
      (.returnResult UserResponse)
      .getResponseBody
      user->map))

(defn get-user-info-as-string
  [rest-client token]
  (-> rest-client
      (get-user-info token)
      user-map->string))

(comment
  ;; Simple tests to ramp up
  (inc 42)
  String
  TodosApplication

  ;; Start the main application and play around with the ApplicationContext
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
      (.findByEmail "mary@luv2code.com")
      optional->nilable
      user->map)

  (let [context ctx
        ;{:keys [context rest-client]} nrepl/args
        user-service (.getBean context "userServiceImpl")
        auth-service (.getBean context "authenticationServiceImpl")
        filter-chain (.getBean context "springSecurityFilterChain")
        auth-req {:email "mary@luv2code.com" :password "test123"}
        auth-req-bean (AuthenticationRequest. (:email auth-req) (:password auth-req))
        auth-res-bean (.login auth-service auth-req-bean)
        auth-res (bean auth-res-bean)
        token (:token auth-res)
        ;(.getUserInfo user-service) ;; UserService.getUserInfo checks the currently authenticated user.
                                     ;; So, how to call all that programatically and yet keep it simple???
                                     ;; Have to emulate the whole web stack, as Spring totally entangles
                                     ;; everything with AOP etc: Transcoding, Error handling, security, validation...
                                     ;; -> RestTestClient comes to our rescue!
        ]
    token)

  ;; Start nREPL via ClojureNrepl.java, so you get the RestTestClient (and a testing context)!
  ;; Login to get a token, and use that to get the current user
  (let [{:keys [rest-client]} nrepl/args
        credentials {:email "mary@luv2code.com" :password "test123"}
        credentials-json (json/generate-string credentials)
        token (login rest-client credentials-json)
        user-as-string (get-user-info-as-string rest-client token)]
     user-as-string)
  )
