package bg.codeacademy.spring.gossiptalks.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})
})
public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @Column(name = "username", nullable = false, unique = true, updatable = false)
  private String  username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String  email;

  @Column(name = "name", nullable = false)
  private String  name;

  @Column(name = "following", columnDefinition = "boolean default false")
  private Boolean following;

//  @OneToMany(mappedBy = "user_id")
//  private List<Gossip> gossips;

//  public Integer userActivity(){
//    return this.getGossips().size();
//  }
}
