package de.adesso.anki.console.learning;

import java.util.Random;

import org.springframework.stereotype.Component;

import de.adesso.anki.learning.QLearning;
import de.adesso.anki.learning.QTable;

@Component
public class AnkiQLearning extends QLearning<AnkiState, AnkiAction, QTable<AnkiState, AnkiAction>> {

	protected static final Random RD = new Random(System.currentTimeMillis());
	protected double exploreRate = 0.33;
	protected double learningRate = 0.1;

	public AnkiQLearning() {
		super(new QTable<>());
		setExploreRate(0.33);
	}

	@Override
	public AnkiAction explore(AnkiState state) {
		AnkiAction action = qFunction.getBestAction(state);
		if (action == null)
			action = AnkiAction.DEFAULT_ACTION;

		return new AnkiAction(randomSpeed(action.getSpeed()), randomOffset(action.getOffset()));
	}

	@Override
	public AnkiAction exploit(AnkiState state) {
		AnkiAction action = qFunction.getBestAction(state);
		if (action != null)
			return action;

		return new AnkiAction(randomSpeed(AnkiAction.DEFAULT_ACTION.getSpeed()), 0);
	}

	protected int randomSpeed(int around) {
		return Math.min(Math.max(around + (RD.nextInt(5) - 1) * 50, 0), 5000);
	}

	protected double randomOffset(double around) {
		return Math.min(Math.max(around + (RD.nextInt(3) - 1) * 27, -40.5), 40.5);
	}

	@Override
	protected double getLearningRate() {
		return learningRate;
	}

	@Override
	protected double getDiscountRate() {
		return 0.9;
	}

	@Override
	protected double getExploreRate() {
		return exploreRate;
	}

	public void setExploreRate(double exploreRate) {
		this.exploreRate = exploreRate;
	}
	
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

}
