import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourses;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

public class Desirialization {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String tokenResponse=given().formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.formParam("grant_type", "client_credentials")
		.formParam("scope", "trust").when().log().all().post("/oauthapi/oauth2/resourceOwner/token")
		.asString();
		
		JsonPath js=new JsonPath(tokenResponse);
		
		String accessToken=js.get("access_token");
		
		GetCourses getResponse=given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
        .when().get("/oauthapi/getCourseDetails").as(GetCourses.class);
		//System.out.println(getResponse);
		
		
		System.out.println(getResponse.getLinkedIn());
		System.out.println(getResponse.getInstructor());
		System.out.println(getResponse.getUrl());
		
		System.out.println(getResponse.getCourses().getApi().get(1).getCourseTitle());
		
		List<Api> getApi = getResponse.getCourses().getApi();
		
		for(int i=0; i<getApi.size(); i++) {
			
			if(getApi.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(getApi.get(i).getPrice());
				break;
			}
		}
		
		List<WebAutomation> getWebAuto = getResponse.getCourses().getWebAutomation();
		for(int i=0; i<getWebAuto.size();i++) {
			System.out.println(getWebAuto.get(i).getCourseTitle());
		}
		
		String[] courseNames= {"Selenium Webdriver Java","Cypress","Protractor"};
		ArrayList<String> coursesList= new ArrayList<String>();
		
		for(int j=0; j<getWebAuto.size(); j++) {
			coursesList.add(getWebAuto.get(j).getCourseTitle());
		}
		
		List<String> finalCourseList= Arrays.asList(courseNames);
		
		Assert.assertTrue(coursesList.equals(finalCourseList));
		
		System.out.println("Done");
		
		
	}

}
