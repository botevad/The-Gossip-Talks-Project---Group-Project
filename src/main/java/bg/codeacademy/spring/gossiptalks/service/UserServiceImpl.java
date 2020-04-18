package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.dto.UserRegistrationDto;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public void addUser(Optional<User> user)
  {

  }

  @Override
  public List<User> findAllUsers(String name)
  {
    return userRepository.findAllByUsernameContaining(name);

  }

  public void saveUser(User user)
  {
    userRepository.save(user);
  }


//  public Boolean isUserExist(String username, String password)
//  {
//    if (userRepository.findB)
//    return userRepository.existsBy(user.getId());
//  }

  @Override
  public boolean changePassword(Principal principal, String oldPassword, String newPassword)
  {
    Optional<User> user = userRepository.findByUsername(principal.getName());
      if (new BCryptPasswordEncoder().matches(oldPassword, user.get().getPassword())) {
        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(user.get());
        return true;

    }
    return false;
  }
  @Override
  public Optional<User> getUser(String userName)
  {
    Optional<User> user = userRepository.findByUsername(userName);
    if (user.isPresent() && user.get().isEnabled()) {
      return user;
    }
    return Optional.empty();
  }
  @Override
  public void createUser(String username, String password)
  {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.saveAndFlush(user);
  }
  @Override
  public List<User> getUsers()
  {
    List<User> users = userRepository.findAll();
    Iterator<User> i = users.iterator();
    while (i.hasNext()) {
      User user = i.next();
      if (!user.isEnabled()) {
        i.remove();
      }
    }
    return users;
  }
  @Override
  public void register(UserRegistrationDto userRegistrationDto) {
    bCryptPasswordEncoder.encode(userRegistrationDto.getPassword());
  }
}
