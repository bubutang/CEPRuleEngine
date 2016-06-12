package com.drools.MeeHealth.model;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.core.WorkingMemoryEntryPoint;
import org.drools.core.io.impl.ClassPathResource;
import org.drools.core.io.impl.DescrResource;
import org.drools.core.marshalling.impl.ProtobufMessages.ActionQueue.Assert;
import org.drools.core.time.SessionPseudoClock;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;



public class MyFirstCEPExample {

    private static final int AIRPORT_AIR_SPACE_SCOPE = 50;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
	    kBuilder.add( ResourceFactory.newFileResource( "D:\\Eclipse WorkSpace\\MyFirstCEPExample\\src\\main\\resources\\rules\\FlightRule.drl" ), ResourceType.DRL );
	    
	    // Check for errors
	    if( kBuilder.hasErrors() ){
	        for( KnowledgeBuilderError err: kBuilder.getErrors() ){
	        	System.err.println( err.toString() );
	        }
	        throw new IllegalStateException( "DRL errors" );
	    }
	    
	    KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
	    config.setOption(EventProcessingOption.STREAM);
	    
	    KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
	    conf.setOption(ClockTypeOption.get("pseudo"));
	    
	    //Collection<KnowledgePackage> kpkgs = kBuilder.getKnowledgePackages();

	    // Create the Knowledge Base
	    KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase(config);
	    
	    // Transfer compiled Knowledge Packages

	    kBase.addKnowledgePackages(kBuilder.getKnowledgePackages());
	    StatefulKnowledgeSession ksession = kBase.newStatefulKnowledgeSession(conf,null);
	    
	    SessionPseudoClock clock = ksession.getSessionClock();
	    
        new Thread() {
            @Override
            public void run() {
                ksession.fireUntilHalt();
            }
        }.start();
        
        
        
        FlightControl control = new FlightControl();
        control.setAirport("LAX");
        ksession.setGlobal("control", control);
        
//	    FlighSimulation flightAA001 = new FlighSimulation("AA001","San Francisco", "Los Angeles", 270);
//	    do {
//	    		FlightStatus flightStatus = flightAA001.update();
//	    		if (flightStatus.getDistance() <= AIRPORT_AIR_SPACE_SCOPE) {
//		    	 	ksession.getWorkingMemoryEntryPoint("flight-arrival").insert(flightStatus);
//		    		clock.advanceTime(7, TimeUnit.MINUTES);
//		    		EmergencySignal emergencySignal = new EmergencySignal("AA001", 40);
//		    		ksession.getWorkingMemoryEntryPoint("emergency-channel").insert(emergencySignal);
//	    		} 
//	    		else {
//		    		ksession.getWorkingMemoryEntryPoint("flight-control").insert(flightStatus);
//		    		clock.advanceTime(5, TimeUnit.MINUTES);
//	    		}
//	    } while (!flightAA001.isLanded());
	    
//        new Thread() {
//            @Override
//            public void run() {
//                final FlighSimulation flightAA001 = new FlighSimulation("AA002", "BeiJing", "ShangHai", 300);
//
//                for (int i = 0; i < 10; i++) {
//                    FlightStatus flightStatus = flightAA001.update();
//                    ksession.getWorkingMemoryEntryPoint("flight-control").insert(flightStatus);
//                    clock.advanceTime(5, TimeUnit.MINUTES);
//                    try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                }
//        	    
//            }
//        }.start();
        
        new Thread() {
            @Override
            public void run() {
                EmergencySystem emergencySystem = new EmergencySystem();
                ksession.setGlobal("emergencySystem", emergencySystem);

                FlighSimulation flightAA003 = new FlighSimulation("AA003", "San Francisco", "Los Angeles", 270);
                FlighSimulation flightAA004 = new FlighSimulation("AA004", "Dallas", "Los Angeles", 470);

                
                ksession.getWorkingMemoryEntryPoint("flight-landing").insert(flightAA003.update());
                clock.advanceTime(2, TimeUnit.MINUTES);
                ksession.fireAllRules();
                ksession.getWorkingMemoryEntryPoint("flight-landing").insert(flightAA004.update());
                ksession.fireAllRules();

                //Assert.assertEquals(1, emergencySystem.getRedirectedFlights().size());
            }
        }.start();
	    
	   
	}

}
