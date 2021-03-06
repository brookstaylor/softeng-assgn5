(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))


;; Do not edit!
;; A map specifying the instructor's office hours that is keyed by day of the week.
(def events {"community-impact"  {"tomorrow"    {:url "https://anchorlink.vanderbilt.edu/events?categories=5978&shortcutdate=tomorrow"}
                                  "anytime"     {:url "https://anchorlink.vanderbilt.edu/events?categories=5978"}}

             "campus-involvement" {"tomorrow"   {:url "https://anchorlink.vanderbilt.edu/events?categories=5977&shortcutdate=tomorrow"}
                                   "anytime"    {:url "https://anchorlink.vanderbilt.edu/events?categories=5977"}}

             "diversity-inclusion" {"tomorrow"  {:url "https://anchorlink.vanderbilt.edu/events?categories=5979&shortcutdate=tomorrow"}
                                    "anytime"   {:url "https://anchorlink.vanderbilt.edu/events?categories=5979"}}

             "faculty-engagement" {"tomorrow"   {:url "https://anchorlink.vanderbilt.edu/events?categories=5980&shortcutdate=tomorrow"}
                                   "anytime"    {:url "https://anchorlink.vanderbilt.edu/events?categories=5980"}}

             "personal-development" {"tomorrow" {:url "https://anchorlink.vanderbilt.edu/events?categories=5982&shortcutdate=tomorrow"}
                                     "anytime"  {:url "https://anchorlink.vanderbilt.edu/events?categories=5982"}}

             "healthy-behaviors" {"tomorrow"    {:url "https://anchorlink.vanderbilt.edu/events?categories=5981&shortcutdate=tomorrow"}
                                  "anytime"     {:url "https://anchorlink.vanderbilt.edu/events?categories=5981"}}

             "gme" {"tomorrow" {:url "https://anchorlink.vanderbilt.edu/events?categories=5530&shortcutdate=tomorrow"}
                    "anytime" {:url "https://anchorlink.vanderbilt.edu/events?categories=5530"}}})


;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg] (first (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg] (rest (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg] {:cmd (cmd msg) :args (args msg)})


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg] {:action :send
                                :to to
                                :msg msg})


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (for [person people] (action-send-msg person msg)))

;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;; (assoc {} :action :assoc-in :ks ks :v v)
(defn action-insert [ks v]
  (assoc {} :action :assoc-in :ks ks :v v))

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (for [key ks] (action-insert (conj prefix key) v)))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
  (assoc {} :action :dissoc-in :ks ks))

;; Asgn 3.
;;
;; @Todo: Create a function called "experts-register"
;; that takes the current application `state`, a `topic`
;; the expert's `id` (e.g., unique name), and information
;; about the expert (`info`) and registers them as an expert on
;; the specified topic. Look at the associated test to see the
;; expected function signature.
;;
;; Your function should NOT directly change the application state
;; to register them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-register [experts topic id info]
   (action-insert [experts topic id] info))


;; Asgn 3.
;;
;; @Todo: Create a function called "experts-unregister"
;; that takes the current application `state`, a `topic`
;; and the expert's `id` (e.g., unique name) and then
;; removes the expert from the list of experts on that topic.
;; Look at the associated test to see the expected function signature.
;;
;; Your function should NOT directly change the application state
;; to unregister them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-unregister [experts topic id]
  (action-remove [experts topic id]))

(defn experts-question-msg [experts question-words]
  (str "Asking " (count experts) " expert(s) for an answer to: \""
       (string/join " " question-words) "\""))

;; Asgn 3.
;;
;; @Todo: Create a function called "ask-experts"
;; that takes two parameters:
;;
;; 1. the list of experts on the topic
;; 2. a parsed message with the format:
;;    {:cmd "ask"
;;     :user-id "phone number that sent the message"
;;     :args [topic question-word1 question-word2 ... question-wordN]}
;;
;; The sender of the message will be identified by their phone number
;; in the user-id parameter. This is the phone number that you will need
;; to forward answers to the question to.
;;
;; The parsed message is generated by breaking up the words in the ask
;; text message. For example, if someone sent the message:
;;
;; "ask food what is the best pizza in nashville"
;;
;; The parsed message would be:
;;
;; {:cmd "ask"
;;  :user-id "+15555555555"
;;  :args ["food" "what" "is" "the" "best" "pizza" "in" "nashville"]}
;;
;; This function needs to return a list with two elements:
;; [[actions...] "response to asker"]
;;
;; The actions in the list are the *side effects* that need to take place
;; to ask the question (e.g., sending messages to the experts). The string
;; is the response that is going to be sent back to the person that asked
;; the question (e.g. "Asking 2 expert(s) for an answer to ....").
;;
;; The correct string response to a valid question should be produced with
;; the `experts-question-msg` function above.
;;
;; Think about how you are going to figure out where to route messages
;; when an expert answers (see the conversations query) and make sure you
;; handle the needed side effect for storing the conversation state.
;;
;; If there are no registered experts on a topic, you should return an
;; empty list of actions and "There are no experts on that topic."
;;
;; If there isn't a question, you should return "You must ask a valid question."
;;
;; Why this strange architecture? By returning a list of the actions to take,
;; rather than directly taking that action, we can keep this function pure.
;; Pure functions are WAY easier to test / maintain. Also, we can isolate our
;; messy impure action handling at the "edges" of the application, where it is
;; easier to track and reason about.
;;
;; You should look at `handle-message` to get an idea of the way that this
;; function is going to be used, its expected signature, and how the actions
;; and output are going to work.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn ask-experts [experts {:keys [args user-id]}]
  (if (empty? experts) [[] "There are no experts on that topic."]
    (if (empty? (rest args)) [[] "You must ask a valid question."]
      [(into [] (concat (action-inserts [:conversations] experts user-id) (action-send-msgs experts (string/join " " (rest args)))))
       (experts-question-msg experts (rest args))])))

;; method to get the url for anytime or tomorrow
(defn display-page [gme time]
  (str (get (get gme time) :url)))


(defn what-events [{:keys [args cmd]}]
  (if (contains? events (first args))
    (if (empty? (rest args)) "You must ask a valid question."
      (str "Here are the " (first args) " events " (first (rest args)) ": " (display-page (get events (first args)) (first (rest args)))))
    (str (first args) " is not a valid GME category. Valid categories are "
      "campus-involement, community-impact, diversity-inclusion, faculty-engagement,"
      " personal-development, healthy-behaviors.")))

(defn gme-info [{:keys [args cmd]}]
  (str "In order to complete GME you need 13 credits. There are 6 tracks with 2 "
    "credits each. The tracks are Campus Involvement, Community Impact, Diversity "
    "and Inclusion, Faculty Engagement, Personal Development, and Healthy Behaviors. "
    "The 13th credit comes from completing 15 hours of community service. You can check "
    "your GME progress by going to https://anchorlink.vanderbilt.edu/, signing into your "
    "account, and looking at your GME path in the bottom right corner."))

(defn welcome [{:keys [args cmd]}]
  (str "Welcome to the GME easy access application. You can text this number "
       "to learn more about GME. Here is a list of valid commands:\n\n\n"

       "'hi' - Prints out list of valid commands.\n\n"
       "'more' - Prints out larger overview of GME and how to track your current progress\n\n"
       "'what' - Sends url that displays events in the specified track. "
              "Acceptable inputs are 'what [any-gme-track-name] [anytime/tomorrow]'. "
              "An example is 'what gme anytime', which will give you the URL for every gme "
              "coming up in the future. Another is 'what diversity-inclusion tomorrow', which "
              "will give you the URL for D&I events occurring tomorrow."))



;; Asgn 3.
;;
;; @Todo: Create a function called "add-expert"
;; that takes two parameters:
;;
;; 1. the current list of experts on the topic
;; 2. a parsed message with the format:
;;    {:cmd "expert"
;;     :user-id "+15555555555"
;;     :args [topic]
;;
;;
;; The parsed message is generated by breaking up the words in the expert
;; text message. For example, if someone sent the message:
;;
;; "expert food"
;;
;; The parsed message would be:
;;
;; {:cmd "expert"
;;  :user-id "+15555555555"))
;;  :args ["food"]}
;;
;; This function needs to add "sara" to the list of experts on "food" and
;; associate her phone number with her ID.
;;
;; Similar to the function `ask-experts` function, this function needs to
;; return the updated `state`, which should now have the expert registered
;; under the specified topic (e.g., "sara" under "food"). The output to
;; send back to the user should be (str expert-id " is now an expert on " topic)
;;
;; The last line of your function should be something like:
;;
;; [new-state (str expert-id " is now an expert on " topic)]
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn add-expert [experts {:keys [args user-id]}]
  (let [msg (str user-id " is now an expert on " (first args) ".")]
    [[(experts-register :expert (first args) user-id {})] msg]))


;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))


(def routes {"default"    (stateless (fn [& args] "Unknown command. Send 'hi' for assistance."))
             "expert"     add-expert
             "ask"        ask-experts
             "what"       (stateless what-events)
             "more"       (stateless gme-info)
             "hello"      (stateless welcome)
             "hi"         (stateless welcome)
             "hey"        (stateless welcome)
             "yo"         (stateless welcome)
             "welcome"    (stateless welcome)})
;; Asgn 3.
;;
;; @Todo: Add mappings of the cmds "expert", "ask", and "answer" to
;; to the `routes` map so that the functions that you
;; created will be invoked when the corresponding text message
;; commands are received.
;;})


;; Don't edit!
(defn experts-on-topic-query [state-mgr pmsg]
  (let [[topic]  (:args pmsg)]
    (list! state-mgr [:expert topic])))


;; Don't edit!
(defn conversations-for-user-query [state-mgr pmsg]
  (let [user-id (:user-id pmsg)]
    (get! state-mgr [:conversations user-id])))

(defn events-for-topic-query [pmsg]
  (let [[track] (:args pmsg)]
    (list! events [track])))


;; Don't edit!
(def queries
  {"expert" experts-on-topic-query
   "ask"    experts-on-topic-query
   "answer" conversations-for-user-query})


;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))


;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
  (fn [pmsg]
    (if (contains? routes (get pmsg :cmd))
      (get routes (get pmsg :cmd))
      (get routes "default"))))

;; Don't edit!
(defn output [o]
  (second o))


;; Don't edit!
(defn actions [o]
  (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))


;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.

    The basic flow to handle a message is as follows:

    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message

  "
  [{:keys [state-mgr] :as system} src msg]
  (go
    (println "=========================================")
    (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          _      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          _      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          _      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          _      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))
          _      (println "  Action results:" arslt)]
      (println "=========================================")
      o)))
