package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
  @Query(value = "SELECT * FROM USER WHERE USERNAME = :username", nativeQuery = true)
  Optional<User> findByUsername(@Param("username") String username);

  @Query(value = "SELECT * FROM USER u  WHERE USERNAME  LIKE %:username% ORDER BY (SELECT COUNT(ID) FROM GOSSIP WHERE USER_ID = u.ID) DESC", nativeQuery = true)
  Set<User> findUser(@Param("username") String username);


}
