package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository  userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<List<User>> getAllUsers(String name)
  {
    if (userRepository != null) {
      return userRepository.findAllByNameContaining(name);
    }
    return Optional.empty();
  }

  @Override
  public Boolean changePassword(User user, String oldPassword, String password)
  {
    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(passwordEncoder.encode(password));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @Override
  public Optional<User> getUserByName(String name)
  {
    Optional<User> user = userRepository.findByName(name);
    if (user.isPresent()) {
      return user;
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> getUserByUsername(String username)
  {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      return user;
    }
    return Optional.empty();
  }

  @Override
  public Optional<List<User>> getFollowList(String name)
  {
    if (!getFollowList(name).get().isEmpty()) {
      return Optional.of(userRepository.findByName(name).get().getFriendList());
    }
    else {
      return Optional.empty();
    }
  }

  public void saveUser(User user)
  {
    userRepository.save(user);
  }
}
