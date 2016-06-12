package com.drools.MeeHealth;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.drools.MeeHealth.model.EmergencySystem;
import com.drools.MeeHealth.model.FlighSimulation;

import org.junit.Test;


/**
 * 
 * @author Tom Tang
 * 
 */
public class FlightControlTest {

    private StatefulKnowledgeSession ksession;

    @Before
    public void start() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add( ResourceFactory.newFileResource( "D:\\Eclipse WorkSpace\\MyFirstCEPExample\\src\\main\\resources\\rules\\FlightRule.drl" ), ResourceType.DRL );

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();

        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("pseudo"));

        ksession = kbase.newStatefulKnowledgeSession(conf, null);

    }

    @Test
    public void emergencySimulation() throws InterruptedException {

        SessionPseudoClock clock = ksession.getSessionClock();

        EmergencySystem emergencySystem = new EmergencySystem();
        ksession.setGlobal("emergencySystem", emergencySystem);

        FlighSimulation flightAA001 = new FlighSimulation("AA001", "San Francisco", "Los Angeles", 270);
        FlighSimulation flightAA002 = new FlighSimulation("AA002", "Dallas", "Los Angeles", 470);

        WorkingMemoryEntryPoint flightLandingEntryPoint = ksession.getWorkingMemoryEntryPoint("flight-landing");
        flightLandingEntryPoint.insert(flightAA001.update());
        clock.advanceTime(2, TimeUnit.MINUTES);
        ksession.fireAllRules();
        flightLandingEntryPoint.insert(flightAA002.update());
        ksession.fireAllRules();

        Assert.assertEquals(1, emergencySystem.getRedirectedFlights().size());

    }

    @After
    public void stop() {
        ksession.halt();
        ksession.dispose();
    }

}

