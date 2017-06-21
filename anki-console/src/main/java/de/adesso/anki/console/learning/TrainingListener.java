package de.adesso.anki.console.learning;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.LaneTask;
import de.adesso.anki.console.tasks.SpeedTask;
import de.adesso.anki.console.tasks.StopTask;
import de.adesso.anki.learning.Experience;
import de.adesso.anki.messages.SegmentTimeMessage;
import de.adesso.anki.roadmap.segments.SegmentType;
import de.adesso.anki.sdk.messages.MessageListener;

@Component
@Scope("prototype")
public class TrainingListener implements MessageListener<SegmentTimeMessage> {

	private boolean active;
	private Vehicle car;

	@Autowired
	private AnkiQLearning learning;

	@Autowired
	private SpeedTask speed;

	@Autowired
	private LaneTask lane;

	@Autowired
	private StopTask stop;

	private AnkiState state;
	private AnkiAction action;
	private List<Experience<AnkiState, AnkiAction>> lesson = new LinkedList<>();

	public TrainingListener(Vehicle car) {
		this.car = Objects.requireNonNull(car, "vehicle must not be null");
	}

	private void replay() {
		if (!lesson.isEmpty()) {
			Experience<AnkiState, AnkiAction> exp;
			for (int e = lesson.size() - 1; e >= 0; e--) {
				exp = lesson.get(e);
				learning.update(exp.getState(), exp.getAction(), exp.getReward(), exp.getNextState());
			}

			lesson.clear();
		}
	}

	@Override
	public void messageReceived(SegmentTimeMessage message) {

		if (message.getType() == SegmentType.START)
			active = true;

		if (active) {
			AnkiState state = new AnkiState(message.getSegmentPosInMap());
			AnkiAction action = learning.applyPolicy(state);

			if (action.getOffset() == 0)
				action = new AnkiAction(action.getSpeed(), car.getDesiredOffset());

			System.out.println(state);
			System.out.println(action);

			speed.setSpeed(car, action.getSpeed());
			lane.setOffset(car, action.getOffset());

			if (this.state != null && this.action != null)
				lesson.add(new Experience<>(this.state, this.action, -message.getLastMeasure(), state));

			this.state = state;
			this.action = action;
		}

		if (message.getType() == SegmentType.START)
			replay();
	}

	public void fail() {
		stop.run(car);
		lesson.add(new Experience<>(this.state, this.action, -3000, null));
		replay();
		active = false;
	}

	public void cancel() {
		stop.run(car);
		lesson.clear();
		active = false;
	}

	public void setAlpha(double alpha) {
		learning.setLearningRate(alpha);
	}

	public void setEpsilon(double epsilon) {
		learning.setExploreRate(epsilon);
	}

}
