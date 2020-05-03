package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;

public interface GossipService
{
  List<Gossips> findAllGossipsByUser(User user);

  List<Gossips> getAllGossipsOfFriends(String username);

  void saveGossip(Gossips gossip);
}
