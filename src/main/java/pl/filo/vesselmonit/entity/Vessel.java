package pl.filo.vesselmonit.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vessel
{
    @Id
    private Integer mmsi;
    //Name of vessel
    private String name;
    //IMO (International Maritime Organization) number
    private String imo;
    //International radio call sign
    private String callsign;
    //The country where the vessel is registered
    private String country;
    //Type of ship/cargo
    @Column(name = "ship_type")
    private Integer shipType;
    //Ship dimension: Meters from the AIS antenna to the bow
    private Integer a;
    //Ship dimension: Meters from the AIS antenna to the stern
    private Integer b;
    //Ship dimension: Meters from the AIS antenna to port side of the vessel
    private Integer c;
    //Ship dimension: Meters from the AIS antenna to starboard side of the vessel
    private Integer d;

    @OneToMany(mappedBy = "vessel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(value = AccessLevel.NONE)
    private List<Position> positions = new ArrayList<>();

    public Vessel(
            final Integer mmsi,
            final String name,
            final String imo,
            final String callsign,
            final String country,
            final Integer shipType,
            final Integer a,
            final Integer b,
            final Integer c,
            final Integer d)
    {
        this.mmsi = mmsi;
        this.name = name;
        this.imo = imo;
        this.callsign = callsign;
        this.country = country;
        this.shipType = shipType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

}