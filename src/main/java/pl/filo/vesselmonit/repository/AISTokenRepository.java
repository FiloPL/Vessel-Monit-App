package pl.filo.vesselmonit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filo.vesselmonit.entity.AISToken;

@Repository
public interface AISTokenRepository extends JpaRepository<AISToken, Integer>
{
}