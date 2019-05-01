package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.DataType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataTypeRepository extends JpaRepository<DataType, Long> {

}
