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

; must be space delimited
(def data "15 * 30 + 41 + 3 * 2 + 9 * 8")
;(def data "5 * 3 * 4")

; tokenization
(def parts (seq (.split data " ")))


; poor-man's lexer
; want to map from * / + - <digit> to :mult-op, :add-op, :digit
;   create the map
(def lexmap {"*" :mult-op, "/" :mult-op, "+" :add-op, "-" :add-op, ")" "rparen", "(" "lparen"})
;   apply the map to produce a list of token types from the list of tokens
(def toks (replace lexmap parts))

; now make the token types and the parts themselves be pairs in a list
;(def old-combined (map cons toks parts))

(def combined (map list  toks parts))
(println combined)


; use the list named combined as a symbol queue
(defn get-next []
	["returns next item from queue and shortens it"]
	(let [retval (first combined)]
		(def combined (rest combined))
		retval) )
(defn my-peek []
	["returns next item from queue and does not shorten it"]
	(first combined))

(def indent-space "  ")
(def *indent* "")

(defn parse-term []
	["parses a term using this rule: <term> ==> <num> mult-op <term>"]
	(binding [*indent* (str indent-space *indent*)] 
		(let [lhs (Integer.(second (get-next))) ]
			(println *indent* "entering parse-term" lhs (my-peek))
			(if (and (not (nil? (my-peek))) (= (first(my-peek)) :mult-op ))
				; <term> ==> <num> mult-op <term>
				(let [op (second (get-next)) rhs (parse-term)  ]
						(println *indent* "term lhs:" lhs "rhs:" rhs "term op:" op)
						(cond 
							(= op  "*") (do (println *indent* "exit term *:"  (* lhs rhs)) (* lhs rhs))
							(= op  "/") (do (println *indent* "exit term -:"  (/ lhs rhs)) (/ lhs rhs)))	)
				; <term> ==> <num>
				(do (print *indent* "exit term:" lhs) lhs)	))))

(defn parse-expr []
	["parses an expr using this rule: <expr> ==> <term> add-op <expr>"]
	(binding [*indent* (str indent-space *indent*)] 
		(let [lhs (parse-term) ]
			(println *indent* "entering parse-expr" lhs (my-peek))
			(cond 
				; <expr> ==> <term>  + <expr>
				(and (not (nil? (my-peek))) (= (first (my-peek)) :add-op))
				(let [op (second (get-next)) rhs (parse-expr)]
						(println *indent* " expr lhs:" lhs "rhs:" rhs "expr op:" op)
						(cond 
							(= op  "+") (do (println *indent* "exit expr: + "  (+ lhs rhs)) (+ lhs rhs))
							(= op  "-") (do (println *indent* "exit expr: - "  (- lhs rhs)) (- lhs rhs))) )

				; <expr> ==> <term>
				(or (and (not (nil? (my-peek))) 
						 (= (first (my-peek)) :mult-op)) 
					(nil? (my-peek)))
				(do (print *indent* "exit expr:" lhs) lhs)
			)

		)))


(println (parse-expr))
