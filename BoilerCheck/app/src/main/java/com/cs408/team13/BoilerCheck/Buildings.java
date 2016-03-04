package com.cs408.team13.BoilerCheck;


import android.os.Build;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Jacob on 2/16/2016.
 */
public class Buildings {
    public Building[] Buildings;
    private double shortestDistance = 30;
    public static Comparator<Building> NAME_ORDER =
            new Comparator<Building>() {
                //@Override
                public int compare(Building b1, Building b2) {
                    int compare = (b1.BuildingName).compareTo(b2.BuildingName);
                    return compare;
                }
            };


    public Building nearestBuilding() {
        double userLat = BoilerCheck.locationService.getLatitude();
        double userLong = BoilerCheck.locationService.getLongitude();
        Building closestBuilding = null;

        for(Building b : BoilerCheck.loadedBuildings.Buildings) {
            double[] buildingLocation = b.Coordinates;
            double distance = BoilerCheck.locationService.calculateDistance(userLat, userLong, buildingLocation[0], buildingLocation[1]);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestBuilding = b;
            }
        }
        if(isClose(shortestDistance))
            return closestBuilding;
        else
            return null;
    }

    public boolean isClose(double distance) {
        if(distance < 15)
            return true;

        return false;
    }

    public void nameSort() {
        Arrays.sort(Buildings, NAME_ORDER);
    }

    public void distanceSort() {
        Arrays.sort(Buildings);
    }


}
