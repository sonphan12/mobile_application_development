package com.part2.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.lang.Math;

@Path("/app")
public class Part2Service
{
	@GET
	@Path("/{message}")
	public Response getMsg(@PathParam("message") String msg)
	{
		String []latlong = msg.split(",");
		double lat1 = Double.parseDouble(latlong[0].split("@")[0]);
		double lat2 = Double.parseDouble(latlong[1].split("@")[0]);
		double lon1 = Double.parseDouble(latlong[0].split("@")[1]);
		double lon2 = Double.parseDouble(latlong[1].split("@")[1]);
		double R = 6371;
		double dLat = deg2rad(lat2 - lat1);  // deg2rad below
		double dLon = deg2rad(lon2 - lon1); 
		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
			    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
			    Math.sin(dLon/2) * Math.sin(dLon/2)
		    ; 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = Math.round(R * c); // Distance in km
		
		String output = "{\n" + "\t\"distance\":" + d + "\n" + "}"; 

		//Simply return the parameter passed as message
		return Response.status(200).entity(output).build();

	}
	
	private double deg2rad(double deg) {
		return deg * (Math.PI/180);
	}
}
