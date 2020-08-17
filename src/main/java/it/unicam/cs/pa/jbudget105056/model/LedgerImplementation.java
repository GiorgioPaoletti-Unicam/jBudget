package it.unicam.cs.pa.jbudget105056.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementa l'interfaccia Ledger
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class LedgerImplementation implements Ledger{

    List<Account> accounts;
    List<Transaction> transactions;
    List<Tag> tags;
    List<ScheduledTransaction> scheduledTransactions;

    public LedgerImplementation() {
        transactions = new ArrayList<>();
        accounts = new ArrayList<>();
        tags = new ArrayList<>();
        scheduledTransactions = new ArrayList<>();
    }

    /**
     *
     * @return                          lista dei conti legata al ledger
     */
    @Override
    public List<Account> getAccounts() { return accounts; }

    /**
     *
     * @param t                         riferimento della transazione da aggiungere alla lista delle transazioni
     * @throws IllegalStateException    se la lista dei movimenti legata alla transazione e vuota
     *                                  se la lista dei tag della transazione e vuota
     *                                  se un moviemento e collegato a un account non presente nella lista degli account del ledger
     *                                  se la lista dei tag contien un tag non presente nella lista dei tag del ledger
     * @throws IllegalArgumentException se la transazione e gia presente nella lista
     */
    @Override
    public synchronized void addTransaction(Transaction t) throws  IllegalStateException, IllegalArgumentException{
        if(transactions.contains(Objects.requireNonNull(t, "Transaction is null")))
            throw new IllegalArgumentException("Transaction already exist");
        transactions.add(checkTransaction(t));
    }

    private Transaction checkTransaction(Transaction t) throws IllegalStateException {
        if(t.movements().isEmpty()) throw new IllegalStateException("Movement list is empty");
        if(t.tags().isEmpty()) throw new IllegalStateException("Tag list is empty");

        for(Movement m : t.movements())
            if(!accounts.contains(Objects.requireNonNull(m.getAccount(),
                    "Transaction contains a Movement with a null Account")))
                throw new IllegalStateException(
                        "Transaction contains a Movement with an Account not contains in the Account List");
        for(Tag tag : t.tags())
            if(!tags.contains(tag))
                throw new IllegalStateException("Transaction contains a Tag not contains in the Account List");
        return t;
    }

    /**
     *
     * @return lista delle transazioni legata al ledger
     */
    @Override
    public List<Transaction> getTransactions() { return transactions; }

    /**
     *
     * @param p                         predicato da applicare alla lista delle transazioni
     * @return                          lista su cui e stato applicato il predicato
     */
    @Override
    public List<Transaction> getTransactions(Predicate<? super Transaction> p) {
        return getTransactions().stream().parallel().filter(
                Objects.requireNonNull(p, "Predicate is null")).collect(Collectors.toList());
    }

    /**
     *
     * @return lista dei tag legata al ledger
     */
    @Override
    public List<Tag> getTags() { return tags; }

    /**
     *
     * @param type                      tipo del conto
     * @param name                      nome del conto
     * @param description               descrizione cel conto
     * @param opening                   bilancio iniziale del conto
     * @return                          conto aggiunto alla lista
     * @throws IllegalArgumentException se il conto e gia presente nella lista
     */
    @Override
    public synchronized Account addAccount(AccountType type, String name, String description, double opening)
            throws IllegalArgumentException{
        Account a = AccountImplementation.getInstance(type, name, description, opening);
        if(accounts.contains(a)) throw new IllegalArgumentException("Account already exist");
        accounts.add(a);
        return a;
    }

    /**
     *
     * @param name                      nome del tag
     * @param description               descrione del tag
     * @return                          tag aggiunto nella lista
     * @throws IllegalArgumentException se il tag e gia presente nella lista
     */
    @Override
    public synchronized Tag addTag(String name, String description) throws IllegalArgumentException {
        Tag t = TagImplementation.getInstance(name, description);
        if(tags.contains(t)) throw new IllegalArgumentException("Tag already exist");
        tags.add(t);
        return t;
    }

    /**
     *
     * @param st    riferimento alla transazione programmata da aggiungere alla lista
     */
    @Override
    public synchronized void addScheduledTransaction(ScheduledTransaction st) {
        if(scheduledTransactions.contains(Objects.requireNonNull(st, "Null ScheduledTransaction")))
            throw new IllegalArgumentException("ScheduledTransaction already exist");
        if(!accounts.contains(st.getAccount())) throw new IllegalStateException("Scheduled Transaction contains an Account that not exist");
        if(st.getTransactions().isEmpty()) throw new IllegalStateException("Scheduled transaction is Empty");
        scheduledTransactions.add(st);
    }

    /**
     *
     * @throws IllegalStateException se la lisat delle transazioni programmate e vuota
     */
    @Override
    public synchronized List<Transaction> schedule() throws IllegalStateException{
        List<Transaction> scheduledTrans = new ArrayList<>();
        for(ScheduledTransaction st: getScheduledTransactions())
            if(st != null) {
                List<Transaction> transactionList = st.getTransactions(t -> t.getDate().before(new Date()));
                for (Transaction t : transactionList) {
                    for (Movement m : t.movements())
                        if (!st.getAccount().getMovements().contains(m)) st.getAccount().addMovement(m);
                    if(!tags.contains(TagImplementation.getInstance("Scheduled Transaction", "Transazione Programmata")))
                        addTag("Scheduled Transaction", "Transazione Programmata");
                    if (!transactions.contains(t)) {
                        scheduledTrans.add(t);
                        addTransaction(t);
                    }

                }
            }
        return scheduledTrans;
    }

    /**
     *
     * @return                          lista delle transazioni programmate
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        return scheduledTransactions;
    }

    /**
     *
     * @param   p predicato da applicare alla lista delle transazioni programmate
     * @return  lista a cui e stato applicato il predicato
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions(Predicate<? super ScheduledTransaction> p) {
        return getScheduledTransactions().stream().filter(p).collect(Collectors.toList());
    }

    /**
     *
     * @param st                        transazione programmata da eliminare dalla lista
     * @throws IllegalArgumentException se la transazione programmata non esiste
     */
    @Override
    public synchronized void removeScheduledTransactions(ScheduledTransaction st) throws IllegalArgumentException{
        if(!scheduledTransactions.contains(Objects.requireNonNull(st, "Scheduled Transaction is Null")))
            throw new IllegalArgumentException("Scheduled transaction not Exist");
        for(Transaction t : st.getTransactions())
            if(transactions.contains(t)) removeTransaction(t);
        scheduledTransactions.remove(st);
    }

    /**
     *
     * @param t                         riferiemento alla tag da rimuove dalla lista dei tag
     * @throws IllegalArgumentException se il tag non e presente nella lista
     * @throws IllegalStateException    se la lista e vuota
     */
    @Override
    public synchronized void removeTag(Tag t) throws IllegalArgumentException, IllegalStateException {
        if(!(getTags().contains(Objects.requireNonNull(t, "Null Tag"))))
            throw new IllegalArgumentException("Tag not exist");
        if(tags.isEmpty()) throw new IllegalStateException("Tag list is empty");
        //TODO Cosa succede quando elimino un tag?
        tags.remove(t);
    }

    /**
     * Rimozione di una transazione che comporta la rimozione di tutti movimenti legati ad essa e
     * di conseguenza la rimozione dei movimenti dai relativi conti
     *
     * @param t                         riferiemento alla transazione da rimuovere dalla lista
     * @throws IllegalArgumentException se la transazione non e presente nella lista
     * @throws IllegalStateException    se la lista e vuota
     */
    @Override
    public synchronized void removeTransaction(Transaction t) throws IllegalArgumentException, IllegalStateException {
        if(transactions.isEmpty()) throw new IllegalStateException("Transactions list is empty");
        if(!getTransactions().contains(Objects.requireNonNull(t, "Transaction is Null")))
            throw new IllegalArgumentException("Transaction not Exist");
        for(Movement m : transactions.get(transactions.indexOf(t)).movements())
            getAccounts().get(getAccounts().indexOf(m.getAccount())).removeMovement(m);
        transactions.remove(t);
    }

    /**
     * Rimozione di un conto che comporta la rimozione di tutti i movimenti che gli sono collegati e
     * di conseguenza la rimozione di tutti movimenti dalle transazioni a cui sono legati
     * Se una Transazione rimane senza movimenti viene rimossa dalla lista delle transazioni
     *
     * @param a                         riferiemento al conto da rimuovere dalla lista
     * @throws IllegalArgumentException se il conto non e presente nella lista
     * @throws IllegalStateException    se la lista e vuota
     */
    @Override
    public synchronized void removeAccount(Account a) throws IllegalArgumentException, IllegalStateException {
        if(accounts.isEmpty()) throw new IllegalStateException("Accounts list is empty");
        if(!getAccounts().contains(Objects.requireNonNull(a, "Account is Null")))
            throw new IllegalArgumentException("Account not Exist");
        for(Movement m : a.getMovements()){
            for(Transaction t : getTransactions()) {
                if (t.movements().contains(m)) removeTransaction(t);
                if (t.movements().isEmpty()) removeTransaction(t);
            }
        }
        accounts.remove(a);
    }

    @Override
    public String toString() {
        return "Ledger {" +
                "accounts: " + listToString(accounts) +
                ", transactions: " + listToString(transactions) +
                ", tags: " + listToString(tags) +
                ", scheduledTransactions: " + listToString(scheduledTransactions) +
                '}';
    }

    private <T> String listToString(List<T> list){
        StringBuilder string = new StringBuilder();
        for(T t : list) string.append(t.toString()).append("\n");
        return string.toString();
    }
}
