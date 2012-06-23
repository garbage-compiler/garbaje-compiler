(ns garbaje-compiler.tokens)

;; the big challenge here is to do "double spending prevention"
;; the proposed really simple model here would be to have a token
;; encapsulating an :owner and a :value attribute in a map

;; the :value should either be cryptographically signed by the creator
;; or we provide an ownership history or restrict the generation of
;; values (maybe all of the above as variants)

;; further concerns include making tokens durable (i.e. database) also
;; so as not to hold them all in memory at all time

;; to make the game fully distributed one might want to switch to a
;; modification of bitcoin, where coin generation is relatively cheap
;; i have no idea whether this is feasible without a difficult
;; proof-of-work though.

;; Below is a small prototype
;; I haven't even run this code, it's just supposed to be an idea of
;; how to do it.

(defprotocol TokenReceiver
  (give [this message sender tokens]))


(defn authorized? [token owner]
  (= owner (:owner token)))

(defn transfer [token recipient]
  (assoc token :owner recipient))

(defn and*
  ([] true)
  ([e1 & e2]
     (and e1 e2)))

 (defn transact-tokens [sender token-refs recipient]
   @(let [tokens (map deref token-refs)
             authorized
             (reduce and* (map authorized? tokens (repeatedly sender)))]
         (when authorized
           (doseq [tr token-refs]
             (alter tr transfer recipient)))
         authorized))

(defn give-msg
  [sender message recipient tokens]
  (when (transact-tokens sender tokens recipient)     
    (send recipient give message sender tokens)))