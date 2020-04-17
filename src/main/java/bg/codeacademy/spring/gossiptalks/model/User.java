package bg.codeacademy.spring.gossiptalks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private Integer       id;
  @Column(unique = true, nullable = false)
  @NotBlank
  private String        username;
  @Column(nullable = false)
  private String        password;
  @Column(unique = true, nullable = false)
  private String        email;
  @Column(nullable = false)
  private String        name;
  @Column(columnDefinition = "boolean default false")
  private Boolean       following;
  @OneToMany(mappedBy = "user")
  private List<Gossips> gossips;

  public Integer userActivity()
  {
    return this.getGossips().size();
  }

}



