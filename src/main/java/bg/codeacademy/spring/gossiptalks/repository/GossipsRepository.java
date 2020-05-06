package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GossipsRepository extends JpaRepository<Gossip, Integer>
{
  @Query(value = "SELECT * FROM GOSSIP g WHERE USER_ID = :user_id ORDER BY DATETIME DESC", nativeQuery = true)
  Page<Gossip> findAllGossipsOfUser(@Param("user_id") Integer user_id, Pageable pageable);

  @Query(value = "SELECT * FROM GOSSIP g WHERE USER_ID IN (SELECT FRIEND_LIST_ID FROM USER_FRIEND_LIST uf WHERE uf.USER_ID = :user_id) ORDER BY DATETIME DESC", nativeQuery = true)
  Page<Gossip> findAllGossipsOfFriends(@Param("user_id") Integer user_id, Pageable pageable);
}
