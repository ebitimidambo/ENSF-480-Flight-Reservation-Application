package control;

import java.util.List;

import entity.Route;

public class RouteController {
	private RouteDAO route;
	
	public RouteController() {
		route = new RouteDAO();
	}
	
	public String[] getOrigins() {
		return route.getOrigins();
	}
	
	public String[] getDestinations() {
		return route.getDestinations();
	}
	
	public List<Route> getAllRoutesForAdmin(){
		List<Route> routes = route.getAllRoutesForAdmin();
		
		return routes;
	}
}
