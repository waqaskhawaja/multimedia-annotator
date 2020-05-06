package pk.waqaskhawaja.ma.repository;
import pk.waqaskhawaja.ma.domain.DataSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {

}
