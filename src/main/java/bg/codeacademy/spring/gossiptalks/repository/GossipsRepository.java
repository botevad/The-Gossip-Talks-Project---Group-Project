package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GossipsRepository extends JpaRepository<Gossips, Integer>
{
  Optional<List<Gossips>> findAllGossipsByUserOrderByDateDesc(User user);
}
