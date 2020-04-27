package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping()
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(@RequestParam(value = "name") String name,
                                                    @RequestParam(value = "f") boolean f,
                                                    Principal principal)
  {
    Optional<User> currentUser = userService.getUserByName(principal.getName());
    Optional<List<User>> allUsersWithName = userService.getAllUsers(name);
    if (allUsersWithName.isPresent()) {
      List<User> showUsers;

      if (f == true) {
        showUsers = allUsersWithName
            .get()
            .stream()
            .filter(user -> currentUser.get().getFriendList().contains(user))
            .collect(Collectors.toList());
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
        userDto.setFollowing(true);
        showUsersDto.add(userDto);
      }
//    TODO:
//    showUsersDto.stream()
//        .sorted(Comparator.comparing(userService.getFollowList()))
//        .collect(Collectors.toList());

      return ResponseEntity.ok(showUsersDto);
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping(consumes = {"multipart/form-data"})
  //TODO: Multipart/form-data request.
  public ResponseEntity<Void> createUser(@RequestParam(value = "email", required = true) String email,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "password", required = true) String password,
                                         @RequestParam(value = "username", required = true) String username,
                                         @RequestParam(value = "following", required = false) Boolean following,
                                         Principal principal)
  {
    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(password);
    user.setName(name);
    userService.saveUser(user);
    if (following == true) {
      Optional<List<User>> currentUserFriends = userService.getFollowList(principal.getName());
      currentUserFriends.get().add(user);
      user.setFriendList(currentUserFriends.get());
    }
    return ResponseEntity.ok().build();
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

  @PostMapping("/me")
  public ResponseEntity<Void> changeUserPassword(@RequestParam(value = "password", required = true) String password,
                                                 @RequestParam(value = "passwordConfirmation", required = true) String passwordConfirmation,
                                                 @RequestParam(value = "oldPassword", required = true) String oldPassword,
                                                 Principal principal)
  {
//    userService.changePassword(userService.getUserByName(principal.getName()).get(), oldPassword, password);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/{username}/follow")
  ResponseEntity<Void> followUser(@PathVariable("username") String username,
                                  @RequestParam(value = "follow", required = true) Boolean follow,
                                  Principal principal)
  {
    Optional<List<User>> currentUserList = userService.getFollowList(principal.getName());
    if (follow == true) {
      currentUserList.get().add(userService.getUserByUsername(username).get());
    }
    else {
      currentUserList.get().remove(userService.getUserByUsername(username).get());
    }
    userService.getUserByName(principal.getName()).get().setFriendList(currentUserList.get());
    return ResponseEntity.ok().build();
  }

}
