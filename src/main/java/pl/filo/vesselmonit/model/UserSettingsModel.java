package pl.filo.vesselmonit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSettingsModel {
    private boolean showTime;
    private boolean showCoordinates;
    private boolean showCog;
    private boolean showSog;
    private boolean showRot;
    private boolean showNavstat;
    private boolean showEta;
    private boolean showDestination;
    private boolean showHeading;
    private boolean showDraught;
    private boolean showSurvey;

}