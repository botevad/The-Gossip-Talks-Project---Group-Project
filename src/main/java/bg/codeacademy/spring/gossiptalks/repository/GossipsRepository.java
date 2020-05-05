package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GossipsRepository extends JpaRepository<Gossip, Integer>
{


  List<Gossip> findAllByUser(User user);

  Optional<Page<Gossip>> findAllByUserOrderByDatetimeDesc(User user, Pageable pageable);

  @Query(value = "SELECT * FROM GOSSIP g WHERE USER_ID in (SELECT FRIEND_LIST_ID FROM USER_FRIEND_LIST uf WHERE uf.USER_ID = ?1) ORDER BY DATETIME DESC", nativeQuery = true)
  Optional<Page<Gossip>> findAllGossipsOfFriends(@Param("user_id") Integer user_id, Pageable pageable);
}
