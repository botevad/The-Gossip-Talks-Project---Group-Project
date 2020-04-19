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
@Table
public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer    id;
  @Column(nullable = false, unique = true, updatable = false)
  private String     username;
  @Column(nullable = false)
  private String     password;
  @Column(nullable = false, unique = true)
  private String     email;
  @Column(nullable = false)
  private String     name;
  @ManyToMany
  private List<User> friendList;

}
