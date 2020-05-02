package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GossipsRepository extends JpaRepository<Gossips, Integer>
{
  Optional<Page<Gossips>> findAllGossipsByUserOrderByDateDesc(User user, Pageable pageable);
}
