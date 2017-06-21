package de.adesso.anki.learning;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> State
 * @param <A> Action
 * @param <Q> QFunction
 */
@FunctionalInterface
public interface Policy<S extends State, A extends Action> {

	A nextAction(S state);

}
