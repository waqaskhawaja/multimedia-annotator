package pk.waqaskhawaja.ma.repository;
import pk.waqaskhawaja.ma.domain.DataSetResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataSetResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSetResourceRepository extends JpaRepository<DataSetResource, Long> {

}
