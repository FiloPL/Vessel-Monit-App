package pl.filo.vesselmonit.service;

import org.springframework.stereotype.Service;

import pl.filo.vesselmonit.entity.UserSettings;
import pl.filo.vesselmonit.model.UserSettingsModel;
import pl.filo.vesselmonit.repository.UserSettingsRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserSettingsService
{
    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsService(final UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSettings getUserSettings(final Long userId) {
        return userSettingsRepository.getById(userId);
    }

    public UserSettings saveUserSettings(final Long userId, final UserSettingsModel userSettingsModel) {
        final UserSettings userSettings = userSettingsRepository.findById(userId).orElse(null);
        userSettings.setShowTime(userSettingsModel.isShowTime());
        userSettings.setShowCoordinates(userSettingsModel.isShowCoordinates());
        userSettings.setShowCog(userSettingsModel.isShowCog());
        userSettings.setShowSog(userSettingsModel.isShowSog());
        userSettings.setShowRot(userSettingsModel.isShowRot());
        userSettings.setShowNavstat(userSettingsModel.isShowNavstat());
        userSettings.setShowEta(userSettingsModel.isShowEta());
        userSettings.setShowDestination(userSettingsModel.isShowDestination());
        userSettings.setShowHeading(userSettingsModel.isShowHeading());
        userSettings.setShowDraught(userSettingsModel.isShowDraught());
        userSettings.setShowSurvey(userSettingsModel.isShowSurvey());
        return userSettingsRepository.save(userSettings);
    }

    public void createDefaultSettings(final Long userId) {
        final UserSettings userSettings = new UserSettings(userId, true, true, true,
                true, true, true, true, true, true,
                true, true, Collections.emptyList());
        userSettingsRepository.save(userSettings);
    }

    public void updateFavoriteVesselMmsi(final Long userId, final Integer mmsi, final boolean add) {
        final UserSettings userSettings = userSettingsRepository.findById(userId).orElse(null);
        if(userSettings != null) {
            final List<Integer> favoriteVesselsMmsi = userSettings.getFavoriteVesselsMmsi();
            if(add) {
                favoriteVesselsMmsi.add(mmsi);
            }
            else {
                favoriteVesselsMmsi.remove(mmsi);
            }
            userSettings.setFavoriteVesselsMmsi(favoriteVesselsMmsi);
            userSettingsRepository.save(userSettings);
        }
    }

}