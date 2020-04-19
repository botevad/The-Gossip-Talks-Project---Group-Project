//package bg.codeacademy.spring.gossiptalks.service;
//
//
//import bg.codeacademy.spring.gossiptalks.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService
//{
//
//  private UserServiceImpl userService;
//
//  public UserDetailsServiceImpl(UserServiceImpl userService)
//  {
//    this.userService = userService;
//  }
//
////  @Override
////  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
////  {
////    List<User> users = userService.getUsers();
////    if (users.isEmpty()) {
////      // first start, create default admin user
////      userService.createUser("admin", "123456");
////    }
////
////    Optional<User> optionalUser = userService.getUser(userName);
////    if (!optionalUser.isPresent()) {
////      throw new UsernameNotFoundException("User not found.");
////    }
////    return  null;
////  }
//
//}