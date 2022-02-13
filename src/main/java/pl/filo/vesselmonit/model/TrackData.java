package pl.filo.vesselmonit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackData {
    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("sog")
    private Double sog;
    @JsonProperty("rot")
    private Double rot;
    @JsonProperty("navstat")
    private Integer navstat;
    @JsonProperty("mmsi")
    private Integer mmsi;
    @JsonProperty("cog")
    private Double cog;
    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("shipType")
    private Integer shipType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("imo")
    private String imo;
    @JsonProperty("callsign")
    private String callsign;
    @JsonProperty("country")
    private String country;
    @JsonProperty("eta")
    private String eta;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("isSurvey")
    private Boolean isSurvey;
    @JsonProperty("heading")
    private Integer heading;
    @JsonProperty("draught")
    private Double draught;
    @JsonProperty("a")
    private Integer a;
    @JsonProperty("b")
    private Integer b;
    @JsonProperty("c")
    private Integer c;
    @JsonProperty("d")
    private Integer d;

}