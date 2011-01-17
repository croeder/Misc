(def a `( a b c ))
(def b ["a" "b" "c"])
(def c `( "a" "b" "c" ))
(def n `(33 34 23 9  64 123))

(println "hello world")

(println "enter a number: ")
;(let [x (read-line)]
;   (println "your number is" (first x))
;)

(println "the nill comes from something else")


(defn first-fun "doc text" [a b c] 
   ( println a b c )
)

(first-fun "foo" "bar" "baz")


(defn second-fun "doc text" [a] 
	(println a)
)
; ------------ p 100 in Halloway ---------------
(second-fun "booger")
(second-fun '("foo" "bar" "baz"))
(println "-------------------------------")
(defn for-play "doc text" [a]
	(let [newseq 
		(for [x  a]
			;(println "-->x" x)
			(* x 2)
		)]
		(println "newseq is: " newseq)
	)
)


(for-play n)

(println "-------------------------------")
(defn filter-play-1 "doc text" [a]
	(let [newseq (filter even? n) ]
		(println "newseq is: " newseq)
	)
)

(filter-play-1 n)

(println "-------------------------------")
(defn mult3? "tells if a number is evenly divisible by 3" [a]
	(= (mod a 3) 0) 
)
(println (mult3? 33) ) ; don't need no steenking "if"
(println (mult3? 34) )

(defn filter-play-2 "use mult3 in a filter" [a]
	(println (filter mult3? a))
)
(filter-play-2 n)
(println (filter (complement mult3?) n))

(println "-------------------------------")
(defn times-two [a] (* a 2))
(println (map times-two n))

(println "-------------------------------")
(println (reduce + n))
(println "-------------------------------")
(println (reduce + (map times-two n)))



