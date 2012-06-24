(ns garbaje-compiler.core)

;; Messages are sent via the agent concurrency primitive, I.e. the
;; state and behavior of every actor are encapsulated in an agent. 

(defprotocol Receiver
  (tell [this message sender]))

(defn send-msg
  [sender message recipient]
  (send recipient tell message sender))

(deftype Actor [state behavior]
  Receiver
  (tell [this message sender]
    (let [[state1 behavior1] (behavior state message)]
      (Actor. state1 behavior1))))