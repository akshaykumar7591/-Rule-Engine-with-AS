package com.yourname.ruleengine.ui;

import com.yourname.ruleengine.api.RuleService;
import com.yourname.ruleengine.ast.Node;

import java.util.*;

public class RuleEngineUI {
    private RuleService ruleService;

    public RuleEngineUI() {
        this.ruleService = new RuleService();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create Rule");
            System.out.println("2. Combine Rules");
            System.out.println("3. Evaluate Rule");
            System.out.println("4. View All Rules");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter rule:");
                    String rule = scanner.nextLine();
                    int ruleId = ruleService.createRule(rule);
                    System.out.println("Rule created with ID: " + ruleId);
                    break;
                case 2:
                    System.out.println("Enter rule IDs to combine (comma-separated):");
                    String ids = scanner.nextLine();
                    List<String> rulesToCombine = new ArrayList<>();
                    for (String id : ids.split(",")) {
                        rulesToCombine.add(ruleService.getAllRules().get(Integer.parseInt(id.trim())));
                    }
                    Node combinedRule = ruleService.combineRules(rulesToCombine);
                    System.out.println("Combined Rule AST: " + combinedRule);
                    break;
                case 3:
                    System.out.println("Enter rule ID to evaluate:");
                    int evalRuleId = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.println("Enter data (format: key=value, comma-separated):");
                    String dataString = scanner.nextLine();
                    Map<String, Object> data = new HashMap<>();
                    for (String pair : dataString.split(",")) {
                        String[] keyValue = pair.split("=");
                        data.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                    Node ruleToEvaluate = ruleService.parseRule(ruleService.getAllRules().get(evalRuleId));
                    boolean result = ruleService.evaluateRule(ruleToEvaluate, data);
                    System.out.println("Evaluation Result: " + result);
                    break;
                case 4:
                    System.out.println("All Rules:");
                    for (Map.Entry<Integer, String> entry : ruleService.getAllRules().entrySet()) {
                        System.out.println("ID: " + entry.getKey() + ", Rule: " + entry.getValue());
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        RuleEngineUI ui = new RuleEngineUI();
        ui.start();
    }
}
