package it.unicam.cs.pa.jbudget105056.model;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * questa interfaccia e implementata dalle classi che hanno la responsabilita di gestire tutti i dati dell’applicazione.
 * E responsabile della creazione dei conti, dell’aggiunta e cancellazione delle transazioni, della creazione e cancellazione dei tag.
 * Inoltre, mantiene la lista delle transazioni schedulate.
 * Si occupa di schedulare le transazioni ad una certa data.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Ledger {

    List<Account> getAccounts();

    void addTransaction(Transaction t);

    List<Transaction> getTransactions();

    List<Transaction> getTransactions(Predicate<? super Transaction> p);

    List<Tag> getTags();

    Account addAccount(AccountType type, String name, String description, double opening);

    Tag addTag(String name, String description);

    void addScheduledTransaction(ScheduledTransaction st);

    List<Transaction> schedule();

    List<ScheduledTransaction> getScheduledTransactions();

    List<ScheduledTransaction> getScheduledTransactions(Predicate<? super ScheduledTransaction> p);

    void removeScheduledTransactions(ScheduledTransaction st);

    void removeTag(Tag t);

    void removeTransaction(Transaction t);

    void removeAccount(Account a);

}
