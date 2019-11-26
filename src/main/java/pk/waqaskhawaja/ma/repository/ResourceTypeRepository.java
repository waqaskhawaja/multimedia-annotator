package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.ResourceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ResourceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long>, JpaSpecificationExecutor<ResourceType> {

    Optional<ResourceType> findByName(String name);

}
