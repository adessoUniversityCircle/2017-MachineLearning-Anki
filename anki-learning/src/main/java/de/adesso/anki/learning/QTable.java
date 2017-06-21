package de.adesso.anki.learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public class QTable<S extends State, A extends Action> implements QFunction<S, A> {

	protected Map<S, Map<A, Double>> map;

	public QTable() {
		this.map = new HashMap<>();
	}

	public int size() {
		if (map.isEmpty())
			return 0;

		int size = 0;
		for (Map<A, Double> m : map.values())
			size += m.size();

		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean containsKey(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.containsKey(action) : false;
	}

	@Override
	public double get(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		if (tmp == null)
			return 0;

		Double weight = tmp.get(action);
		if (weight == null)
			return 0;

		return weight;
	}

	@Override
	public void put(S state, A action, double weight) {
		Map<A, Double> tmp = map.get(state);

		if (tmp == null)
			map.put(state, (tmp = new HashMap<>()));

		tmp.put(action, weight);
	}

	public void remove(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		if (tmp != null)
			tmp.remove(action);
	}

	public void clear() {
		map.clear();
	}

	public Collection<Double> values(S state) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.values() : new ArrayList<>();
	}

	public Set<Entry<A, Double>> entrySet(S state) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.entrySet() : new HashSet<>();
	}

	@Override
	public double getMaxWeight(S state) {
		if (!map.containsKey(state))
			return 0;

		double maxWeight = -Double.MAX_VALUE;
		for (double weight : values(state)) {
			if (weight > maxWeight)
				maxWeight = weight;
		}

		return maxWeight;
	}

	@Override
	public A getBestAction(S state) {
		if (!map.containsKey(state))
			return null;

		double maxWeight = -Double.MAX_VALUE;
		A bestAction = null;

		for (Entry<A, Double> e : entrySet(state)) {
			if (e.getValue() > maxWeight) {
				bestAction = e.getKey();
				maxWeight = e.getValue();
			}
		}

		return bestAction;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		String state;
		int i = 0;

		for (Entry<S, Map<A, Double>> s : map.entrySet()) {
			state = s.getKey().toString();

			for (Entry<A, Double> a : s.getValue().entrySet()) {
				sb.append(state);
				sb.append(a.getKey());
				sb.append('=');
				sb.append(String.format(Locale.ENGLISH, "%.2f", a.getValue()));

				if (++i < size())
					sb.append(',').append(' ');
			}
		}

		sb.append('}');
		return sb.toString();
	}

}
