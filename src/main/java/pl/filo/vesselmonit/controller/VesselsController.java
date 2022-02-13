package pl.filo.vesselmonit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.filo.vesselmonit.entity.User;
import pl.filo.vesselmonit.entity.UserSettings;
import pl.filo.vesselmonit.entity.Vessel;
import pl.filo.vesselmonit.model.UserSettingsModel;
import pl.filo.vesselmonit.model.VesselModel;
import pl.filo.vesselmonit.service.AuthenticationUserService;
import pl.filo.vesselmonit.service.UserService;
import pl.filo.vesselmonit.service.UserSettingsService;
import pl.filo.vesselmonit.service.VesselService;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
@Controller
public class VesselsController
{
    private final AuthenticationUserService authenticationUserService;
    private final VesselService vesselService;
    private final UserService userService;
    private final UserSettingsService userSettingsService;

    public VesselsController(
            final AuthenticationUserService authenticationUserService,
            final VesselService vesselService,
            final UserService userService,
            final UserSettingsService userSettingsService)
    {
        this.authenticationUserService = authenticationUserService;
        this.vesselService = vesselService;
        this.userService = userService;
        this.userSettingsService = userSettingsService;
    }

    @GetMapping("/vessels")
    public String getVessels(
            final ModelMap modelMap,
            @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser)
    {
        authenticationUserService.setAuthorizationInfo(modelMap, authenticationUser);
        modelMap.addAttribute("pageName", "vessels");
        final User user = userService.getByUsernameOrEmail(authenticationUser.getUsername());
        if(user == null)
        {
            modelMap.addAttribute("favoriteVessels", Collections.emptyList());
            modelMap.addAttribute("otherVessels", Collections.emptyList());
        }
        else
        {
            final UserSettings userSettings = userSettingsService.getUserSettings(user.getId());
            final List<Integer> favoriteVesselsMmsi = userSettings.getFavoriteVesselsMmsi();
            final List<VesselModel> favoriteVessels = getVesselModels(vesselService.getVessels()).stream()
                    .filter(vessel -> favoriteVesselsMmsi.contains(vessel.getMmsi()))
                    .collect(toList());
            final List<VesselModel> otherVessels = getVesselModels(vesselService.getVessels()).stream()
                    .filter(vessel -> !favoriteVesselsMmsi.contains(vessel.getMmsi()))
                    .collect(toList());
            modelMap.addAttribute("favoriteVessels", favoriteVessels);
            modelMap.addAttribute("otherVessels", otherVessels);
        }
        return "vessels";
    }

    @PostMapping("/vessels")
    public String updateFavorites(
            @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser,
            @RequestParam final Integer vesselMmsi, @RequestParam final boolean add)
    {
        final User user = userService.getByUsernameOrEmail(authenticationUser.getUsername());
        if(user != null)
        {
            userSettingsService.updateFavoriteVesselMmsi(user.getId(), vesselMmsi, add);
        }
        return "redirect:/vessels";
    }

    private List<VesselModel> getVesselModels(final Collection<Vessel> vessels)
    {
        return vessels.stream()
                .map(vessel -> new VesselModel(
                        vessel.getMmsi(),
                        vessel.getName(),
                        vessel.getImo(),
                        vessel.getCallsign(),
                        vessel.getCountry(),
                        vessel.getShipType(),
                        VesselService.SHIP_TYPE_MAP.getOrDefault(vessel.getShipType(), ""),
                        vessel.getA(),
                        vessel.getB(),
                        vessel.getC(),
                        vessel.getD()))
                .sorted(Comparator.comparing(VesselModel::getMmsi))
                .collect(toList());
    }

    @GetMapping("/vessels/statistics/{id}")
    public String getVesselStatisticsById(
            final ModelMap modelMap, @PathVariable final Integer id,
            @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser)
    {
        authenticationUserService.setAuthorizationInfo(modelMap, authenticationUser);
        modelMap.addAttribute("pageName", "vessels");
        modelMap.addAttribute("userSettings", getUserSettings(authenticationUser));
        final Vessel vessel = vesselService.getVesselById(id);
        final VesselModel vesselModel = getVesselModels(Collections.singleton(vessel))
                .stream()
                .findFirst()
                .orElse(null);
        modelMap.addAttribute("vessel", vesselModel);
        modelMap.addAttribute("positions", vessel.getPositions());
        return "vessel-statistics";
    }

    private UserSettingsModel getUserSettings(final org.springframework.security.core.userdetails.User authenticationUser)
    {
        final User user = userService.getByUsernameOrEmail(authenticationUser.getUsername());
        final UserSettings userSettings = userSettingsService.getUserSettings(user.getId());
        return new UserSettingsModel(
                userSettings.isShowTime(),
                userSettings.isShowCoordinates(),
                userSettings.isShowCog(),
                userSettings.isShowSog(),
                userSettings.isShowRot(),
                userSettings.isShowNavstat(),
                userSettings.isShowEta(),
                userSettings.isShowDestination(),
                userSettings.isShowHeading(),
                userSettings.isShowDraught(),
                userSettings.isShowSurvey());
    }

}