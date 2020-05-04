package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


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
  public void post_gossip_response() // test is OK
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType("multipart/form-data")
        .multiPart("text", "Gossip1") // is this equivalent to param?
        .when().post("/api/v1/gossips")
        .then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
  }


}
