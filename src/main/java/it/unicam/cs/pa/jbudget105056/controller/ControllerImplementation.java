package it.unicam.cs.pa.jbudget105056.controller;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Questa classe implementa linterfaccia Controller
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class ControllerImplementation implements  Controller{

    private Ledger ledger;
    private Budget budget;
    private LoggerManager loggerManager;
    private Saver saver;
    private Loader loader;

    /**
     *
     * @param ledger        Il ledger che gestisce i dati dell'applicazione
     * @param budget        Il budget che gestisce i budget dell'applicazione
     * @param loggerManager Il logger manager che gestisce il savataggio dei log
     * @param saver         Il saver che gestisce il savataggio dei dati collegati al ledger e budget
     * @param loader        Il loader che gestisce il caricamento dei dati collegati al ledger e al budget
     */
    @Inject
    public ControllerImplementation(@LedgerInject Ledger ledger, @BudgetInject Budget budget,
                                    @LoggerManagerInject LoggerManager loggerManager,
                                    @SaverInject Saver saver, @LoaderInject Loader loader) {
        this.ledger = ledger;
        this.budget = budget;
        this.loggerManager = loggerManager;
        this.saver = saver;
        this.loader = loader;
    }

    /**
     * Si occupa di salvare i dati dell'applicazione
     */
    @Override
    public void loadData() {
        try{ ledger = loader.loadLedger(); }
        catch (Exception e) {
            loggerManager.setMessage(Level.WARNING, "Ledger.json not found");
            ledger = new LedgerImplementation();
            loggerManager.setMessage(Level.INFO, "Created a new Ledger");
        }
        try{ budget = loader.loadBudget(); }
        catch (Exception e) {
            loggerManager.setMessage(Level.WARNING, "Budget.json not found");
            budget = new BudgetImplementation();
            loggerManager.setMessage(Level.INFO, "Created a new Budget");
        }
        saveData();
    }

    /**
     * Si occupa si caricare i dati dell'applicazione
     */
    @Override
    public void saveData() {
        saver.saveLedger(ledger);
        saver.saveBudget(budget);
    }

    /**
     *
     * @return Ritorna la lista delle transazioni legata al Ledger
     */
    @Override
    public List<Transaction> getTransactions() { return ledger.getTransactions(); }

    /**
     *
     * @param p predicato da applicare alla lista delle transazioni
     * @return  lista delle transazioni a cui e stato applicato il predicato
     */
    @Override
    public List<Transaction> getTransactions(Predicate<? super Transaction> p) { return ledger.getTransactions(p); }

    /**
     *
     * @param conto                     conto a cui appartiene la spesa o entrata
     * @param type                      tipo della transazione da cui dipende se e un entrata o una spesa
     * @param amount                    importo della transazione
     * @param tags                      lista dei tag legata alla transazione
     * @param date                      data della transazione
     * @param description               descrione della transazione
     * @throws IllegalArgumentException se la transazione e gia presente
     */
    @Override
    public synchronized void addTransaction(Account conto, MovementType type, double amount, List<Tag> tags, Date date, String description)
            throws IllegalArgumentException{
        if(!ledger.getAccounts().contains(conto)) throw new IllegalArgumentException("Account not exist");
        Movement m = MovementImplementation.getInstance(description, amount, type);
        Transaction t = TransactionImplementation.getInstance(date);
        for(Tag tag : tags) t.addTag(tag);
        t.addMovement(m);
        ledger.getAccounts().get(ledger.getAccounts().indexOf(conto)).addMovement(m);
        ledger.addTransaction(t);
        loggerManager.setMessage(Level.INFO, "Transazione Aggiunta: " + t.toString());

    }

    /**
     *
     * @param dalConto                  riferiemnto al Conto da cui viene prelevato l'importo della transazione
     * @param alConto                   riferiemnto al Conto a cui viene accreditato l'importo della transazione
     * @param amount                    importo della transazione
     * @param description               descrione della transazione
     * @param commisione                commisione del trasferimento
     * @param tags                      tag collegati alla transazione
     * @param date                      data della transazione
     * @throws IllegalArgumentException se la transazione e gia presente
     */
    @Override
    public synchronized void addTrasferimento(Account dalConto, Account alConto, double amount, String description,
                                 double commisione, List<Tag> tags, Date date)
            throws IllegalStateException, IllegalArgumentException{
        if(!ledger.getAccounts().contains(dalConto)) throw new IllegalArgumentException("dalConto not exist");
        if(!ledger.getAccounts().contains(dalConto)) throw new IllegalArgumentException("alConto not exist");
        if(dalConto.equals(alConto)) throw new IllegalArgumentException("dalConto and alConto are Equal");
        Movement m1 = MovementImplementation.getInstance(description, amount, MovementType.CREDITS);
        Movement m2 = MovementImplementation.getInstance(description, amount, MovementType.DEBIT);
        Movement m3 = MovementImplementation.getInstance(description, commisione, MovementType.DEBIT);
        Transaction t = TransactionImplementation.getInstance(date);
        for(Tag tag : tags) t.addTag(tag);
        t.addMovement(m1);
        t.addMovement(m2);
        t.addMovement(m3);
        ledger.getAccounts().get(ledger.getAccounts().indexOf(dalConto)).addMovement(m2);
        ledger.getAccounts().get(ledger.getAccounts().indexOf(dalConto)).addMovement(m3);
        ledger.getAccounts().get(ledger.getAccounts().indexOf(alConto)).addMovement(m1);
        ledger.addTransaction(t);
        loggerManager.setMessage(Level.INFO, "Trasferimento Aggiunto: " + t.toString());
    }

    /**
     *
     * @param t                         riferiemento alla transazione da rimuovere dalla lista
     * @throws IllegalArgumentException se la transazione non e presente nella lista
     * @throws IllegalStateException    se la lista e vuota
     */
    @Override
    public synchronized void removeTransaction(Transaction t) throws IllegalArgumentException, IllegalStateException {
        ledger.removeTransaction(t);
        loggerManager.setMessage(Level.INFO, "Transazione rimossa: " + t.toString());
    }

    /**
     *
     * @param t                         transazione di cui si vuole conoscere il saldo
     * @return                          saldo delle transazione passata per argomento
     * @throws IllegalStateException    lista dei moviementi vuota
     */
    @Override
    public double getTotalAmount(Transaction t) throws IllegalStateException {
        return ledger.getTransactions().get(ledger.getTransactions().indexOf(t)).getTotalAmount();
    }

    /**
     *
     * @return lista delle transazioni programmate
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() { return ledger.getScheduledTransactions(); }

    /**
     *
     * @param p predicato da applicare alla lista delle transazioni programmate
     * @return  lista su cui e stato applicato il predicato
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions(Predicate<? super ScheduledTransaction> p) {
        return ledger.getScheduledTransactions(p);
    }

    /**
     *
     * @param description               descrizione transazione programmata
     * @param account                   account legato alla transazione programmata
     * @param nTrasaction               numero transazioni che compongono la transazione programmata
     * @param intervalloGiorniPagamento intervallo di giorni tra le tansazioni della lista
     * @param firstDate                 prima data della transazione della lista delle transazioni
     * @param totalAmount               amount totale della transazione programmata
     * @throws IllegalArgumentException se i parametri 'asati presentano dei problemi
     */
    @Override
    public synchronized void addScheduledTransaction(String description, Account account, int nTrasaction,
                                        int intervalloGiorniPagamento, Date firstDate, Double totalAmount)
            throws IllegalArgumentException{
        ScheduledTransaction st = ScheduledTransactionImplementation.getInstance(description, account);
        st.generateTransactions(nTrasaction, intervalloGiorniPagamento, firstDate, totalAmount);
        ledger.addScheduledTransaction(st);
        loggerManager.setMessage(Level.INFO, "Transazione Programmata Aggiunta: " + st.toString());
    }

    /**
     *
     * @param st                        transazione programmata da eliminare
     * @throws IllegalArgumentException se la transazione programmata non e presente nella lista
     * @throws IllegalStateException    se la lista delle transazioi programmate e vuota
     */
    @Override
    public synchronized void removeScheduledTransaction(ScheduledTransaction st) throws IllegalArgumentException, IllegalStateException {
        ledger.removeScheduledTransactions(st);
        loggerManager.setMessage(Level.INFO, "Transazione Programmata Rimossa: " + st.toString());
    }

    /**
     *
     * @throws IllegalStateException se la lista delle transazioi programmate e vuota
     */
    @Override
    public synchronized void schedule() throws IllegalStateException {
        List<Transaction> transactions = ledger.schedule();
        if(!transactions.isEmpty())
            loggerManager.setMessage(Level.INFO, "Transazioni Schedulate: " + transactions.toString());
    }

    /**
     *
     * @return                          Budget legato all'applicazione
     */
    @Override
    public Map<Tag, Double> getBudgets() { return budget.getBudgets();}

    /**
     *
     * @param t                         tag del budget da aggiungere
     * @param expect                    aspetattiva del budget da aggiungere
     * @throws IllegalArgumentException se il budget e gia presente nella lista
     */
    @Override
    public synchronized void addBudget(Tag t, double expect) throws IllegalArgumentException {
        budget.set(t,expect);
        loggerManager.setMessage(Level.INFO, "Budget Aggiunto: " + t.toString() + ": " + expect);
    }

    /**
     *
     * @param t                         riferiemnto al budget da rimuovere
     * @throws IllegalArgumentException se il budget non e presente
     * @throws IllegalStateException se la lista e vuota
     */
    @Override
    public synchronized void removeBudget(Tag t) throws IllegalStateException, IllegalArgumentException {
        budget.removeBudget(t);
        loggerManager.setMessage(Level.INFO, "Budget Rimosso: " + t.toString());
    }

    /**
     *
     * @return                          report legato al ledger e budget dell'applicazione
     * @throws IllegalStateException    se la lista delle transazioni del ledger e vuota o se la lista dei budget e vuota
     */
    @Override
    public BudgetReport getReportBudgets()
            throws IllegalStateException {return new BudgetManagerImplementation().generateReport(ledger, budget);}

    /**
     *
     * @return lista conti legati al ledger
     */
    @Override
    public List<Account> getAccounts() { return ledger.getAccounts();}

    /**
     *
     * @param p predicato da applicare alla lista dei conti
     * @return  lista dei conti a cui e stato applicato il predicato
     */
    @Override
    public List<Account> getAccounts(Predicate<? super Account> p) {
        return ledger.getAccounts().stream().parallel().filter(p).collect(Collectors.toList());
    }

    /**
     *
     * @param a                         riferimento al conti di cui si vuole ottenere il bilancio attuale
     * @return                          bilancio del conto passato per paramento
     * @throws IllegalStateException    se il conto non contiene moviementi
     */
    @Override
    public double getBalance(Account a) throws IllegalStateException {
        return ledger.getAccounts().get(ledger.getAccounts().indexOf(a)).getBalance();
    }

    /**
     *
     * @param name                      nome del conto
     * @param description               descrizione del conto
     * @param opening                   bilancio iniziale del conto
     * @param type                      tipo del conto
     * @throws IllegalArgumentException se il conto e gia presente
     */
    @Override
    public synchronized void addAccount(String name, String description, double opening, AccountType type)
            throws IllegalArgumentException {
        ledger.addAccount(type, name, description, opening);
        loggerManager.setMessage(Level.INFO, "Account Aggiunto: " + AccountImplementation.getInstance(type, name, description, opening).toString());
    }

    /**
     *
     * @param a                          riferiemento al conto da rimuovere
     * @throws IllegalArgumentException  se il conto non e presente
     * @throws IllegalStateException     se la lista e vuota
     */
    @Override
    public synchronized void removeAccount(Account a) throws IllegalArgumentException, IllegalStateException {
        ledger.removeAccount(a);
        loggerManager.setMessage(Level.INFO, "Account Rimosso: " + a.toString());
    }

    /**
     *
     * @return                          lista dei tag legata al ledger
     */
    @Override
    public List<Tag> getTags() { return ledger.getTags();}

    /**
     *
     * @param p predicato da applicare alla lista dei conti
     * @return  lista dei conti a cui e stato applicato il predicato
     */
    @Override
    public List<Tag> getTags(Predicate<? super Tag> p) {
        return ledger.getTags().stream().filter(Objects.requireNonNull(p, "Predicate is null")).collect(Collectors.toList());
    }


    /**
     *
     * @param name                      nome della categoria
     * @param description               descrizione della categoria
     * @throws IllegalArgumentException se la categoria e gia presente
     */
    @Override
    public synchronized void addTag(String name, String description) throws IllegalArgumentException {
        ledger.addTag(name, description);
        loggerManager.setMessage(Level.INFO, "Tag Aggiunto: " + TagImplementation.getInstance(name, description).toString());
    }

    //TODO -> cosa succede se elimino un Tag?
    /*
    @Override
    public synchronized void removeTag(Tag t) {
        ledger.removeTag(t);
        loggerManager.setMessage(Level.INFO, "Tag Rimosso: " + t.toString());
    }

     */
}
