; Halloway p. 38
; Anonymous Functions

; use a function to do something to a list with filter
(defn divthree "true if n is divisible by 3" [n]
	(= 0 (mod n 3))
)
(println " -- test fun. ")
(println (divthree 39))
(println (divthree 38)	)
(println " -- use fun in filter")
(def mylist `(1,34,5,8,9,345,3,5,74,645,5))
(println (filter divthree mylist))

; now try it anonymously with "fn"
(println (filter
		(fn [n] (= 0 (mod n 3)))
			mylist)
)
; now try it  more tersely with #
(println (filter
		  #(= 0 (mod % 3))
			mylist)
)


; realize you can write local functions now with this and let
(defn my-silly-function "just for scope" []
	(let [ local-div-three  #(= 0 (mod % 3)) ] 
		(println "local:" (filter local-div-three mylist))
		(println "non-local" (filter divthree mylist))
	)
		;(println "local:" (filter local-div-three mylist))
		(println "non-local" (filter divthree mylist))
)
		;(println "local:" (filter local-div-three mylist))
		(println "non-local" (filter divthree mylist))

