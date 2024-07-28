package com.yourname.ruleengine.api;

import com.yourname.ruleengine.ast.Node;
import com.yourname.ruleengine.data.Database;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RuleService {
    private Database database;

    public RuleService() {
        this.database = new Database();
    }

    public int createRule(String ruleString) {
        return database.saveRule(ruleString);
    }

    public Node parseRule(String ruleString) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = ruleString.split(" ");
        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR")) {
                Node right = stack.pop();
                Node left = stack.pop();
                stack.push(new Node("operator", left, right, token));
            } else {
                stack.push(new Node("operand", null, null, token));
            }
        }
        return stack.pop();
    }

    public Node combineRules(List<String> rules) {
        Node combinedRoot = null;
        for (String rule : rules) {
            Node ruleNode = parseRule(rule);
            if (combinedRoot == null) {
                combinedRoot = ruleNode;
            } else {
                combinedRoot = new Node("operator", combinedRoot, ruleNode, "AND");
            }
        }
        return combinedRoot;
    }

    public boolean evaluateRule(Node ast, Map<String, Object> data) {
        if (ast == null) {
            return false;
        }
        if (ast.getType().equals("operator")) {
            if (ast.getValue().equals("AND")) {
                return evaluateRule(ast.getLeft(), data) && evaluateRule(ast.getRight(), data);
            } else if (ast.getValue().equals("OR")) {
                return evaluateRule(ast.getLeft(), data) || evaluateRule(ast.getRight(), data);
            }
        } else if (ast.getType().equals("operand")) {
            String[] condition = ast.getValue().split("=");
            String attribute = condition[0].trim();
            String value = condition[1].trim().replace("'", "");
            return data.get(attribute).toString().equals(value);
        }
        return false;
    }

    public Map<Integer, String> getAllRules() {
        return database.getRules();
    }
}
