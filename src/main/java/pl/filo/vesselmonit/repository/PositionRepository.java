package pl.filo.vesselmonit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filo.vesselmonit.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long>
{
}