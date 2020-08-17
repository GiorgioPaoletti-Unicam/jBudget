package it.unicam.cs.pa.jbudget105056.model;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * implementa l'interfaccia ScheduledTransaction
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class ScheduledTransactionImplementation implements ScheduledTransaction, ClassRegistryImplementation.ObjectWithGetId{

    private final int ID;
    private String description;
    private List<Transaction> transactions;
    private Account account;

    private static ClassRegistryImplementation<ScheduledTransactionImplementation> registry;

    /**
     *
     * @param description               descrizione dela transazione programmata
     * @param account                   account legato alla transazione programmata
     * @throws IllegalArgumentException se la descrizioni ha lughezza 0
     */
    private ScheduledTransactionImplementation(String description, Account account) throws IllegalArgumentException{
        setDescription(description);
        transactions = new ArrayList<>();
        this.account = Objects.requireNonNull(account, "Account is Null");
        ID = hashCode();
    }

    /**
     *
     * @return  l'instanza della classe ClassRegistry parametrizzata a ScheduledTransactionImplementation
     *          che si occupa della gestione delle istanze della classe correntev e dei loro ID
     */
    private synchronized static ClassRegistryImplementation<ScheduledTransactionImplementation> getRegistry(){
        if(registry == null) registry = new ClassRegistryImplementation<>(
                arguments -> new ScheduledTransactionImplementation((String) arguments[0], (Account) arguments[1]));
        return registry;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public static ScheduledTransactionImplementation getInstanceByID(int id) throws IllegalArgumentException{
        return getRegistry().getInstanceByID(id);
    }

    /**
     *
     * @param description   descrizione dela transazione programmata
     * @param account       account legato alla transazione programmata
     * @return              l'istanza collegata ai parametri passati
     */
    public static ScheduledTransactionImplementation getInstance(String description, Account account){
        return getRegistry().getInstance(description, account);
    }

    /**
     *
     * @return  id della transazione programmata
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     *
     * @return  descrione della transazione programmata
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return                          lista delle trasazioni
     * @throws IllegalStateException    se la lista delle transazioni e vuota
     */
    @Override
    public List<Transaction> getTransactions() throws IllegalStateException {
        if(transactions.isEmpty()) throw new IllegalStateException("Transaction List is Empty");
        return transactions;
    }

    /**
     *
     * @param p                         predicato da apllicare alla lista delle transazioni
     * @return                          lista a cui e stato apllicato il predicato
     * @throws IllegalStateException    se la lista delle transazioni e vuota
     */
    @Override
    public List<Transaction> getTransactions(Predicate<? super Transaction> p) throws IllegalStateException {
        return getTransactions().stream().filter(p).collect(Collectors.toList());
    }

    /**
     *
     * @return                          true se tutte le transazioni della lista sono state schedulate
     * @throws IllegalStateException    se la lista delle transazioni e vuota
     */
    @Override
    public boolean isCompleted() throws IllegalStateException {
        return getTransactions(t -> t.getDate().after(new Date())).isEmpty();
    }

    /**
     *
     * @param d                           descrizione della trasazione programmata
     * @throws IllegalArgumentException   se la descrizione ha lunghezza 0
     */
    @Override
    public void setDescription(String d) throws IllegalArgumentException {
        if(Objects.requireNonNull(d, "Description is Null").length() == 0)
            throw new IllegalArgumentException("Description is Empty");
        this.description = d;
    }

    /**
     *
     * @param list                      da assegnare alla transazione programmata
     * @throws IllegalStateException    se la transazione programmata gia posside una lista
     * @throws IllegalArgumentException se la lista delle trasazioni contine un movimento che non e collegato all'account della transazione programmata
     */
    @Override
    public synchronized void setTransactionsList(List<Transaction> list)throws IllegalStateException, IllegalArgumentException {
        if(!transactions.isEmpty()) throw new IllegalStateException("ScheduledTransaction alredy generated");
        transactions = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledTransactionImplementation that = (ScheduledTransactionImplementation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {
        return description.hashCode() + account.hashCode();
    }

    /**
     *
     * @param nTrasaction               numero transazioni che compongolo la list della transazione programmata
     * @param intervalloGiorniPagamento intervallo di giorni tra le transazioni
     * @param firstDate                 data della prima transazione
     * @param totalAmount               amount totale della transazione programmata
     * @throws IllegalArgumentException se nTrasaction o intervalloGiorniPagamento  o totalAmount e minore uguale a 0,
     *                                  se firstDate non e una data futura
     * @throws IllegalStateException    se la transazione programmata posside gia una lista di transazioni
     */
    @Override
    public synchronized void generateTransactions(int nTrasaction, int intervalloGiorniPagamento, Date firstDate, Double totalAmount)
            throws IllegalArgumentException, IllegalStateException {
        if(!transactions.isEmpty()) throw new IllegalStateException("ScheduledTransaction alredy generated");
        if(nTrasaction <= 0) throw new IllegalArgumentException("Numero Transazioni e minore o uguale a 0");
        if(intervalloGiorniPagamento <= 0) throw new IllegalArgumentException("L'intervallo di giorni tra ogni Transazione e minore o uguale a 0");
        if(Objects.requireNonNull(firstDate, "First Date is Null").before(new Date()))
            throw new IllegalArgumentException("First Date is not a fure Date");
        if(totalAmount <= 0) throw new IllegalArgumentException("Total amount is negative");

        Calendar c = Calendar.getInstance();
        c.setTime(firstDate);
        for(int i = 0; i <= (nTrasaction-1); i++){
            c.add(Calendar.DAY_OF_MONTH, intervalloGiorniPagamento * i);
            Transaction transaction = TransactionImplementation.getInstance(c.getTime());
            transaction.addTag(TagImplementation.getInstance("Scheduled Transaction", "Transazione Programmata"));
            Movement movement = MovementImplementation.getInstance(description + " Numero: " + (i+1), totalAmount/nTrasaction, MovementType.DEBIT);
            transaction.addMovement(movement);
            AccountImplementation.getInstance(AccountType.ASSETS, "AccountAppoggio", "AccountAppoggio", 5000).addMovement(movement);
            transactions.add(transaction);
        }
    }

    /**
     *
     * @return account collegato alla transazione programmata
     */
    @Override
    public Account getAccount() {
        return account;
    }
}
