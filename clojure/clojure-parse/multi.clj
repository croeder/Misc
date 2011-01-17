(println "starting")

(defn foo [a]
	(println a)
	(let [a (+ 3 4) b (+ 4 3)]
		(println a b)
	)
	(println a )	
)


(foo 55)	
(println "ending")
