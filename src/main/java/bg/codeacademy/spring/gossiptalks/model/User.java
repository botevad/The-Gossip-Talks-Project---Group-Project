package bg.codeacademy.spring.gossiptalks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer    id;
  @Column(unique = true, nullable = false)
  @NotBlank
  private String     username;
  @Column(nullable = false)
  private String     password;
  @Column(unique = true, nullable = false)
  private String     email;
  @Column(nullable = false)
  private String     name;
  private boolean active;
  private String roles;
  @ManyToMany
  private List<User> friendList;

  public User(@NotBlank String username, String password, String email, String name, String roles, boolean active)
  {
    this.username = username;
    this.password = password;
    this.email = email;
    this.name = name;
    this.roles = roles;
    this.active = active;
  }
}



