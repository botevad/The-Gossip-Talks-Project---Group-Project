package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository)
  {
    this.userRepository = userRepository;
  }

  @Override
  public void addUser(Optional<User> user)
  {

  }

  @Override
  public List<User> findAllUsers(String name)
  {
    return userRepository.findAllByUsernameContaining(name)
        .stream()
        .sorted(Comparator.comparing(User::userActivity).reversed())
        .collect(Collectors.toList());
  }

  public void saveUser(String password, String username, String name, String email)
  {
    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setName(name);
    user.setPassword(password);
    userRepository.save(user);
  }


//  public Boolean isUserExist(String username, String password)
//  {
//    if (userRepository.findB)
//    return userRepository.existsBy(user.getId());
//  }
}
