(ns compojuretest
	(use 'compojure.core 'ring.adapter.jetty))
(defroutes main-routes
	(GET"/" []
		"<h1>Hello</h1>")
	(ANY "*" []
		{:status 404, :body "<h1>Page not found</h1>"}))

(run-jetty main-routes {:port 8080})

