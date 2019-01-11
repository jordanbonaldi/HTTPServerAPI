## Java Http and Https Server API

 - ### Creating Server
	
	- #### HTTP Server
		 
			 HTTPServerAPI api =  new  HTTPServerAPI(port, threads);
	
	- #### HTTPS Server
			 HTTPServerAPI api = new HTTPServerAPI(port, threads, HttpTypes.HTTPS, "myPassPhrase",  "fileName");
			 
		Don't put any extension to fileName.

 - ### Creating Routes
	- #### With URL Parameters

		    @Route(name = "/test", params = {"user", "id", "content"})
		    public class TestRoute extends RoutingProperties {
			    @Override
			    public JSONObject routeAction(HttpExchange t) 
			    {
				    JSONObject obj = new JSONObject().put("success", true);
				    
				    String user = this.params.get("user");
					
					obj.put("user", user);
	    
				    return obj;
			    }
			 } 

		This route is accessible via : ip:port/test/{user}/{id}/{content}

	- #### Without URL Parameters
		
			@Route(name = "/test")
		    public class TestRoute extends RoutingProperties {
			    @Override
			    public JSONObject routeAction(HttpExchange t) 
			    {
				    JSONObject obj = new JSONObject().put("success", true);
	    
				    return obj;
			    }
			 } 
		This route is accessible via : ip:port/test

 - ### Adding Routes
	 - #### Adding with Class
		  `api.addRoute(TestRoute.class);`
		  
	 - #### Adding with Package 
		  `api.addAllRoutesInPath("net.neferett.httpserver.api.Examples", "TestRoute"...);`
		  
 - ### Starting and Stop server
	 - #### Start
		 `api.start()`
		 
	 - #### Stop
		 `api.stop()`
