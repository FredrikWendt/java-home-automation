package se.wendt.rules.impl;

public class RulesParser {

	private Iterable<String> ruleLines;
	private Environment environment;

	public RulesParser(Environment environment, Iterable<String> ruleLines) {
		this.environment = environment;
		this.ruleLines = ruleLines;
		parse();
	}

	private void parse() {
		StringBuilder sb = new StringBuilder();
		for (String line : ruleLines) {
			if (line.startsWith("rule")) {
				addRuleToEngine(sb);
				sb = new StringBuilder(line);
				sb.append("\n");
			} else {
				sb.append(line);
				sb.append("\n");
			}
		}
		addRuleToEngine(sb);
	}

	private void addRuleToEngine(StringBuilder sb) {
		if (sb.length() > 0) {
			RuleImpl rule = new RuleImpl(environment);
			new RuleParser(sb.toString(), rule);
		}
	}

}
