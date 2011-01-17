; Halloway functional programming p. 127

; simple lazy-seq
(defn simple-lazy
	([] 
		(concat [0] (simple-lazy 0) )
	)
	([a]
		(let  [n (+ a 1) ]
			(println n)
			(lazy-seq
				(cons n (simple-lazy n) )
			)
		)
	)
)
(println (take 10 (simple-lazy 20))  )

(println (take 10 (simple-lazy 10000))  )

; fibonacci
(defn lazy-seq-fibo
	([]
		(concat [0 1] (lazy-seq-fibo 0 1) ) )
	([a b]
		(let [n (+ a b)]
			(println "recursing: " n)
			(lazy-seq
				(cons n (lazy-seq-fibo b n) ) ) ) ) )
(println (take 10 (lazy-seq-fibo)))
(println (rem (nth  (lazy-seq-fibo) 10000 ) 1000))
