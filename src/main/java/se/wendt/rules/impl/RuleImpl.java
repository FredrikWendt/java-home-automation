package se.wendt.rules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import se.wendt.home.util.LogEnabledBase;
import se.wendt.rules.ActionMachine;
import se.wendt.rules.Rule;
import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;

public class RuleImpl extends LogEnabledBase implements Rule {

	private final ActionMachine actionHub;
	private String name;
	private Collection<VariableChangeEvent> eventsToFire = new ArrayList<VariableChangeEvent>();
	private Map<Variable, String> conditions = new HashMap<Variable, String>();
	private boolean ruleAppliedAtLastStateChange = false;

	public RuleImpl(String ruleDefinition, ActionMachine actionMachine) {
		this(actionMachine);
		new RuleParser(ruleDefinition, this);
	}

	/*
	 * For testing.
	 */
	RuleImpl(ActionMachine actionMachine) {
		this.actionHub = actionMachine;
	}

	@Override
	public void process(StateMachine states) {
		for (Entry<Variable, String> entry : conditions.entrySet()) {
			String currentValue = states.getCurrentValue(entry.getKey());
			if (currentValue != null && !currentValue.equals(entry.getValue())) {
				ruleNoLongerApplies();
				return;
			}
		}
		ruleAppliesToCurrentState();
	}

	// ---- used by parser _____________________________________________________

	void addCondition(Variable variable, String state) {
		conditions.put(variable, state);
	}

	void addAction(Variable variable, String state) {
		eventsToFire.add(new VariableChangeEventImpl(variable, state));
	}

	void setName(String name) {
		this.name = name;
	}

	// ---- helpers ____________________________________________________________

	protected void fireEvents() {
		for (VariableChangeEvent event : eventsToFire) {
			actionHub.fire(event);
		}
	}

	private void ruleNoLongerApplies() {
		if (ruleAppliedAtLastStateChange) {
			log("Rule " + name + " is no longer in effect");
		}
		ruleAppliedAtLastStateChange = false;
	}

	private void ruleAppliesToCurrentState() {
		if (ruleAppliedAtLastStateChange) {
			log("Rule " + name + " is still in effect");
		} else {
			ruleAppliedAtLastStateChange = true;
			log("Rule " + name + " is now in effect");
			fireEvents();
		}
	}

}
