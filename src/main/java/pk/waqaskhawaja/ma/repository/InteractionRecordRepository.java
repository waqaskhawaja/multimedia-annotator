package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InteractionRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteractionRecordRepository extends JpaRepository<InteractionRecord, Long>, JpaSpecificationExecutor<InteractionRecord> {

}
