; Halloway p. 40 Vars, Bindings and Namespaces

; global var "root binding"
(def foo 10)
(println "variable" foo)

; get the symbol from/for, bound-to, the variable
(println "symbol from var" (var foo))
(println "symbol from macro:"  #'foo)

; let creates a scope, a lexical binding(s)
(let  [x 10 y 20]
	(println x y)
)
; fails because x and y are not in the global scope,
; they don't have root bindings (maybe they do, but 
; they would be different from the ones inside the let)
; (println x y)
(println "expected error: \"Unable to resolve symbol\"")

; Destructuring (slicing for hashes?)
; "Destructuing an associative collection"
;   define a struct and an instance
(defstruct author :first-name :last-name)
(def chris (struct author "Chris" "Roeder"))
(println chris)
;   define a function that takes one field
(defn greet-author [ {fname :first-name} ]
	(println "hi " fname)
)
(println (greet-author chris))
(println "------")

; "Destructuring a sequential collection (slicing an array/list)
(let  [my-list [ 1 2 3 5 ] ]
	(let [[_ _ z _] my-list]
		(println z)
	)
)
; does not work for lists!!
;   (let [[_ _ z _] (1 2 3 4)]
;		(println z)
;	)

; add "as" to destructing....
; you can destructure a few of them, but get them all in 
; the parameter marked with ":as"
(let [ [x y :as coords] [1 2 3 4 5 6]]
	(println x y  (count coords))
	(println (last coords))
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Namespace, p.44
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; resolve is like var, but from a string, not a variable
(def foo 3)
(println (var foo))
(println (resolve 'foo))

; create a new namespace and go into it
; Q: is it parallel to "user" or subordinate?
(in-ns 'myapp)
(def bar 44)
; chokes because the println function is not in 
; this new namespace
;(println (var bar))
;;; use, or "import", clojure.core
(clojure.core/use 'clojure.core)
(println (var bar))

; import java classes...
; require clojure packages, but must still specify the namespace
; use the package, and you can omit the namespace
; use has a specific form where you import only what you need:
(use '[clojure.contrib.math :only (round)])
(println (round 1.2))


