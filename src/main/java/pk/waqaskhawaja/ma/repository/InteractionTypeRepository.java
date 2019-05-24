package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.InteractionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the InteractionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteractionTypeRepository extends JpaRepository<InteractionType, Long>, JpaSpecificationExecutor<InteractionType> {

    Optional<InteractionType> findByName(String name);

}
