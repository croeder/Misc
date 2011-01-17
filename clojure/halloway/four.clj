; Halloway p. 48

; do says "side effects follow", doesn't effect return value
(defn is-small? [number]
	(if (< number 100)
		"yes"
		(do (println "saw a big one" number)
		"no")))
(println "-----------------")
(let [x (is-small? 123)]
	(println 123 x)
)
(let [x (is-small? 23)]
	(println 23 x)
)
(println "-----------------")

; loop-recur
(loop [result [] x 5]
	(if (zero? x)
		result
		(recur (conj result x) (dec x))
	)
)

; fibonacci is really short!
(loop [result [] x 1 y 1]
		(if (> x 100)
			result
			(recur (conj result  y) y  (+ x y))
		)
)

; p. 51 *dense* example
; Q: what does iterate do?
; (iterate inc 0)
; A: infinite loop starting at 0

; Q: remind me how map works? (map function list)
; (from p. 102)
(defn mydouble [x] (* x 2))
(def mylist `(123 3 54 32 54 76))
(println (map  mydouble mylist))

;Q: what does vector do?
; (from p.99)
; makes a vector of a bunch of elements

; Q: and what is map's 4 parameter about?
; ....its just another arg to the function

; ok, now the example's function "indexed"
(defn indexed [coll]
	(map vector (iterate inc 0) coll)
)

(indexed "abcde")
; vector is taking pairs of values, an index from the iterate
; and a letter from coll. Vector also stops the infinite iterate.


; metadata, like annotations in java
(def stu {:name "Stu" :email "stu@foo.com"})
(def ser-stu (with-meta stu {:serializable true}))

(println stu)
(println ser-stu)
(println (meta stu))
(println (meta ser-stu))


