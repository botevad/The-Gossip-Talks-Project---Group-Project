package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  private final UserServiceImpl       userService;
  private final GossipServiceImpl     gossipService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserController(UserServiceImpl userService, GossipServiceImpl gossipService, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userService = userService;
    this.gossipService = gossipService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(@RequestParam(value = "name", required = false, defaultValue = "*") String name,
                                                    @RequestParam(value = "f", required = false, defaultValue = "false") Boolean f,
                                                    Principal principal)
  {
    List<UserDto> showUsersDto = new ArrayList<>();
    List<User> allUsers = userService.getAllUsers(name).get();
    List<User> friendList = userService.getFriendList(principal.getName());
    if (f) {
      allUsers = allUsers.stream()
          .filter(user -> friendList.contains(user))
          .collect(Collectors.toList());
    }
    for (User user : allUsers) {
      UserDto userDto = new UserDto();
      userDto.setUsername(user.getUsername());
      userDto.setName(user.getName());
      userDto.setEmail(user.getEmail());
      userDto.setFollowing(friendList.contains(user));
      showUsersDto.add(userDto);

    }
    return ResponseEntity.ok(showUsersDto);
  }


  @PostMapping(value = "", consumes = {"multipart/form-data"})
  public ResponseEntity<Void> createUser(@RequestParam(value = "email", required = true) String email,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "password", required = true) String password,
                                         @RequestParam(value = "username", required = true) String username,
                                         @RequestParam(value = "following", required = false, defaultValue = "false") Boolean following,
                                         Principal principal)
  {
    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    user.setName(name);
    userService.saveUser(user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  @ResponseBody
  ResponseEntity<UserDto> showCurrentUser(Principal principal)
  {
    User currentUser = userService.getUserByUsername(principal.getName()).get();
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

    userService.changePassword(userService.getUserByUsername(principal.getName()).get(), oldPassword, password);

    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/{username}/follow", consumes = "multipart/form-data")
  ResponseEntity<User> followUser(@PathVariable("username") String username,
                                  @RequestParam(value = "follow", required = true) Boolean follow,
                                  Principal principal)
  {
    List<User> currentUserList = userService.getFriendList(principal.getName());
    User userToFollow = userService.getUserByUsername(username).get();
    if (follow) {
      if (!currentUserList.contains(userToFollow)) {
        currentUserList.add(userToFollow);
      }
      else {
        return ResponseEntity.badRequest().build();
      }
    }
    if (!follow) {
      if (currentUserList.contains(userToFollow)) {
        currentUserList.remove(userToFollow);
      }
      else {
        return ResponseEntity.badRequest().build();
      }
    }
    userService.saveUserFriendList(principal.getName(), currentUserList);
    return ResponseEntity.ok(userToFollow);
  }

  //from Rado
  @GetMapping("/{username}/gossips")
  public ResponseEntity<List<GossipDto>> getGossipsOfUser(@PathVariable("username") String username)
  {
    Optional<User> userCheck = userService.getUserByUsername(username);
    if (!userCheck.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    else {
      User user = userCheck.get();
      List<Gossips> userGossips = gossipService.findAllGossipsByUser(user);
      List<GossipDto> gossipsToShow = new ArrayList<>();
      if (userGossips.isEmpty()) {
        return ResponseEntity.ok(gossipsToShow);
      }
      else {
        for (Gossips g : userGossips
        ) {
          GossipDto gDto = new GossipDto();
          gDto.setUsername(g.getUser().getUsername());
          gDto.setDate(g.getDate());
          gDto.setGossip(g.getGossip());
          gossipsToShow.add(gDto);
        }
      }
      gossipsToShow.sort(Comparator.comparing(GossipDto::getDate));
      return ResponseEntity.ok(gossipsToShow);
    }
  }

}
