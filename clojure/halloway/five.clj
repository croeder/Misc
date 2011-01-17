; doall, dorun p. 106
(def x 	(for [i (range 1 3)]
		    (do (println i) i)
		)
)
; wait a minute, doesn't a function def take a vector?
;(defn y [ arg1 arg2 ] (println arg1 arg2))
;(y "simple" "function")
;(println " just one: " )
; it does, but thats not a defn, its def
x
(println " doall: " )
(println (doall x))
; and the point of all of this is that you used a range
; to def a var, but the for was not evaluated until the variable
; is evaluated

; dorun, walk a large sequence
(println " dorun: " )
; x needs loaded again, not sure why.
(def x 	(for [i (range 1 3)]
		    (do (println i) i)
		)
)
(println (dorun x))

; Seq-able p. 107
; seq's first/ rest covers Java Collections, Regexes, File systems, xml and rdb 
(println (first (.getBytes "hello")))
(println (rest (.getBytes "hello")))
(println (first (System/getProperties)))
(println (rest (System/getProperties)))
; of course, clojure  structures are seq able too
(println (first `(1 2 3)))
(println (rest `(1 2 3)))

(println (first `[1 2 3]))
(println (rest `[1 2 3]))

(println (first `{:one 1  :two 2 :three 3}))
(println (rest `{:one 1  :two 2 :three 3}))
(println (first `#{1 2 3 3 4}))
(println (rest `#{1 2 3 3 4}))


; regex
; why both loop and recur?
(let [m (re-matcher #"\w+" "the quick brown fox") ]
	(loop [match (re-find m)]
		(when match 
			(println match)
			(recur (re-find m))
		)
	)
)
; despite confusion, book says do this:
(map #(.toUpperCase %) (re-seq #"\w+" "the quick brown fox") )

