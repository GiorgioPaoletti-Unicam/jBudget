package it.unicam.cs.pa.jbudget105056.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Implementa l'interfaccia Transaction
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class TransactionImplementation implements Transaction, ClassRegistryImplementation.ObjectWithGetId{

    private final int ID;
    private List<Tag> tags;
    private Date date;
    private List<Movement> movements;

    private static ClassRegistryImplementation<TransactionImplementation> registry;

    /**
     *
     * @param date  data della transazione
     */
    private TransactionImplementation(Date date) {
        this.date = Objects.requireNonNull(date);
        this.ID = hashCode();
        movements = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     *
     * @return  l'instanza della classe ClassRegistry parametrizzata a TransactionImplementation
     *          che si occupa della gestione delle istanze della classe correntev e dei loro ID
     */
    private synchronized static ClassRegistryImplementation<TransactionImplementation> getRegistry(){
        if(registry == null) registry = new ClassRegistryImplementation<>(
                arguments -> new TransactionImplementation((Date) arguments[0]));
        return registry;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public static TransactionImplementation getInstanceByID(int id) throws IllegalArgumentException{
        return getRegistry().getInstanceByID(id);
    }

    /**
     *
     * @param date  data della transazione
     * @return      l'istanza collegata ai parametri passati
     */
    public static TransactionImplementation getInstance(Date date){
        return getRegistry().getInstance(date);
    }

    /**
     *
     * @return  ID univoco della transazione
     */
    @Override
    public int getID() { return ID; }

    /**
     *
     * @return                          lista dei moviemnto legata alla transazione
     */
    @Override
    public List<Movement> movements() {
        return movements;
    }

    /**
     *
     * @return                          lista dei tag legata alla transazione
     */
    @Override
    public List<Tag> tags() {
        return tags;
    }

    /**
     *
     * @param t                         riferimento al tag da aggiungere alla lista dei tag
     * @throws IllegalArgumentException se il riferimento passato come argoemto e gia presente nella lista
     */
    @Override
    public synchronized void addTag(Tag t) throws IllegalArgumentException {
        if(tags.contains(Objects.requireNonNull(t, "Null Tag")))
            throw new IllegalArgumentException("Tag already exist");
        tags.add(t);

    }

    /**
     *
     * @param t                         riferimento al tag da rimuovere
     * @throws IllegalArgumentException se il riferimento non e presente nella lista
     * @throws IllegalStateException    se la lista dei tag e vuota
     */
    @Override
    public synchronized void removeTag(Tag t) throws IllegalArgumentException, IllegalStateException {
        if(tags.isEmpty()) throw new IllegalStateException("Tag List is Empty");
        if(!tags.contains(Objects.requireNonNull(t, "Null Tag")))
            throw new IllegalArgumentException("Tag not exist");
        tags.remove(t);
    }

    /**
     *
     * @return      data della transazione
     */
    @Override
    public Date getDate() { return date; }

    /**
     *
     * @param m                         riferimento del moviemento da aggiungere alla lista dei moviementi della transazione
     * @throws IllegalArgumentException se il riferimento passato per argomento e gia presente nella lista
     */
    @Override
    public synchronized void addMovement(Movement m) throws IllegalArgumentException{
        if(movements.contains(Objects.requireNonNull(m, "Null Movement")))
            throw new IllegalArgumentException("Movement already exist");
        movements.add(m);
        m.setTransaction(this);
    }

    /**
     *
     * @param m                         riferimento del moviemento da rimuovere dalla lista dei moviementi della transazione
     * @throws IllegalArgumentException se il riferimento passato per argomento non e presente nella lista
     * @throws IllegalStateException    se la lista dei movimenti e vuota
     */
    @Override
    public synchronized void removeMovement(Movement m) throws IllegalArgumentException, IllegalStateException {
        if(movements.isEmpty()) throw new IllegalStateException("movements list is empty");
        if(!movements.contains(Objects.requireNonNull(m, "Null Movement")))
            throw new IllegalArgumentException("Movement not exist");
        movements.remove(m);
    }

    /**
     *
     * @param d riferiemento alla data della transazione
     */
    @Override
    public void setDate(Date d) { this.date = Objects.requireNonNull(d); }

    /**
     *
     * @return                          il saldo della transazione
     * @throws IllegalStateException    se la lista dei moviemnti e vuota
     */
    @Override
    public double getTotalAmount() throws IllegalStateException {
        if(movements.isEmpty()) throw new IllegalStateException("movements list is empty");
        double tot = 0.0;
        for( Movement m : movements){
            if(m.type().equals(MovementType.DEBIT)) tot -= m.getAmount();
            else tot += m.getAmount();
        }
        return new BigDecimal(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionImplementation that = (TransactionImplementation) o;
        return ID == that.ID &&
                tags.equals(that.tags) &&
                date.equals(that.date) &&
                movements.equals(that.movements);
    }

    @Override
    public int hashCode() {
        return date.toString().hashCode();
    }

    @Override
    public String toString() {
        String movementsString = "";
        for(Movement m : movements){
            movementsString += m.toString() + "\n";
        }

        return "Transaction {" +
                "ID: " + ID +
                ", tags: " + tags +
                ", date: " + date +
                ", movements: " + movementsString +
                " }";
    }
}
