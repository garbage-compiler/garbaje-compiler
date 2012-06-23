(ns garbaje-compiler.core)

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