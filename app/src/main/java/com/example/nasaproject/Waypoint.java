package com.example.nasaproject;

/**
 * This class stores waypoint information.
 * @since 2021-03-26
 */

public class Waypoint {
    public String description;
    public double xCoord;
    public double yCoord;

    @Override
    public String toString() {
        return "{ x: " + xCoord + ", y: " + yCoord + "}";
    }
}
