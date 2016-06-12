package com.drools.MeeHealth.model;


import java.util.ArrayList;
import java.util.List;

import com.drools.MeeHealth.model.FlightStatus;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class EmergencySystem {

    private List<FlightStatus> redirectedFligts = new ArrayList<FlightStatus>();

    public void sendFireTrucks() {
        System.out.println("-> Fire trucks sended");
    }

    public void sendAmbulances() {
        System.out.println("-> Ambulances sended");
    }

    public void cleanRunways() {
        System.out.println("-> Cleaning runways in progress");
    }

    public void redirect(FlightStatus flightStatus) {
        System.out.println("Redirecting flight " + flightStatus.getFlight() + " to another runway");
        redirectedFligts.add(flightStatus);
    }

    public List<FlightStatus> getRedirectedFlights() {
        return redirectedFligts;
    }

}
