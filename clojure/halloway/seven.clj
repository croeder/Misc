; Halloway, concurrency, p. 157


(def current-track (ref "Mars, the Bringer of War"))
(println (deref current-track))
(println @current-track)
current-track

; not quite this simple
;(def track1 (ref "Mars, the Bringer of War"))
;(def current-track (ref track1))
;(println (deref current-track))

(dosync 
	(ref-set current-track "Venus")
)
(println (deref current-track))

(def track3  "Zeus")
; careful not to write "do sync" its "dosync" one word
(dosync
	(ref-set current-track track3)
	;(ref-set current-track "Zeus")
)
(println (deref current-track))

(println "--- experimenting ---")
; let's try this again
(def track1  "Rock n Roll All Night")
(def current-track2 (ref track1 ))
(println (deref current-track2))
