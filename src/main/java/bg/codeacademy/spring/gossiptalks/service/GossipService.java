package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GossipService
{
  Page<Gossip> findAllGossipsByUser(User user, Pageable pageable);

  Page<Gossip> getAllGossipsOfFriends(User user, Pageable pageable);

  void saveGossip(Gossip gossip);
}
