(def lista '(1 2 3 3 4))
(def mapa {:fname "chris" :lname "roeder"})
(def seta { 1 2 3 4 })
(def vectora [ 1  4 2 3 3 ])

(println (class lista))
(println (class mapa))
(println (class seta))
(println (class vectora))

(def tree 	`(a 
				(b 
					(d 1)
					(e 1)
				)
				(c
					(f 3)
					(g 4)
				)
			)
)

(println tree)


(def aseq `(1 2 3))

(println aseq)
(cons -1 aseq)

(println aseq)


(println (conj `(1 2 3) :a))
(println (conj `(1 2 3) `(9 10 11)))
(println (into `(1 2 3) `(9 10 11)))

