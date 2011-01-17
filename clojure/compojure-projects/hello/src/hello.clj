(ns hello
  	(:use compojure.core 
		  ring.adapter.jetty))
	
(defn test-page [name]
	(str "<b>Hello " name "</b>"))

(defn test-page3 [name]
	(str "<b>Hello " name "</b>"))


(defroutes main-routes
  (GET "/" []
    "<h1>Hello World</h1>")
  (GET "/test/:name" req
    (test-page (str req)) )
  (GET "/test2/:name" req
    (test-page (:params req)))
  (GET "/test3/:params" req
    (test-page3 (:params req))))
  (ANY "*" []
    {:status 404, :body "<h1>Page not found</h1>"}))


;(defn start []
	;(run-jetty #'main-routes {:port 8080 :join? false}))
	(run-jetty main-routes {:port 8080 :join? false})
;)
