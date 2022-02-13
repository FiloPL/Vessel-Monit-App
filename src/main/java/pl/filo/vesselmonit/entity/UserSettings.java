package pl.filo.vesselmonit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings
{
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "show_time")
    private boolean showTime;
    @Column(name = "show_coordinates")
    private boolean showCoordinates;
    @Column(name = "show_cog")
    private boolean showCog;
    @Column(name = "show_sog")
    private boolean showSog;
    @Column(name = "show_rot")
    private boolean showRot;
    @Column(name = "show_navstat")
    private boolean showNavstat;
    @Column(name = "show_eta")
    private boolean showEta;
    @Column(name = "show_destination")
    private boolean showDestination;
    @Column(name = "show_heading")
    private boolean showHeading;
    @Column(name = "show_draught")
    private boolean showDraught;
    @Column(name = "show_survey")
    private boolean showSurvey;
    @ElementCollection
    @CollectionTable(name = "favorite_vessels_mmsi")
    private List<Integer> favoriteVesselsMmsi = new ArrayList<>();

}