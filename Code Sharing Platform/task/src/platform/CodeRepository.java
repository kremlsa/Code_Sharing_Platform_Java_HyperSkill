package platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends CrudRepository<CodeDB, Long> {

    Optional<CodeDB> findTopByOrderByIdDesc();

    Optional<CodeDB> findByUuid(String uuid);

}
