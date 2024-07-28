package com.yourname.ruleengine.data;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<Integer, String> ruleStorage;
    private int idCounter;

    public Database() {
        this.ruleStorage = new HashMap<>();
        this.idCounter = 1;
    }

    public int saveRule(String rule) {
        ruleStorage.put(idCounter, rule);
        return idCounter++;
    }

    public Map<Integer, String> getRules() {
        return ruleStorage;
    }
}
