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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    List<User> allUsers = userService.getAllUsers(name);
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
    showUsersDto.remove(userService.getUserByUsername(principal.getName()));
    return ResponseEntity.ok(showUsersDto);
  }


  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<String> createUser(@RequestParam(value = "email", required = true) String email,
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
    if (userService.getUserByUsername(username).isPresent()) {
      return ResponseEntity.badRequest().body("Failed");
    }
    userService.saveUser(user);
    return ResponseEntity.ok().body("Successful operation");
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
  public ResponseEntity<String> changeUserPassword(@RequestParam(value = "password", required = true) String password,
                                                   @RequestParam(value = "passwordConfirmation", required = true) String passwordConfirmation,
                                                   @RequestParam(value = "oldPassword", required = true) String oldPassword,
                                                   Principal principal)
  {

    if (userService.changePassword(userService.getUserByUsername(principal.getName()).get(), oldPassword, password)) {
      return ResponseEntity.ok().body("Successful operation");
    }
    return ResponseEntity.badRequest().body("Failed");
  }

  @PostMapping(value = "/{username}/follow", consumes = "multipart/form-data")
  ResponseEntity<String> followUser(@PathVariable("username") String username,
                                    @RequestParam(value = "follow", required = true) Boolean follow,
                                    Principal principal)
  {
    List<User> currentUserList = userService.getFriendList(principal.getName());
    User userToFollow = userService.getUserByUsername(username).get();
    UserDto userDto = new UserDto();
    if (follow && !currentUserList.contains(userToFollow)) {
      currentUserList.add(userToFollow);
    }
    else if (!follow && currentUserList.contains(userToFollow)) {
      currentUserList.remove(userToFollow);
    }
    else {
      return ResponseEntity.badRequest().body("Failed - already subscriber or not subscribed.");
    }

    userService.saveUserFriendList(principal.getName(), currentUserList);

//    userDto.setEmail(userToFollow.getEmail());
//    userDto.setUsername(userToFollow.getUsername());
//    userDto.setName(userToFollow.getName());
//    userDto.setFollowing(currentUserList.contains(userToFollow));
    return ResponseEntity.ok("Successful operation");
  }

  @GetMapping("/{username}/gossips")
  public ResponseEntity<PageDto> getGossipsOfUser(@PathVariable("username") @Valid String username,
                                                  @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                  Principal principal)
  {
    User userCheck = userService.getUserByUsername(username).get();
    PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("datetime"));
    Page<Gossip> userGossips = gossipService.findAllGossipsByUser(userCheck, pageRequest);
    List<GossipDto> gossipsToShow = new ArrayList<>();
    for (Gossip gossip : userGossips) {
      GossipDto gDto = new GossipDto();
      gDto.setId(Integer.toString(gossip.getId(), 32));
      gDto.setUsername(gossip.getUser().getUsername());
      gDto.setDatetime(gossip.getDatetime());
      gDto.setText(gossip.getGossip());
      gossipsToShow.add(gDto);
    }

    Page<User> page = new PageImpl(gossipsToShow, pageRequest, gossipsToShow.size());
    PageDto pageDto = new PageDto();
    pageDto.setNumberOfElemets(page.getSize());
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setContent(page.getContent());
    return ResponseEntity.ok(pageDto);
  }
}
