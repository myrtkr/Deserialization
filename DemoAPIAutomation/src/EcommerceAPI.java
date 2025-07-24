import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.CreateOrderResponse;
import pojo.Login;
import pojo.LoginResponse;

import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class EcommerceAPI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Login API:
		
		RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		Login loginObjects = new Login();
		loginObjects.setUserEmail("mayurithakur7222@gmail.com");
		loginObjects.setUserPassword("Ruchi12345");
		
		RequestSpecification loginReq=given().log().all().spec(req).body(loginObjects);
		LoginResponse response= loginReq.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
		System.out.println(response.getToken());
		System.out.println(response.getUserId());
		String token = response.getToken();
		String userId = response.getUserId();
		
		//Add Product
		
		RequestSpecification createProduct = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token).build();
		RequestSpecification createProductRequest=given().spec(createProduct).param("productName", "Stitch")
		.param("productAddedBy", userId)
		.param("productCategory", "Fashion")
		.param("productSubCategory", "shirts")
		.param("productPrice",568943)
		.param("productDescription", "Sticker")
		.param("productFor", "Stitch Lover")
		.multiPart("productImage", new File("/Users/ruchi/eclipse-workspace/DemoAPIAutomation/src/utility/sticker.png"));
		
		String createProductResponse = createProductRequest.when().post("/api/ecom/product/add-product").then()
		.extract().asString();
		
		JsonPath js = new JsonPath(createProductResponse);
		String productId=js.get("productId");
		System.out.println(productId);
		
		//Getting Order Details: Order ID from Complex Json response
		
		OrderDetails orderDetails=new OrderDetails();
		orderDetails.setCountry("USA");
		orderDetails.setProductOrderedId(productId);
		
		ArrayList<OrderDetails> listOfOrderInfo =new ArrayList<OrderDetails>();
		listOfOrderInfo.add(orderDetails);  //passing the set values of elements(country and product Id)
		
		Orders orderObject = new Orders();
		orderObject.setOrders(listOfOrderInfo);
		
		RequestSpecification placeProductOrder =new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token)
				.setContentType(ContentType.JSON).build();
		//relaxedHTTPSValidation() to avoid invalid/expired  SSL or http certifications
		RequestSpecification placeFinalOrder=given().relaxedHTTPSValidation().log().all().spec(placeProductOrder).body(orderObject);
		
		String finalOrder=placeFinalOrder.when().post("/api/ecom/order/create-order")
		.then().log().all().extract().response().asString();
		JsonPath viewJson=new JsonPath(finalOrder);
		
		//View Order: get request...
		
		ArrayList<String> orders=viewJson.get("orders");
		
		CreateOrderResponse order= new CreateOrderResponse();
		order.setOrders(orders);
		System.out.println(orders.get(0));
		String oredersNum=orders.get(0);
		
		
		RequestSpecification ViewOrderRequest =new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.build();
		RequestSpecification viewPlacedOrder=given().spec(ViewOrderRequest).param("id", oredersNum);
		String view=viewPlacedOrder.when().get("/api/ecom/order/get-orders-details")
				.then().log().all().extract().response().asString();
		JsonPath viewResponse=new JsonPath(view);
		System.out.println(viewResponse);
		
		//Delete Product:
		
		RequestSpecification deleteProduct =new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token)
				.build();
		RequestSpecification deleteRequest= given().log().all().spec(deleteProduct).pathParam("productId", productId);
		String deletResponse=deleteRequest.when().log().all().delete("/api/ecom/product/delete-product/{productId}")
	    .then().extract().response().asString();
		
		JsonPath viewDeleteJson=new JsonPath(deletResponse);
		String message=viewDeleteJson.get("message");
		System.out.println(message);
		
		//Delete order:
		
		RequestSpecification deleteOrder =new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token)
				.build();
		RequestSpecification deleteOrderRequest =given().spec(deleteOrder).pathParam("id", oredersNum);
		String deletOrderResponse =deleteOrderRequest.when().log().all().delete("/api/ecom/order/delete-order/{id}").then().extract().response().asString();
		JsonPath orderDeleteJson=new JsonPath(deletOrderResponse);
		String messageOrderDelete=orderDeleteJson.get("message");
		System.out.println(messageOrderDelete);
		Assert.assertEquals(messageOrderDelete, "Orders Deleted Successfully");

	}

}
