package de.adesso.anki.learning;

import java.util.Random;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> State
 * @param <A> Action
 * @param <Q> QFunction
 */
public abstract class EpsilonGreedyPolicy<S extends State, A extends Action, Q extends QFunction<S, A>> implements Policy<S, A> {

	protected static final Random RD = new Random(System.currentTimeMillis());
	protected final Q qFunction;
	protected double epsilon;

	public EpsilonGreedyPolicy(Q qFunction) {
		this.qFunction = qFunction;
	}

	@Override
	public A nextAction(S state) {
		if (RD.nextDouble() <= epsilon)
			return explore(state);

		return qFunction.getBestAction(state);
	}

	protected abstract A explore(S state);

	protected double getExploreRate() {
		return epsilon;
	}

}
