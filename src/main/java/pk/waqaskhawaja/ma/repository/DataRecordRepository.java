package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.DataRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {

}
