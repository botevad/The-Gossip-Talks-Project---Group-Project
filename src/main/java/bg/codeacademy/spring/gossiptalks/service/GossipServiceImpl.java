package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GossipServiceImpl implements GossipService
{
  private final GossipsRepository gossipsRepository;

  @Autowired
  public GossipServiceImpl(GossipsRepository gossipsRepository)
  {
    this.gossipsRepository = gossipsRepository;
  }


  @Override
  public Optional<List<Gossips>> findAllGossipsByUser(User user)
  {
    return gossipsRepository.findAllGossipsByUser(user);
  }

  @Override
  public List<Gossips> getAllGossipsOfFriends(User currentUser)
  {
    List<Gossips> allGossips = gossipsRepository.findAll();
    List<Gossips> gossipsList = new ArrayList<>();
    for (Gossips g : allGossips
         ) {
      if (currentUser.getFriendList().contains(g.getUser()))
      {
        gossipsList.add(g);
      }
    }
    return gossipsList;
  }

  @Override
  public void addGossip(Gossips gossip)
  {
    gossipsRepository.save(gossip);
  }

}
