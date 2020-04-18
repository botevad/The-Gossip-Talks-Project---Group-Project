package bg.codeacademy.spring.gossiptalks.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

//  @Column(name = "following", columnDefinition = "boolean default false")
//  private Boolean following;

//  @OneToMany
//  private List<User> friends;

  private boolean isEnabled = true;

//  private Role      role;
//  private Set<Role> authorities;

  @ManyToMany
  private List<User> friendList;
}
