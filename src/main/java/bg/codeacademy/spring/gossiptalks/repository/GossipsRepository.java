package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GossipsRepository extends JpaRepository<Gossip, Integer>
{
  Optional<Page<Gossip>> findAllByUser(User user, Pageable pageable);

  Optional<List<Gossip>> findAllByUser(User user);
}
