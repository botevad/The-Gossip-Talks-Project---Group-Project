package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.PageDto;
import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
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
  public ResponseEntity<Set<UserDto>> showAllUsers(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                   @RequestParam(value = "f", required = false, defaultValue = "false") Boolean f,
                                                   Principal principal)
  {
    Set<UserDto> showUsersDto = new LinkedHashSet<>();
    Set<User> allUsers = userService.getAllUsers(name);
    Set<User> friendList = userService.getFriendList(principal.getName());
    if (f) {
      allUsers = allUsers.stream().filter(user -> friendList.contains(user)).collect(Collectors.toSet());
    }
    for (User user : allUsers) {
      if (user.getUsername().equals(principal.getName())) {
        continue;
      }
      else {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setFollowing(friendList.contains(user));
        showUsersDto.add(userDto);
      }
    }
    return ResponseEntity.ok(showUsersDto);
  }

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<String> createUser(@RequestParam(value = "email", required = true) String email,
                                           @RequestParam(value = "name", required = false, defaultValue = "") String name,
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
    if (userService.getUserByUsername(username).isPresent()) {
      return ResponseEntity.badRequest().build();
    }
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
  public ResponseEntity<UserDto> changeUserPassword(@RequestParam(value = "password", required = true) String password,
                                                    @RequestParam(value = "passwordConfirmation", required = true) String passwordConfirmation,
                                                    @RequestParam(value = "oldPassword", required = true) String oldPassword,
                                                    Principal principal)
  {

    if (userService.changePassword(userService.getUserByUsername(principal.getName()).get(), oldPassword, password)) {
      User currentUser = userService.getUserByUsername(principal.getName()).get();
      UserDto userDto = new UserDto();
      userDto.setEmail(currentUser.getEmail());
      userDto.setUsername(currentUser.getUsername());
      userDto.setName(currentUser.getName());
      userDto.setFollowing(true);
      return ResponseEntity.ok(userDto);
    }
    return ResponseEntity.badRequest().build();
  }

  @PostMapping(value = "/{username}/follow", consumes = "multipart/form-data")
  ResponseEntity<UserDto> followUser(@PathVariable("username") String username,
                                     @RequestParam(value = "follow", required = true) Boolean follow,
                                     Principal principal)
  {
    Set<User> currentUserList = userService.getFriendList(principal.getName());
    User userToFollow = userService.getUserByUsername(username).get();
    if (follow && !currentUserList.contains(userToFollow)) {
      currentUserList.add(userToFollow);
    }
    else if (!follow && currentUserList.contains(userToFollow)) {
      currentUserList.remove(userToFollow);
    }
    else {
      return ResponseEntity.badRequest().build();
    }

    userService.saveUserFriendList(principal.getName(), currentUserList);

    UserDto userDto = new UserDto();
    userDto.setEmail(userToFollow.getEmail());
    userDto.setUsername(userToFollow.getUsername());
    userDto.setName(userToFollow.getName());
    userDto.setFollowing(currentUserList.contains(userToFollow));
    return ResponseEntity.ok(userDto);
  }

  @GetMapping("/{username}/gossips")
  public ResponseEntity<PageDto> getGossipsOfUser(@PathVariable("username") @Valid String username,
                                                  @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                  Principal principal)
  {
    User userCheck = userService.getUserByUsername(username).get();
    PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
    Page<Gossip> userGossips = gossipService.findAllGossipsByUser(userCheck, pageRequest);
    if (pageNo > userGossips.getTotalPages()) {
      return ResponseEntity.notFound().build();
    }
    else {
      List<GossipDto> gossipsToShow = new ArrayList<>();
      for (Gossip gossip : userGossips) {
        GossipDto gDto = new GossipDto();
        gDto.setId(Integer.toString(gossip.getId(), 32));
        gDto.setUsername(gossip.getUser().getUsername());
        gDto.setDatetime(gossip.getDatetime());
        gDto.setText(gossip.getText());
        gossipsToShow.add(gDto);
      }

      PageDto pageDto = new PageDto();
      pageDto.setNumberOfElemets(pageSize);
      pageDto.setTotalElements(userGossips.getTotalElements());
      pageDto.setContent(gossipsToShow);

      return ResponseEntity.ok(pageDto);
    }
  }
}
