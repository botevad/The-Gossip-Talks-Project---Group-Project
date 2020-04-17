package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.dto.UserRegistrationDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController
{
  private final UserServiceImpl userService;
  private final GossipServiceImpl gossipService;
  private final ModelMapper     modelMapper;

  @Autowired
  public UserController(UserServiceImpl userService, GossipServiceImpl gossipService, ModelMapper modelMapper)
  {
    this.userService = userService;
    this.gossipService = gossipService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(String name){
    List<User> showUsers = userService.findAllUsers(name);
//        .stream()
//        .sorted(Comparator.comparing(User::userActivity).reversed())
//        .collect(Collectors.toList());
    List<UserDto> showUsersDto = new ArrayList<>();
    for (User user : showUsers) {
      UserDto userDto = new UserDto();
      userDto.setUsername(user.getUsername());
      userDto.setName(user.getName());
      userDto.setEmail(user.getEmail());
      userDto.setFollowing(user.getFollowing());
      userDto.setGossips(gossipService.findAllGossipsByUser(user));
      showUsersDto.add(userDto);
    }

    return ResponseEntity.ok(showUsersDto);
  }

  @PostMapping()
  public ResponseEntity<Void> createUser(@RequestBody UserRegistrationDto userRegistrationDto){
//    if(userService.isUserExist(userRegistrationDto.getUsername(), userRegistrationDto.getEmail()))){
//      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
//    }

    userService.saveUser(userRegistrationDto.getEmail(),
        userRegistrationDto.getUsername(),
        userRegistrationDto.getName(),
        userRegistrationDto.getPassword());
    return new ResponseEntity<Void>(HttpStatus.CREATED);
  }

}
