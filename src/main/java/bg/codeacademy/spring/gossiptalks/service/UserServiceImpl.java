package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository        userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public List<User> getAllUsersByName(String name)
  {
    return userRepository.findAllByNameContaining(name).orElse(null);
  }

  @Override
  public Boolean changePassword(User user, String oldPassword, String password)
  {
    if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(bCryptPasswordEncoder.encode(password));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @Override
  public Optional<User> getUserByUsername(String username)
  {

    return Optional.ofNullable(userRepository.findByUsername(username).orElse(null));
  }

  @Override
  public List<User> getFollowList(String name)
  {
    return Optional.ofNullable(userRepository.findByName(name).get().getFriendList()).orElse(null);
  }

  @Override
  public void saveUser(User user)
  {
    this.userRepository.save(user);
  }

  @Override
  public List<User> getAllUsers()
  {

    return userRepository.findAll();
  }
}
