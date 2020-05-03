package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GossipServiceImpl implements GossipService
{
  private final UserRepository    userRepository;
  private final GossipsRepository gossipsRepository;
  private final UserService       userService;

  @Autowired
  public GossipServiceImpl(UserRepository userRepository, GossipsRepository gossipsRepository, UserService userService)
  {
    this.userRepository = userRepository;
    this.gossipsRepository = gossipsRepository;
    this.userService = userService;
  }

  @Override
  public Page<Gossip> findAllGossipsByUser(User user, Pageable pageable)
  {

    return gossipsRepository.findAllByUser(user, pageable).get();
  }

  @Override
  public Page<Gossip> getAllGossipsOfFriends(String username, Pageable pageable)
  {
    User currentUser = userRepository.findByUsername(username).get();
    List<User> friendList = userService.getFriendList(username);
//    List<Gossips> friendGossips = gossipsRepository
//        .findAll()
//        .stream()
//        .filter(gossips -> friendList.contains(gossips.getUser()))
//        .collect(Collectors.toList());
    List<Gossip> friendGossips = new ArrayList<>();
    for (User friend : friendList) {
      friendGossips.addAll(gossipsRepository.findAllByUser(friend, pageable).get().getContent());
    }
    return new PageImpl(friendGossips, pageable, friendGossips.size());
  }

  @Override
  public void saveGossip(Gossip gossip)
  {
    gossipsRepository.save(gossip);
  }

}
