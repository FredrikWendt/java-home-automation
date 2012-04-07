package se.wendt.rules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.rules.HomeVariable;
import se.wendt.home.rules.LampAction;
import se.wendt.home.rules.TimeVariable;
import se.wendt.home.rules.WeekdayVariable;
import se.wendt.home.tellstick.tdtool.TellstickCommandPublisher;
import se.wendt.rules.ActionMachine;
import se.wendt.rules.Rule;
import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;
import se.wendt.rules.VariableChangeEventListener;

public class Environment implements StateMachine, ActionMachine {

	private Map<Variable, String> states = new HashMap<Variable, String>();
	private Map<Variable, Collection<VariableChangeEventListener>> listeners = new HashMap<Variable, Collection<VariableChangeEventListener>>();
	private Collection<Rule> rules = new ArrayList<Rule>();

	public static void main(String[] args) throws InterruptedException {
		Environment environment = new Environment();
		LampAction lampAction = new LampAction(new TellstickCommandPublisher(new JmsUtils()));
		
		// wiring
		HomeVariable homeVariable = new HomeVariable(environment);
		new TimeVariable(environment).start();
		new WeekdayVariable(environment).start();
		environment.addVariableChangeEventListener(new VariableImpl("lamp"), lampAction);
		
		// go
		environment.addRule("rule home_on \n when \n home is not empty \n then \n turn lamp 4 on");
		environment.addRule("rule home_off \n when \n home is empty \n then \n turn lamp 4 off");
		homeVariable.personCame("anyone");
		Thread.sleep(4000);
		homeVariable.personLeft("anyone");
	}

	
	public void addRule(String ruleDefinition) {
		Rule rule = new RuleImpl(ruleDefinition, this);
		rules.add(rule);
	}

	@Override
	public void change(Variable variable, String newState) {
		String currentState = states.get(variable);
		if (currentState == null || !currentState.equals(newState)) {
			states.put(variable, newState);
		}

		// fire event
		VariableChangeEvent event = new VariableChangeEventImpl(variable, newState);
		fire(event);

		// fire rules
		for (Rule rule : rules) {
			rule.process(this);
		}
	}

	public void addVariableChangeEventListener(Variable variable, VariableChangeEventListener listener) {
		if (!listeners.containsKey(variable)) {
			listeners.put(variable, new ArrayList<VariableChangeEventListener>());
		}
		listeners.get(variable).add(listener);
	}

	@Override
	public void fire(VariableChangeEvent event) {
		Variable variable = event.getVariable();
		if (listeners.containsKey(variable)) {
			for (VariableChangeEventListener listener : listeners.get(variable)) {
				listener.stateChanged(event);
			}
		}
	}

	@Override
	public String getCurrentValue(Variable variable) {
		return states.get(variable);
	}


	public void addRules(Iterable<String> parseRulesFile) {
		for (String rule : parseRulesFile) {
			addRule(rule);
		}
	}

}
