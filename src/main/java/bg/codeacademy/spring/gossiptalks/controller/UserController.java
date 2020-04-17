package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.dto.UserRegistrationDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", headers = "content-type=multipart/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public class UserController
{
  private final UserServiceImpl userService;
  private final ModelMapper     modelMapper;

  @Autowired
  public UserController(UserServiceImpl userService, ModelMapper modelMapper)
  {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @GetMapping()
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(String name){
    List<User> showUsers = userService.findAllUsers(name);
    List<UserDto> showUsersDto = new ArrayList<>();
    for (User user: showUsers) {
      showUsersDto.add(modelMapper.map(user, UserDto.class));
    }
    return ResponseEntity.ok(showUsersDto);
  }

  @PostMapping()
  public ResponseEntity<Void> createUser(@RequestBody UserRegistrationDto userRegistrationDto){
//    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    ObjectMapper objectMapper = new ObjectMapper();
//    httpHeaders.add(MediaType.MULTIPART_FORM_DATA, );
    if(userService.isUserExist(modelMapper.map(userRegistrationDto, User.class))){
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    userService.saveUser(objectMapper.readValue(userRegistrationDto,User.class));
//    userRegistrationDto.getEmail(),userRegistrationDto.getUsername(),userRegistrationDto.getName(),userRegistrationDto.getPassword()
    return new ResponseEntity<Void>(HttpStatus.CREATED);
  }
//  modelMapper.map(userRegistrationDto, User.class)

}
