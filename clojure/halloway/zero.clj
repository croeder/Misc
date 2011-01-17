; list
(def list-a '("foo", "bar", "baz"))
(def list-b '(1, 12, 123))

; map
(def map-a {:name "Chris", :ssn, "123-45-6789"})
(def map-b {"name" "Chris" "ssn" "123-45-6789"})

; vector
(def vector-a [4 5 6])

; set
(def set-a #{ 1,2,3,4,5,6,3 })

(println list-a)
(println list-b)
(println map-a)
(println vector-a)
(println set-a)

; if
(def a 4)
(println a)
(if (= a 3) 
	(println "yupper")
	(println "wtf")
)

; cond
(def a `(44, 55 66))
(cond 
	(= a 3) (println "yupper")
	(= a 4) (println "wtf")
	(complement (and (= a 4) (= a 3) ))  (println "other")
	(= a 44) (println "it gets the first one") ; not
)

; maps
; a keyword is not equivalent to a string
(println "--- maps ---")
(println (map-a "name"))
(println (map-a :name))
(println "--- maps 2 ---")
(println (map-b "name"))
; default
(println (get map-a :salary "default"))
(println (map-a :salary))
(println (map-a :name))

; Q: if you want equivalence, you must use the colon?
; A: NO
(println "--- maps 3 ---")
(println (map-a ":name"))


; structs, a typedef
(println "--- structs ---")
(defstruct person :first :last :ssn :dob :addr)
(defstruct address :number :street :city :state :zip)
(def  chris-addr (struct address 655 "Trenton" "Denver" "CO" 80230)  )
(println chris-addr)
(println (struct person "Chris" "Roeder" "123-45-6789" "1/1/01" chris-addr ))


; functions with multi args
(println "--- fns ---")
(defn date [ p1 p2 & chaperones ] 
	(println p1 "and" p2
		"went out with" (count chaperones) "chaperones.")
	(class chaperones)
	(println "the first was" (first chaperones))
	; !! last is NOT the same as cdr!!!!!
	(println "the last(rest?) was" (last chaperones))
	; --> rest gets you the rest
	(println "the last(rest?) was" (rest chaperones))
)
(date "chris" "mandy" "scott" "edna" "iian")
(date "chris" "mandy" )
; runtime error when not enough arguments for the non-var part
(date "chris" )


(println "--- done ---")
