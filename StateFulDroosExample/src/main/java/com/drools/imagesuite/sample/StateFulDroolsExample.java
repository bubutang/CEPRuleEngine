package com.drools.imagesuite.sample;

import java.util.HashMap;
import java.util.Map;

import org.drools.conf.EventProcessingOption;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

public class StateFulDroolsExample {

	public static final void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			KieServices kServices	=	KieServices.Factory.get();
			KieContainer kContainer	=	kServices.getKieClasspathContainer();
			KieSession kSession	=	kContainer.newKieSession("ksession-rules-1");
			
			//KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
			//config.setOption( EventProcessingOption.CLOUD );
			
			
			
			String[] names = new String[] {"kitchen","bedroom","office","livingroom"};
			Map<String,Room> name2room = new HashMap<String,Room>();
			for(String name:names){
				Room room = new Room(name);
				name2room.put(name,room);
				kSession.insert(room);
				Sprinkler sprinkler	=	new Sprinkler(room);
				kSession.insert(sprinkler);
			}
			
			kSession.fireAllRules();
			
			
			Fire kitchenFire	=	new Fire(name2room.get("kitchen"));
			Fire officeFire		=	new Fire(name2room.get("office"));
			
			FactHandle kitchenFireHandle	=	kSession.insert(kitchenFire);
			FactHandle	officeFireHandle	=	kSession.insert(officeFire);
			
			kSession.fireAllRules();
			
			kSession.delete(kitchenFireHandle);
			kSession.delete(officeFireHandle);
			
			kSession.fireAllRules();
			
			
		}
		catch(Throwable t){
			t.printStackTrace();
		}

	}

}
