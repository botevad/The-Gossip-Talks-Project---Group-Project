package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
  public List<Gossips> findAllGossipsByUser(User user)
  {

    return gossipsRepository.findAllGossipsByUserOrderByDateDesc(user).get();
  }

  @Override
  public List<Gossips> getAllGossipsOfFriends(String username)
  {
    User currentUser = userRepository.findByUsername(username).get();
    List<User> friendList = userService.getFriendList(username);
    List<Gossips> friendGossips = gossipsRepository
        .findAll()
        .stream()
        .filter(gossips -> friendList.contains(gossips.getUser()))
        .sorted(Comparator.comparing(Gossips::getDate).reversed())
        .collect(Collectors.toList());
//    List<Gossips> friendGossips = new ArrayList<>();
//    for (User user: friendList) {
//      List<Gossips> tempList = gossipsRepository.findAllGossipsByUserOrderByDateDesc(user).get();
//      for (Gossips gossip: tempList) {
//        friendGossips.add(gossip);
//      }
//    }
//    List<Gossips> temp =gossipsRepository.findAll(pageable).getContent().stream()
//        .filter(gossips -> friendList.contains(gossips.getUser())).collect(Collectors.toList());
//        return new PageImpl(temp,pageable,temp.size());
    return friendGossips;
  }

  @Override
  public void saveGossip(Gossips gossip)
  {
    gossipsRepository.save(gossip);
  }

}
