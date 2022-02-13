package pl.filo.vesselmonit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point
{
    private final double x;
    private final double y;
    private final VesselModel vesselModel;

    /*
    public Point(double x, double y, VesselModel vesselModel) {
        this.x = x;
        this.y = y;
        this.vesselModel = vesselModel;
    }
    */

}
