package de.adesso.anki.learning;

import java.util.Objects;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class QLearning<S extends State, A extends Action, Q extends QFunction<S, A>> {

	protected final Q qFunction;

	public QLearning(Q qFunction) {
		this.qFunction = Objects.requireNonNull(qFunction, "q-function must not be null");
	}

	public A applyPolicy(S state) {
		if (Math.random() <= getExploreRate())
			return explore(state);

		return exploit(state);
	}

	public abstract A explore(S state);

	public abstract A exploit(S state);

	/**
	 * Calculates and updates the weight of an executed action.
	 * 
	 * @param state
	 * @param action
	 * @param reward
	 * @param nextState
	 */
	public Experience<S, A> update(S state, A action, double reward, S nextState) {
		double old = qFunction.get(state, action);
		double max = (nextState != null && nextState.isFinal()) ? qFunction.getMaxWeight(nextState) : 0;

		double alpha = getLearningRate();
		double gamma = getDiscountRate();

		double weight = (1 - alpha) * old + alpha * (reward + gamma * max);
		qFunction.put(state, action, weight);
		return new Experience<>(state, action, reward, nextState);
	}

	/**
	 * The learning rate alpha is between 0 and 1 and determines the impact of a new reward.
	 * 
	 * @return the learning rate alpha
	 */
	protected abstract double getLearningRate();

	/**
	 * The discount rate gamma is between 0 and 1 and determines the impact of future actions.
	 * 
	 * @return the discount rate gamma
	 */
	protected abstract double getDiscountRate();

	/**
	 * The explore rate epsilon is between 0 and 1 and determines probability of choosing a random action.
	 * 
	 * @return the explore rate epsilon
	 */
	protected abstract double getExploreRate();

	public Q getQFunction() {
		return qFunction;
	}
}
