package pl.filo.vesselmonit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VesselModel {
    private Integer mmsi;
    //Name of vessel
    private String name;
    //IMO (International Maritime Organization) number
    private String imo;
    //International radio call sign
    private String callsign;
    //The country where the vessel is registered
    private String country;
    //Type of ship/cargo (number)
    private Integer shipType;
    //Type of ship/cargo (name)
    private String shipTypeName;
    //Ship dimension: Meters from the AIS antenna to the bow
    private Integer a;
    //Ship dimension: Meters from the AIS antenna to the stern
    private Integer b;
    //Ship dimension: Meters from the AIS antenna to port side of the vessel
    private Integer c;
    //Ship dimension: Meters from the AIS antenna to starboard side of the vessel
    private Integer d;

}