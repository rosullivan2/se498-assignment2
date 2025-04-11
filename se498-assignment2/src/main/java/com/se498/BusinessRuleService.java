package com.se498;

public class BusinessRuleService {

    //Implement method(s) for the rule execution
    public boolean applyBusinessRule(BusinessRule rule, Object objectToCheck) {
        try {
            return rule.apply(objectToCheck);
        } catch (Exception e) {
            System.err.println("Rule application failed: " + e.getMessage());
            return false;
        }
    }
}
