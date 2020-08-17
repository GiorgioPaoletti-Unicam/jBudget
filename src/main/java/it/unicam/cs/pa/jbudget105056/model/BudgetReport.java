package it.unicam.cs.pa.jbudget105056.model;

import java.util.List;
import java.util.Map;

/**
 * ha la responsabilita di mostrare gli scostamenti di spesa/guadagno rispetto ad un particolare budget.
 * Il BudgetReport viene costruito da un BudgetManager.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface BudgetReport {

    List<Tag> tags();

    Map<Tag, Double> report();

    Budget getBudget();

    double get(Tag t);

}
