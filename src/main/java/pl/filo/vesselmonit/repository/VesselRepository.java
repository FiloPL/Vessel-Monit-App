package pl.filo.vesselmonit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filo.vesselmonit.entity.Position;
import pl.filo.vesselmonit.entity.Vessel;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Integer> {
}