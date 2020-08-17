package it.unicam.cs.pa.jbudget105056.model;

import java.util.*;

/**
 *Implementa l'interfaccia Movement
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class MovementImplementation implements Movement, ClassRegistryImplementation.ObjectWithGetId{

    private final int ID;
    private String description;
    private double amount;
    private Account account;
    private MovementType type;
    private Transaction transaction;

    private static ClassRegistryImplementation<MovementImplementation> registry;

    /**
     *
     * @param description               descrizione del movimento
     * @param amount                    importo del moviemento
     * @param type                      tipo del movimento
     * @throws IllegalArgumentException Se descrizione ha lunghezza uguale a 0,
     *                                  Se importo e negativo
     */
    private MovementImplementation(String description, double amount, MovementType type)
            throws IllegalArgumentException{
        if(Objects.requireNonNull(description, "Description is null")
                .length() == 0) throw new IllegalArgumentException("Description is empty");
        this.description = description;
        if(amount < 0) throw new IllegalArgumentException("Amount is Negative");
        this.amount = amount;
        this.type = Objects.requireNonNull(type, "Type is null");
        this.ID = hashCode();

    }

    /**
     *
     * @return  l'instanza della classe ClassRegistry parametrizzata a MovementImplementation
     *          che si occupa della gestione delle istanze della classe correntev e dei loro ID
     */
    private synchronized static ClassRegistryImplementation<MovementImplementation> getRegistry(){
        if(registry == null) registry = new ClassRegistryImplementation<>(
                arguments -> new MovementImplementation((String) arguments[0], (Double) arguments[1], (MovementType) arguments[2]));
        return registry;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public static MovementImplementation getInstanceByID(int id) throws IllegalArgumentException{
        return getRegistry().getInstanceByID(id);
    }

    /**
     *
     * @param description   descrizione movimento
     * @param amount        amount movimento
     * @param type          tipo movimento
     * @return              l'istanza collegata ai parametri passati
     */
    public static MovementImplementation getInstance(String description, double amount, MovementType type){
        return getRegistry().getInstance(description, amount, type);
    }

    /**
     *
     * @return  descrizione del movimento
     */
    @Override
    public String getDescription() { return description; }

    /**
     *
     * @return  tipo del movimento
     */
    @Override
    public MovementType type() { return type; }

    /**
     *
     * @return  importo del movimento
     */
    @Override
    public double getAmount() { return amount; }

    /**
     *
     * @return  riferimento alla transzione a cui appartiene
     */
    @Override
    public Transaction getTransaction() {
        return Objects.requireNonNull(transaction,
                "This movement is not assigned to any transaction");
    }

    /**
     *
     * @return  riferimento al conto a cui appartiene
     */
    @Override
    public Account getAccount() {
        return Objects.requireNonNull(account,
                "This movement is not assigned to any account");
    }

    /**
     *
     * @return  Id univoco del moviemento
     */
    @Override
    public int getID() { return ID; }

    /**
     *
     * @return  data del movimento che deriva dalla data della transazione a cui appartiene
     */
    @Override
    public Date getDate() {
        return Objects.requireNonNull(transaction,
                "This movement is not assigned to any transaction")
                .getDate();
    }

    /**
     *
     * @return                          lista delle categorie a cui appartiene il movimento che deriva da quello della transazione a cui appartiene
     * @throws IllegalStateException    Se la lista dei tag e vuota
     */
    @Override
    public List<Tag> tags() throws IllegalStateException{
        if(transaction.tags().isEmpty()) throw new IllegalStateException("Tag list is empty");
        return Objects.requireNonNull(transaction,
                "This movement is not assigned to any transaction")
                .tags();
    }

    /**
     *
     * @param t                         riferimento al tag da aggiungere alla lista dei tag della transazione a cui appartiene
     * @throws IllegalArgumentException se il riferiemnto passato per argomento e gia esistente nella lista
     */
    @Override
    public void addTag(Tag t) throws IllegalArgumentException {
        Objects.requireNonNull(transaction,
                "This movement is not assigned to any transaction")
                .addTag(t);
    }

    /**
     *
     * @param t                         riferimento al tag da rimuovere dall lista delle categorie
     * @throws IllegalArgumentException se il riferiemnto passato per argomento non e presente nella lista
     * @throws IllegalStateException    se la lista dei tag e vuota
     */
    @Override
    public void removeTag(Tag t) throws IllegalArgumentException, IllegalStateException {
        Objects.requireNonNull(transaction,
                "This movement is not assigned to any transaction")
                .removeTag(t);
    }

    /**
     *
     * @param a     riferimento al conto da collegare al moviemnto
     */
    @Override
    public void setAccount(Account a) { account = Objects.requireNonNull(a); }

    /**
     *
     * @param t     riferimento alla transazione da collegare al moviemnto
     */
    @Override
    public void setTransaction(Transaction t) { transaction = Objects.requireNonNull(t); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementImplementation that = (MovementImplementation) o;
        return ID == that.ID &&
                Double.compare(that.amount, amount) == 0 &&
                description.equals(that.description) &&
                account.equals(that.account) &&
                type == that.type &&
                transaction.equals(that.transaction);
    }

    @Override
    public int hashCode() { return description.hashCode() + type.name().hashCode() + (int) amount; }


    @Override
    public String toString() {
        return "Movement { " +
                "ID: " + ID +
                ", description: '" + description +
                ", amount: " + amount +
                ", accountID: " + account.getID() +
                ", type: " + type +
                ", transactionID: " + transaction.getID() +
                " }";
    }
}
