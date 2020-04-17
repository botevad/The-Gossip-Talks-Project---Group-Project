package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "gossips", path = "gossips")
public interface GossipRepository extends JpaRepository<Gossip, Integer>
{
}
