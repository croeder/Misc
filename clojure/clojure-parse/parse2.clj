;
; parse.clj
;
; let's start with some thing a little more involved
; a recursive descent parser for integer expressions
; ....recognizing of course, there's probably a straight
; forward way to just have lisp do this for you.
;
; 
; <expr> ==> <term> <add-op> <expr>
; <expr> ==> <term> 
;
; <term> ==> <num> <mult-op> <term>
; <term> ==> <num>
;
;   next:(not implemented)
; <term> ==> <l paren> <expr> <r paren>
;

(def data "15 * 30 + 41")
;(def data "5 * 3 * 4")

(def parts (seq (.split data " ")))


; poor-man's lexer
; want to map from * / + - <digit> to :mult-op, :add-op, :digit
;   create the map
(def lexmap {"*" :mult-op, "/" :mult-op, "+" :add-op, "-" :add-op, ")" "rparen", "(" "lparen"})
;   apply the map to produce a list of token types from the list of tokens
(def toks (replace lexmap parts))

; now make the token types and the parts themselves be pairs in a list
;(def old-combined (map cons toks parts))
;(println old-combined)
(def combined (map list  toks parts))
(println combined)


(defn get-next []
	(let [retval (first combined)]
		(def combined (rest combined))
		retval) )
(defn my-peek []
	(first combined))

(def indent-space "  ")

(defn parse-term [indent]
	(let [lhs (Integer.(str(second (get-next)))) ]
		(println indent "entering parse-term" lhs (my-peek))
		(if (and (not (nil? (my-peek))) (= (first(my-peek)) :mult-op ))
			; <term> ==> <num> mult-op <term>
			(let [op (second (get-next)) rhs (parse-term (str indent indent-space))]
					(println indent "term lhs:" lhs "rhs:" rhs "term op:" op)
					(cond 
						(= op  "*") (do (println "exit term *:"  (* lhs rhs)) (* lhs rhs))
						(= op  "/") (do (println "exit term -:"  (/ lhs rhs)) (/ lhs rhs)))	)
			; <term> ==> <num>
			(do (print "exit term:" lhs) lhs)
			)))

(defn parse-expr [indent]
	(let [lhs (parse-term (str indent indent-space)) ]
		(println indent "entering parse-expr" lhs (my-peek))
		(if (not (nil? (my-peek))) 
			; <expr> ==> <term>  + <expr>
			(cond 
				(= (first (my-peek)) :add-op)
				(let [op (second (get-next)) rhs (parse-expr (str indent indent-space))]
						(println indent " expr lhs:" lhs "rhs:" rhs "expr op:" op)
						(cond 
							(= op  "+") (do (println "exit expr: + "  (+ lhs rhs)) (+ lhs rhs))
							(= op  "-") (do (println "exit expr: - "  (- lhs rhs)) (- lhs rhs))) )
				(= (first (my-peek)) :mult-op)
				lhs )
			; <expr> ==> <term> 
			(do (print "exit expr:" lhs) lhs)
			)))


(println (parse-expr ""))
