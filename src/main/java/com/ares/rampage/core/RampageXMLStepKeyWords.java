package com.ares.rampage.core;

public enum RampageXMLStepKeyWords {
	
	DO("DO"), URL("URL"), SESSIONURL("SESSIONURL"), SLEEPTIME("SLEEPTIME"), 
	SEQUENCE("SEQUENCE"), COMMAND("COMMAND"), STEPID("STEPID");
    
	private String value;

    private RampageXMLStepKeyWords(String value) {
            this.value = value;
    }
};
