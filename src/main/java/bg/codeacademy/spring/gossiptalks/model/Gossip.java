package bg.codeacademy.spring.gossiptalks.model;

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
public class Gossip
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Integer id;

@Column(name = "gossip", length = 255)
  private String gossip;

  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_name", referencedColumnName = "username")
  private User user;

  @Column(name = "date", updatable = false, columnDefinition = "timestamp default current_timestamp")
  private LocalDateTime date;
}
