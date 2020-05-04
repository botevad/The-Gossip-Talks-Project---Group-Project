package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
public class GossipControllerTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
  }

  @Test
  public void post_gossip_response_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType("multipart/form-data")
        .multiPart("text", "Gossip1")
        .when().post("/api/v1/gossips")
        .then().assertThat().statusCode(200).and().contentType(ContentType.JSON)
        .body("text", equalTo("Gossip1"));
  }

  @Test
  public void post_gossip_response_500()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType("multipart/form-data")
        .multiPart("text", "")
        .when().post("/api/v1/gossips")
        .then().assertThat().statusCode(500);
  }


}
