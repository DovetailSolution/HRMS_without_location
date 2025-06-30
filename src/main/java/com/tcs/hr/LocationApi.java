package com.tcs.hr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


@WebServlet(name = "Location", urlPatterns = { "/Location" })
public class LocationApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public LocationApi() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String url="http://api.weatherapi.com/v1/current.json?key=74e97d5a23c94a09aaf121253240305&q=india&name&region&temp_c&temp_f";
		
		try 
		{
		HttpRequest res=HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
		HttpClient client=HttpClient.newBuilder().build();
		HttpResponse<String> resp=client.send(res, HttpResponse.BodyHandlers.ofString());
		
		System.out.println(resp.statusCode());
		System.out.println(resp.body());
	
		if(resp.statusCode()==200)
		{
			JSONObject obj=new JSONObject(resp.body());
			int a=obj.getInt("id");
			System.out.println("id:"+a);
		}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
