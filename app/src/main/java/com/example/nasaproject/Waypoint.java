package com.example.nasaproject;

public class Waypoint {
    public String description;
    public double xCoord;
    public double yCoord;

    public String toString() {
        return "{ x: " + xCoord + ", y: " + yCoord + "}";
    }
}
