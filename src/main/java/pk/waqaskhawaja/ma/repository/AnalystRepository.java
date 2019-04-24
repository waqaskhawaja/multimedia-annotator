package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.Analyst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Analyst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalystRepository extends JpaRepository<Analyst, Long> {

}
