package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController
{
  private final UserServiceImpl   userService;
  private final GossipServiceImpl gossipService;

  @Autowired
  public UserController(UserServiceImpl userService, GossipServiceImpl gossipService)
  {
    this.userService = userService;
    this.gossipService = gossipService;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(String name, Boolean following, Principal principal)
  {
    Optional<User> currentUser = userService.getUserByName(principal.getName());
    Optional<List<User>> allUsersWithName = userService.getAllUsers(name);
    List<User> showUsers;

    if (following == true) {
      showUsers = allUsersWithName
          .get()
          .stream()
          .filter(user -> currentUser.get().getFriendList().contains(user)).collect(Collectors.toList());
    }
    else {
      showUsers = allUsersWithName.get();
    }
    List<UserDto> showUsersDto = new ArrayList<>();
    for (User user : showUsers) {
      UserDto userDto = new UserDto();
      userDto.setUsername(user.getUsername());
      userDto.setName(user.getName());
      userDto.setEmail(user.getEmail());
      userDto.setGossips(gossipService.findAllGossipsByUser(user));
      showUsersDto.add(userDto);
    }
    showUsersDto.stream()
        .sorted(Comparator.comparing(UserDto::gossipsNumber))
        .collect(Collectors.toList());

    return ResponseEntity.ok(showUsersDto);
  }

  @PostMapping()
  public ResponseEntity<Void> createUser(@RequestParam(value = "email", required = true) String email,
                                         @RequestParam(value = "username", required = true) String username,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "following", required = false) Boolean following,
                                         @RequestParam(value = "password", required = true) String password,
                                         Principal principal)
  {

    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(password);
    user.setName(name);
    userService.saveUser(user);
    if (following == true) {
      List<User> currentUserList = userService.getFollowList(principal.getName());
      currentUserList.add(user);
      user.setFriendList(currentUserList);
    }
    return new ResponseEntity<Void>(HttpStatus.CREATED);
  }

  @GetMapping("/me")
  @ResponseBody
  ResponseEntity<UserDto> showCurrentUser(Principal principal)
  {
    User currentUser = userService.getUserByName(principal.getName()).get();
    UserDto currentUserDto = new UserDto();
    currentUserDto.setUsername(currentUser.getUsername());
    currentUserDto.setEmail(currentUser.getEmail());
    currentUserDto.setName(currentUser.getName());
    currentUserDto.setFollowing(true);
    return ResponseEntity.ok(currentUserDto);
  }

//  @PostMapping(value = "me")
//  public @ResponseBody
//  ResponseEntity<?> changePassword(@RequestParam(value = "password") String password,
//                                   @RequestParam(value = "oldPassword") String oldPassword,
//
//                                  )
//  {
//    if (!userName.equals(principal.getName()) || !userService.changePassword(principal, changePasswordDto.oldPassword, changePasswordDto.newPassword)) {
//      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong password or user name");
//    }
//    return ResponseEntity.ok().build();
//  }

  @PostMapping("/me")
  public ResponseEntity<Void> changeUserPassword(@RequestParam(value = "password", required = true) String password,
                                                 @RequestParam(value = "passwordConfirmation", required = true) String passwordConfirmation,
                                                 @RequestParam(value = "oldPassword", required = true) String oldPassword,
                                                 Principal principal)
  {
    User currentUser = userService.getUserByName(principal.getName()).get();
    if (currentUser.equals(oldPassword) && passwordConfirmation.equals(password)) {
      currentUser.setPassword(password);
    }
//    User user = (SecurityContextHolder.getContext().getAuthentication().getName());

//    if (!userService.checkIfValidOldPassword(principal, oldPassword)) {
//      throw new InvalidOldPasswordException();

//        GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{username}/follow")
  ResponseEntity<Void> followUser(@PathVariable("username") String username,
                                  @RequestParam(value = "follow", required = true) Boolean follow,
                                  Principal principal)
  {
    List<User> currentUserList = userService.getFollowList(principal.getName());
    if (follow == true) {
      currentUserList.add(userService.getUserByUsername(username).get());
    }
    else {
      currentUserList.remove(userService.getUserByUsername(username).get());
    }
    userService.getUserByName(principal.getName()).get().setFriendList(currentUserList);
    return ResponseEntity.ok().build();
  }

}
