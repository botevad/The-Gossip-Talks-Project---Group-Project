package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
      return userRepository.findAllByUsernameContaining(name);
    }
    return Optional.empty();
  }

  @Override
  public Boolean changePassword(Principal principal, String oldPassword, String newPassword)
  {
    Optional<User> currentUser = userRepository.findByName(principal.getName());

    if (passwordEncoder.matches(oldPassword, currentUser.get().getPassword())) {
      currentUser.get().setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(currentUser.get());
      return true;
    }
    return false;
  }
  @Override
  public void saveUser(User user)
  {
    userRepository.save(user);
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
  public List<User> getFollowList(String name)
  {
 return userRepository.findByName(name).get().getFriendList();
  }
}
