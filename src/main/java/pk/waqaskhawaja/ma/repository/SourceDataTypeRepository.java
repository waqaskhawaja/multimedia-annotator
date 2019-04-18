package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.SourceDataType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SourceDataType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceDataTypeRepository extends JpaRepository<SourceDataType, Long> {

}
