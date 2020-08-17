package it.unicam.cs.pa.jbudget105056.model;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * indica una transazione o una serie di transazioni schedulate ad una certa data.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface  ScheduledTransaction {

    int getID();

    String getDescription();

    List<Transaction> getTransactions();

    List<Transaction> getTransactions(Predicate<? super Transaction> p);

    boolean isCompleted();

    void setDescription(String d);

    void setTransactionsList(List<Transaction> list);

    void generateTransactions(int nTrasaction, int intervalloGiorniPagamento, Date firstDate, Double totalAmount);

    Account getAccount();

}
