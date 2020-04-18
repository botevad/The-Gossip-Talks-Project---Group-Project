package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.ChangePasswordDto;
import bg.codeacademy.spring.gossiptalks.dto.UserDto;
import bg.codeacademy.spring.gossiptalks.dto.UserRegistrationDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController
{
  private final UserServiceImpl   userService;
  private final GossipServiceImpl gossipService;

  @Autowired
  public UserController(UserServiceImpl userService, GossipServiceImpl gossipService, ModelMapper modelMapper)
  {
    this.userService = userService;
    this.gossipService = gossipService;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserDto>> showAllUsers(String name)
  {
    List<User> showUsers = userService.findAllUsers(name);
    List<UserDto> showUsersDto = new ArrayList<>();
    for (User user : showUsers) {
      UserDto userDto = new UserDto();
      userDto.setUsername(user.getUsername());
      userDto.setName(user.getName());
      userDto.setEmail(user.getEmail());
//      userDto.setFollowing(user.getFollowing());
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
//                                         @RequestParam(value = "following", required = false) Boolean following,
                                         @RequestParam(value = "password", required = true) String password)
  {
//    if(userService.isUserExist(userRegistrationDto.getUsername(), userRegistrationDto.getEmail()))){
//      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
//    }
    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setName(name);
//    user.setFollowing(following);
    user.setPassword(password);
    userService.saveUser(user);
    return new ResponseEntity<Void>(HttpStatus.CREATED);
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
  public ResponseEntity<Void> changeUserPassword(@RequestParam("password") String password,
                                                 @RequestParam("oldPassword") String oldPassword,
//                                                 @Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                 Principal principal)
  {
//    User user = (SecurityContextHolder.getContext().getAuthentication().getName());

//    if (!userService.checkIfValidOldPassword(principal, oldPassword)) {
//      throw new InvalidOldPasswordException();

    userService.changePassword(principal, password, oldPassword);
//    return new
//
//        GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    return ResponseEntity.ok().build();
  }

}
