package com.cs408.team13.BoilerCheck;

import java.util.Comparator;

/**
 * Created by Jacob on 2/16/2016.
 */
public class Building implements Comparable<Building> {
    public String _id;
    public String BuildingName;
    public String Category;
    public double[] Coordinates;
    public int TotalCapacity;
    public int CurrentCapacity;
    public int distance;


    public int compareTo(Building anotherBuilding) throws ClassCastException {
        if (!(anotherBuilding instanceof Building))
            throw new ClassCastException("A Building object expected.");
        double anotherBuildingDistance = BoilerCheck.locationService.calculateDistance(
                (int)anotherBuilding.Coordinates[0], (int)anotherBuilding.Coordinates[1], (int)BoilerCheck.locationService.getLatitude(),
                (int)BoilerCheck.locationService.getLongitude());

        double currentBuildingDistance = BoilerCheck.locationService.calculateDistance(
                (int)this.Coordinates[0], (int)this.Coordinates[1], (int)BoilerCheck.locationService.getLatitude(),
                (int)BoilerCheck.locationService.getLongitude());
        return (int)(anotherBuildingDistance - currentBuildingDistance);
    }
}