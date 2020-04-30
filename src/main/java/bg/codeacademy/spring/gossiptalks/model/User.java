package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class User implements Serializable
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
  @ManyToMany
  private List<User> friendList = new ArrayList<>();

  private Role role = Role.USER;

  private static final long serialVersionUID = 1L;

  public Integer countFriends()
  {
    return this.getFriendList().size();
  }
}



