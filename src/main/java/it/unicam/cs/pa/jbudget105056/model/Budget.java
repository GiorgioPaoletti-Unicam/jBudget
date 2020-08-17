package it.unicam.cs.pa.jbudget105056.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Ha la responsabilita di rappresentare e gestire un particolare budget.
 * Ogni budget associa ad ogni tag un importo che indica l’ammontare di spesa/guadagno per il particolare tag.
 * Ogni budget, inoltre, costruisce il predicato per selezionare i movimenti di interesse.
 * E responsabilita delle sottoclassi definire i criteri per la selezione dei movimenti.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Budget {

    List<Tag> tags();

    void set(Tag t, double expected);

    double get(Tag t);

    Predicate<? super Transaction> getPredicate();

    Map<Tag, Double> getBudgets();

    void removeBudget(Tag t);

}
