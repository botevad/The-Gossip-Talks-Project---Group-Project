package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.validation.NotHTML;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "gossips")
public class Gossips
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer       id;
  @Column(updatable = false)
  @NotHTML
  private String        gossip;
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "username", referencedColumnName = "username")
  private User          user;
  @Column(updatable = false)
  private LocalDateTime date = LocalDateTime.now();
}
