

(def global 0)

(defn fun []
	(println "fun" global)
	(if (< global 10)
		(binding [global (+ global 1)] (fun))
	)
)

(fun)		
