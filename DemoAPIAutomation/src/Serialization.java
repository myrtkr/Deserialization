import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.GooleMapDesirialize;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;

public class Serialization {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		GooleMapDesirialize addPlace = new GooleMapDesirialize();
		addPlace.setAccuracy(50);
		addPlace.setLanguage("French-IN");
		addPlace.setAddress("29, side layout, cohen 09");
		addPlace.setName("Frontline house");
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setWebsite("http://google.com");
		
		Location localList= new Location();
		localList.setLatitude(-38.383494);
		localList.setLongitude(33.427362);
		
		
		addPlace.setLocation(localList);
		
		ArrayList<String> typesList=new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop");
		addPlace.setTypes(typesList);
		
		
		RequestSpecification requestSpecifications = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification responseSpecifications=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		//ResponseSpecification response = new ResponseSpecBuilder().
		
		RequestSpecification request = given().spec(requestSpecifications).body(addPlace);
		
		Response response = request.when().log().all().post("/maps/api/place/add/json").then()
		.spec(responseSpecifications).extract().response();
		
		String responseGoogleMap = response.asString();
		System.out.println(responseGoogleMap);

	}

}
