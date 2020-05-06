package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.enums.Role;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

@ActiveProfiles("dev2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
public class GossipControllerTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;


  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeClass
  public void create_users_for_usage()
  {
    User admin2 = new User();
    admin2.setId(99);
    admin2.setUsername("admin2");
    admin2.setName("Administrator");
    admin2.setEmail("admin2@email.com");
    admin2.setPassword(bCryptPasswordEncoder.encode("1234"));
    admin2.setRole(Role.ADMIN);

    userRepository.save(admin2);

    User user = new User();
    user.setId(100);
    user.setUsername("user");
    user.setName("User");
    user.setEmail("user@mail.bg");
    user.setPassword(bCryptPasswordEncoder.encode("12345"));
    user.setRole(Role.USER);

    userRepository.save(user);
  }

  @Test
  public void post_gossip_response_200()
  {
    RestAssured.given()
        .auth().basic("user", "12345")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("text", "Gossip1")
        .when()
        .post("/api/v1/gossips")
        .then().assertThat().statusCode(200)
        .and().
        contentType(ContentType.JSON)
        .body("text", equalTo("Gossip1")
            , "username", equalTo("user"));
  }

  @Test
  public void post_gossip_response_200_1()
  {
    RestAssured.given()
        .auth().basic("admin2", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("text", "This is a gossip")
        .when()
        .post("/api/v1/gossips")
        .then().assertThat().statusCode(200)
        .and().
        contentType(ContentType.JSON);
  }

  @Test
  public void post_gossip_response_500()
  {
    RestAssured.given()
        .auth().basic("admin2", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("text", "")
        .when()
        .post("/api/v1/gossips")
        .then()
        .assertThat().statusCode(500);
  }

  @Test
  public void get_gossips_200()
  {
    RestAssured.given()
        .auth().basic("user", "12345")
        .when()
        .get("/api/v1/gossips")
        .then().assertThat()
        .statusCode(200).and().contentType(ContentType.JSON);
  }


  @Test
  public void get_gossips_200_1()
  {
    RestAssured.given()
        .auth().basic("admin2", "1234")
        .when()
        .get("/api/v1/gossips")
        .then().assertThat()
        .statusCode(200).and().contentType(ContentType.JSON).body("content", is(empty())
    , "numberOfElemets", is(20), "totalElements", is(0));
  }

  @Test
  public void get_gossips_param_200()
  {
    RestAssured.given()
        .auth().basic("user", "12345")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("pageNo", "1000")
        .when()
        .get("/api/v1/gossips")
        .then().assertThat()
        .statusCode(404);
  }
}
