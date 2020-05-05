package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
public class UserControllerTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
  }

  @Test
  public void show_current_user_200()
  {
    RestAssured.given().auth().basic("admin", "1234")
        .when().get("/api/v1/users/me").then().assertThat().statusCode(200).and()
        .contentType(ContentType.JSON).body("username", equalTo("admin")
    ,"name", equalTo("Administrator"),
        "email", equalTo("admin@email.com"));
  }


}
