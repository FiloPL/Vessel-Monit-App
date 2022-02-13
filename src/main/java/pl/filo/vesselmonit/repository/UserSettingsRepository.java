package pl.filo.vesselmonit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filo.vesselmonit.entity.UserSettings;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long>
{
}