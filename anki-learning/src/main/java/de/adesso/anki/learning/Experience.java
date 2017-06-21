package de.adesso.anki.learning;

import java.util.Locale;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Experience<S extends State, A extends Action> {

	protected S state;
	protected A action;
	protected double reward;
	protected S nextState;

	public Experience(S state, A action, double reward, S nextState) {
		this.state = state;
		this.action = action;
		this.reward = reward;
		this.nextState = nextState;
	}

	protected Experience() {}

	public S getState() {
		return state;
	}

	public A getAction() {
		return action;
	}

	public double getReward() {
		return reward;
	}

	public S getNextState() {
		return nextState;
	}

	@Override
	public String toString() {
		return "(" + state + "," + action + "," + String.format(Locale.ENGLISH, "%.0f", reward) + "," + nextState + ")";
	}

}
