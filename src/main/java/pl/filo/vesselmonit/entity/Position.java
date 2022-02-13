package pl.filo.vesselmonit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
    private Long id;
    private LocalDateTime time;
    //x position
    private Double longitude;
    //y position
    private Double latitude;
    //Course over ground - course expressed in degrees (in range 0 - 359.9, 360 indicates not available)
    private Double cog;
    //Speed over ground in knots
    private Double sog;
    //Rate of turn
    private Double rot;
    //Navigational status (as defined by AIS standard)
    private Integer navstat;
    //Estimated time of arrival at destination
    private LocalDateTime eta;
    //The destination of the vessel
    private String destination;
    //Heading, in degrees (0-359)
    private Integer heading;
    //Draught of vessel, in 0.1 meter increments
    private Double draught;
    private Boolean survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter(value = AccessLevel.NONE)
    private Vessel vessel;
}
