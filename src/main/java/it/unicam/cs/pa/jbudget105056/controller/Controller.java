package it.unicam.cs.pa.jbudget105056.controller;

import it.unicam.cs.pa.jbudget105056.model.*;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * questa interfaccia e implementata dalle classi che hanno la responsabilita di gestire il
 * processo delle funzionalita disponibili dell’applicazione con le classi che gestiscono il Model.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Controller {

    void loadData();

    void saveData();

    List<Transaction> getTransactions();

    List<Transaction> getTransactions(Predicate<? super Transaction> p);

    void addTransaction(Account conto, MovementType type, double amount, List<Tag> tags, Date date, String description);

    void addTrasferimento(Account dalConto, Account alConto, double amount, String description,
                          double commisione, List<Tag> tags, Date date);

    void removeTransaction(Transaction t);

    double getTotalAmount(Transaction t);

    List<ScheduledTransaction> getScheduledTransactions();

    List<ScheduledTransaction> getScheduledTransactions(Predicate<? super ScheduledTransaction> p);

    void addScheduledTransaction(String description, Account account, int nTrasaction,
                                 int intervalloGiorniPagamento, Date firstDate, Double totalAmount);

    void removeScheduledTransaction(ScheduledTransaction st);

    void schedule();

    Map<Tag, Double> getBudgets();

    void addBudget(Tag t, double expect);

    void removeBudget(Tag t);

    BudgetReport getReportBudgets();

    List<Account> getAccounts();

    List<Account> getAccounts(Predicate<? super Account> p);

    double getBalance(Account a);

    void addAccount(String name, String description, double opening, AccountType type);

    void removeAccount(Account a);

    List<Tag> getTags();

    List<Tag> getTags(Predicate<? super Tag> p);

    void addTag(String name, String description);

    //TODO -> cosa succede se rimuovo un tag?
    //void removeTag(Tag t);

}
