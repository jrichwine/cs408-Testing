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


    public int compareTo(Building anotherBuilding) throws ClassCastException {
        if (!(anotherBuilding instanceof Building))
            throw new ClassCastException("A Building object expected.");
        double anotherBuildingDistance = BoilerCheck.locationService.calculateDistance(
                anotherBuilding.Coordinates[0], anotherBuilding.Coordinates[1],BoilerCheck.locationService.getLatitude(),
                BoilerCheck.locationService.getLongitude());

        double currentBuildingDistance = BoilerCheck.locationService.calculateDistance(
                this.Coordinates[0], this.Coordinates[1], BoilerCheck.locationService.getLatitude(),
                BoilerCheck.locationService.getLongitude());
        return (int)(anotherBuildingDistance - currentBuildingDistance);
    }
}