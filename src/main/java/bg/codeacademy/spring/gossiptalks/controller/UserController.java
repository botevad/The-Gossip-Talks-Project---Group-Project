package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.PageDto;
import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    return ResponseEntity.ok()
        .header("Custom header", "The list of documents that can be loaded directly from swagger-ui, via \"urls\" configuration parameter.")
        .body(showUsersDto);
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
    if (userService.getUserByUsername(username) == null) {
      userService.saveUser(user);
      return ResponseEntity.ok().header("Custom header", "Successful operation").build();
    }
    else {
      return ResponseEntity.badRequest().header("Custom header", "Failed").build();
    }
  }

  @GetMapping("/me")
  @ResponseBody
  ResponseEntity<UserDto> showCurrentUser(Principal principal)
  {
    User currentUser = userService.getUserByUsername(principal.getName());
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

    userService.changePassword(userService.getUserByUsername(principal.getName()), oldPassword, password);

    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/{username}/follow", consumes = "multipart/form-data")
  ResponseEntity<UserDto> followUser(@PathVariable("username") String username,
                                     @RequestParam(value = "follow", required = true) Boolean follow,
                                     Principal principal)
  {
    List<User> currentUserList = userService.getFriendList(principal.getName());
    User userToFollow = userService.getUserByUsername(username);
    UserDto userDto = new UserDto();
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
    User userCheck = userService.getUserByUsername(username);
    List<Gossips> userGossips = gossipService.findAllGossipsByUser(userCheck);
    List<GossipDto> gossipsToShow = new ArrayList<>();
    for (Gossips g : userGossips
    ) {
      GossipDto gDto = new GossipDto();
      gDto.setId(Integer.toString(g.getId(), 32));
      gDto.setUsername(g.getUser().getUsername());
      gDto.setDatetime(g.getDate());
      gDto.setText(g.getGossip());
      gossipsToShow.add(gDto);
    }
    PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
    Page<User> page = new PageImpl(gossipsToShow, pageRequest, gossipsToShow.size());
    PageDto pageDto = new PageDto();
    pageDto.setNumberOfElemets(page.getSize());
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setContent(page.getContent());
    return ResponseEntity.ok(pageDto);
  }

}
