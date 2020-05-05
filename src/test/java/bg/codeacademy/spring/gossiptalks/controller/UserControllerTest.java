package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void show_current_user_200()
  {
    RestAssured.given().auth().basic("admin", "1234")
        .when().get("/api/v1/users/me").then().assertThat().statusCode(200).and()
        .contentType(ContentType.JSON).body("username", equalTo("admin")
        , "name", equalTo("Administrator"),
        "email", equalTo("admin@email.com"));
  }

  @Test
  public void show_all_users_200()
  {
    RestAssured.given().auth().basic("admin", "1234")
        .when().get("/api/v1/users").then().assertThat().statusCode(200)
        .and().contentType(ContentType.JSON);
  }


//  @Test
//  public void create_user_200()
//  {
//    HashMap<String, String> userParams = new HashMap<>();
//    userParams.put("email", "petar25@abv.bg");
//    userParams.put("username", "petar");
//
//
//
//    User userToDelete = userRepository.findByUsername("petar").get();
//    userRepository.delete(userToDelete);
//  }

//  @Test
//  public void follow_user_200()
//  {
//    User userToFollow = new User();
//    userToFollow.setPassword(bCryptPasswordEncoder.encode("1234"));
//    userToFollow.setUsername("kalata");
//    userToFollow.setRole(Role.USER);
//
//    RestAssured.given().auth().basic("admin", "1234").and()
//        .contentType("multipart/form-data").multiPart("follow", true)
//
//  }

}


