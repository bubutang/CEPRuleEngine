package com.drools.imagesuite.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsIntroduction {
	private String topic;
	
	public DroolsIntroduction(String topic) {
		this.topic	=	topic;
	}
	
	
	public static final void main(String[] args){
		try {
			//load up the knowledge base
			KieServices ks	=	KieServices.Factory.get();
			KieContainer kContainer	=	ks.getKieClasspathContainer();
			KieSession kSession	=	kContainer.newKieSession("ksession-rules");
			
			DroolsIntroduction droolsIntroduction	=	new DroolsIntroduction("Drools");
			kSession.insert(droolsIntroduction);
			kSession.insert(new DroolsIntroduction("Spring"));
			kSession.insert(new DroolsIntroduction("Drools"));
			kSession.fireAllRules();
			
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public String getTopic() {
		return topic;
	}
	
	
	
	public String introductYourself()	{
		return "Drools 6.3 Final";
	}
	
}
