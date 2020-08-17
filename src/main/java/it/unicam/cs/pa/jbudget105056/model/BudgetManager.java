package it.unicam.cs.pa.jbudget105056.model;

/**
 * ha la responsabilita di costruire il BudgetReport associato ad un Budget e ad un Ledger.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface BudgetManager {

    BudgetReport generateReport(Ledger l, Budget b);

}
