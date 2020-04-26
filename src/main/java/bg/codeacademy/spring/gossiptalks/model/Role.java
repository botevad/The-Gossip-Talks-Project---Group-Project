package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role implements GrantedAuthority
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String   id;
  @Column
  @Enumerated(EnumType.STRING)
  private RoleEnum role;

  @ManyToMany
  Set<User> user;

  @Override
  @Transient
  public String getAuthority()
  {
    return this.role.name();
  }
}
