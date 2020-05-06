package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.enums.Role;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import bg.codeacademy.spring.gossiptalks.service.UserService;
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

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
public class UserControllerTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  UserService userService;

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeClass
  public void create_users_for_usage()
  {
    User admin = new User();
    admin.setUsername("admin");
    admin.setName("Administrator");
    admin.setEmail("admin@email.com");
    admin.setPassword(bCryptPasswordEncoder.encode("1234"));
    admin.setRole(Role.ADMIN);

    User user = new User();
    user.setUsername("user");
    user.setName("User");
    user.setEmail("user@mail.bg");
    user.setPassword(bCryptPasswordEncoder.encode("12345"));
    user.setRole(Role.USER);

    User user1 = new User();
    user1.setUsername("user1");
    user1.setName("User1");
    user1.setEmail("user1@mail.bg");
    user1.setPassword(bCryptPasswordEncoder.encode("pa$$word"));
    user1.setRole(Role.USER);

    userRepository.save(admin);
    userRepository.save(user);
    userRepository.save(user1);
  }

  @Test
  public void show_current_user_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .when()
        .get("/api/v1/users/me")
        .then().assertThat().statusCode(200)
        .and()
        .contentType(ContentType.JSON)
        .body("username", equalTo("admin")
        , "name", equalTo("Administrator"),
        "email", equalTo("admin@email.com"));
  }

  @Test
  public void create_user_200()
  {
    RestAssured.given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
      .multiPart("email", "user2@mail.bg")
      .multiPart("username", "user2")
      .multiPart("password", "p@assw0rd")
      .multiPart("passwordConfirmation", "p@assw0rd")
      .when()
      .post("/api/v1/users")
      .then()
      .statusCode(200);
  }

  @Test
  public void create_user_500()
  {
    RestAssured.given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("email", "user2@mail.bg")
        .multiPart("username", "")
        .multiPart("password", "p@assw0rd")
        .multiPart("passwordConfirmation", "p@assw0rd")
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(500);
  }

  @Test
  public void create_user_500_1()
  {
    RestAssured.given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("email", "")
        .multiPart("username", "theUser")
        .multiPart("password", "p@assw0rd")
        .multiPart("passwordConfirmation", "p@assw0rd")
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(500);
  }

  @Test
  public void create_user_400()
  {
    RestAssured.given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("email", "user2@mail.bg")
        .multiPart("username", "user2")
        .multiPart("password", "p@assw0rd")
        .multiPart("passwordConfirmation", "")
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(400);
  }

  @Test
  public void create_user_400_1()
  {
    RestAssured.given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("email", "user2@mail.bg")
        .multiPart("username", "user2")
        .multiPart("password", "")
        .multiPart("passwordConfirmation", "p@assw0rd")
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(400);
  }

  @Test
  public void change_password_200()
  {
  RestAssured.given().auth().basic("user", "12345")
      .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
      .multiPart("password", "p@ss")
      .multiPart("passwordConfirmation", "p@ss")
      .multiPart("oldPassword", "12345")
      .when()
      .post("/api/v1/users/me")
      .then()
      .statusCode(200);
  }

  @Test
  public void change_password_400_1()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("password", "p@ssword")
        .multiPart("passwordConfirmation", "p@ssword")
        .multiPart("oldPassword", "1000")
        .when()
        .post("/api/v1/users/me")
        .then()
        .statusCode(400);
  }

  @Test
  public void change_password_400_2()
  {
    RestAssured.given()
        .auth().basic("user1", "pa$$word")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("password", "p@ssword")
        .multiPart("passwordConfirmation", "p@sswor")
        .multiPart("oldPassword", "pa$$word")
        .when()
        .post("/api/v1/users/me")
        .then()
        .statusCode(400);
  }

  @Test
  public void show_all_users_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .when()
        .get("/api/v1/users")
        .then()
        .statusCode(200);
  }

  @Test
  public void follow_user_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
      .pathParam("username", "user")
      .multiPart("follow", "true")
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .statusCode(200);
  }

  @Test
  public void follow_user_200_1()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .pathParam("username", "user1")
        .multiPart("follow", "true")
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .statusCode(200);
  }

  @Test
  public void follow_user_400()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .pathParam("username", "user")
        .multiPart("follow", "true")
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .statusCode(400);
  }

  @Test
  public void unfollow_user_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .pathParam("username", "user")
        .multiPart("follow", "false")
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .statusCode(200);
  }

  @Test
  public void unfollow_user_400()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .pathParam("username", "user")
        .multiPart("follow", "false")
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .statusCode(400);
  }

  @Test
  public void show_all_parameterized_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("name", "user1")
        .multiPart("f", "true")
        .when()
        .get("/api/v1/users")
        .then().assertThat().statusCode(200)
        .and()
        .contentType(ContentType.JSON).body("username", contains("user1")
        , "email", contains("user1@mail.bg")
    , "name", contains("User1"));
  }

  @Test
  public void show_all_parameterized_200_1()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("name", "user")
        .when()
        .get("/api/v1/users")
        .then().assertThat().statusCode(200);
  }

  @Test
  public void show_all_parameterized_200_2()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart("f", "true")
        .when()
        .get("/api/v1/users")
        .then().assertThat().statusCode(200);
  }

  @Test
  public void get_gossips_of_user_200()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .pathParam("username", "user")
        .when()
        .get("/api/v1/users/{username}/gossips")
        .then().assertThat()
        .statusCode(200).and().contentType(ContentType.JSON);
  }

  @Test
  public void get_gossips_of_user_200_param()
  {
    RestAssured.given()
        .auth().basic("admin", "1234")
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .pathParam("username", "user")
        .multiPart("pageNo", "1000")
        .when()
        .get("/api/v1/users/{username}/gossips")
        .then().assertThat()
        .statusCode(404);
  }


}
