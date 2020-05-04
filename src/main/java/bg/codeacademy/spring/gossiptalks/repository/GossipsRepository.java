package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GossipsRepository extends JpaRepository<Gossip, Integer>
{
  Optional<Page<Gossip>> findAllByUser(User user, Pageable pageable);

  List<Gossip> findAllByUser(User user);

  @Query(value = "SELECT * FROM GOSSIP WHERE USER_ID  IN (SELECT DISTINCT USER_ID  FROM USER_FRIEND_LIST) ORDER BY DATETIME DESC", nativeQuery = true)
  Optional<Page<Gossip>> findAllGossipsOfFriend(Pageable pageable);
}
