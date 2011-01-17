
(defn usesFn [ x ]
	(let [ z  1] 
		(println z)
		(x)
		(println z)
	)
)


(defn myFun []
	(def z (+ z 1) )
)

(usesFn myFun)

