package se.wendt.rules.impl;

import se.wendt.rules.Variable;

public class RuleParser {

	private final RuleImpl rule;

	public RuleParser(String ruleDefinition, RuleImpl rule) {
		this.rule = rule;
		
		parseDefinition(ruleDefinition);
	}

	protected void parseDefinition(String ruleDefinition) {
		String[] parts = ruleDefinition.split("\n");
		this.rule.setName(stripFirstWord(parts[0].trim()));
		
		int i = 1;
		if ("when".equals(parts[i].trim())) {
			i = addConditions(parts, ++i);
		}
		if (i < parts.length) {
			addActions(parts, i+1);
		}
	}

	private int addConditions(String[] parts, int i) {
		while (!"then".equals(parts[i].trim())) {
			String part = parts[i].trim().replaceAll("\\s{2,}", " ");
			String variableName = getFirstWord(part);
			Variable variable = new VariableImpl(variableName);
			String state = stripFirstWord(part);
			rule.addCondition(variable, state);
			i++;
		}
		return i;
	}

	private void addActions(String[] parts, int i) {
		while (i < parts.length) {
			String part = parts[i].trim().replaceAll("\\s{2,}", " ");
			String variableName = getFirstWord(stripFirstWord(part)); // "set ABC to YYY"
			Variable variable = new VariableImpl(variableName);
			String state = stripFirstWord(stripFirstWord(part));
			rule.addAction(variable, state);
			i++;
		}
	}

	private String getFirstWord(String part) {
		int index = 0;
		while (Character.isJavaIdentifierPart(part.charAt(index))) {
			index++;
		}
		return part.substring(0, index);
	}
	
	private String stripFirstWord(String part) {
		int index = 0;
		while (Character.isJavaIdentifierPart(part.charAt(index))) {
			index++;
		}
		while (Character.isWhitespace(part.charAt(index))) {
			index++;
		}
		return part.substring(index);
	}

}
