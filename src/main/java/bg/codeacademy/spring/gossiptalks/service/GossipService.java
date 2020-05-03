package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GossipService
{
  Page<Gossips> findAllGossipsByUser(User user, Pageable pageable);

  Page<Gossips> getAllGossipsOfFriends(String username, Pageable pageable);

  void saveGossip(Gossips gossip);
}
