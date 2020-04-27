package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the InteractionRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteractionRecordRepository extends JpaRepository<InteractionRecord, Long>, JpaSpecificationExecutor<InteractionRecord> {

  public InteractionRecord findOneByTime(Integer  time);
  public InteractionRecord findByDuration(Integer duration);
  public List<InteractionRecord> findListByDuration(Integer duration);

}
