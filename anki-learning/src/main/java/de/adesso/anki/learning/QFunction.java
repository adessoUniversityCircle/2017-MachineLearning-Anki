package de.adesso.anki.learning;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface QFunction<S extends State, A extends Action> {

	double get(S state, A action);

	void put(S state, A action, double weight);

	/**
	 * Determines the highest currently learned weight belonging to the given state.
	 * 
	 * @param state
	 * @return the highest currently learned weight
	 */
	double getMaxWeight(S state);

	/**
	 * Determines the action with the highest currently learned weight belonging to the given state.
	 * 
	 * @param state
	 * @return the action with the highest currently learned weight
	 */
	A getBestAction(S state);

}
