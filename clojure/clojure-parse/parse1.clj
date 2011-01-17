;
; parse.clj
;
; let's start with some thing a little more involved
; a recursive descent parser for integer expressions
; ....recognizing of course, there's probably a straight
; forward way to just have lisp do this for you.
;
;
; <expr> ==> <term>  + <expr>
; <expr> ==> <term> 
; <term> ==> <num> * <term>
; <term> ==> <num>
;

;(def data "5 * 3 + 4")
(def data "55 * 33 * 44")

; use java.lang.String.split()
; it returns an array of String, so 
; construct it (?) back to a clojure seq
(def parts (seq (.split data " ")))

; want to map from * / + - <digit>
;  to :mult-op, :add-op, :digit
(def lexmap {"*" "mult-op", "/" "mult-op", "+" "add-op", "-" "add-op"})
(def toks (replace lexmap parts))
; now make the token types and the parts themselves be pairs in a list

(def combined (map cons toks parts))
(println (type combined))

; forgive for I sin
(defn get-next []
	(let [retval (first combined)]
		(def combined (rest combined))
		retval) )
(defn my-peek []
	(first combined))

(defn parse-term [indent]
	(let [lhs (Integer.(str(second (get-next)))) ]
		(if (not (nil? (my-peek)))
			; <term> ==> <num> * <term>
			(let [op (second (get-next)) rhs (parse-term (str indent "   "))]
					(println indent "lhs:" lhs "rhs:" rhs)
					(cond 
						(= op  \*) (* lhs rhs) 
						(= op  \/) (/ lhs rhs) )) 
			; <term> ==> <num>
			lhs
		) 
	) 
)

(defn parse-expr [indent]
	(let [lhs (Integer.(str(second (get-next)))) ]
		(if (not (nil? (my-peek)))
			; <expr> ==> <term>  + <expr>
			(let [op (second (get-next)) rhs (parse-term (str indent "   "))]
					(println indent "lhs:" lhs "rhs:" rhs)
					(cond 
						(= op  \*) (* lhs rhs) 
						(= op  \/) (/ lhs rhs) )) 
			; <expr> ==> <term> 
			lhs
		) 
	) 
)


(parse-term "")
