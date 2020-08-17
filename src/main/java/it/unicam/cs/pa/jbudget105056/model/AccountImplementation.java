package it.unicam.cs.pa.jbudget105056.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementa l'interfaccia Account
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class AccountImplementation implements Account, ClassRegistryImplementation.ObjectWithGetId{

    private int ID;
    private String name;
    private String description;
    private double opening;
    private AccountType type;

    private List<Movement> movements;

    private static ClassRegistryImplementation<AccountImplementation> registry;

    /**
     *
     * @param type          tipo del conto
     * @param name          nome del conto
     * @param description   descrizione del conto
     * @param opening       bilancio iniziale con cui viene creato il conto
     */
    private AccountImplementation(AccountType type, String name, String description, double opening) throws IllegalArgumentException{
        if(name.length() == 0) throw new IllegalArgumentException("Name is empty");
        this.name = Objects.requireNonNull(name, "Name is null");
        if(description.length() == 0) throw new IllegalArgumentException("Description is empty");
        this.description = description;
        if(type.equals(AccountType.ASSETS) && opening < 0) throw new IllegalArgumentException("Assets account cannot have negative opening");
        if(type.equals(AccountType.LIABILITIES) && opening > 0) throw new IllegalArgumentException("Liabilities account cannot have positive opening");
        this.opening = opening;
        this.type = type;
        this.ID = hashCode();
        movements = new ArrayList<>();
    }

    /**
     *
     * @return  l'instanza della classe ClassRegistry parametrizzata a AccountImplementation
     *          che si occupa della gestione delle istanze della classe correntev e dei loro ID
     */
    private synchronized static ClassRegistryImplementation<AccountImplementation> getRegistry() throws IllegalArgumentException{
        if(registry == null) registry = new ClassRegistryImplementation<>(
                arguments -> new AccountImplementation((AccountType) arguments[0], (String) arguments[1], (String) arguments[2], (Double) arguments[3]));
        return registry;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public static AccountImplementation getInstanceByID(int id) throws IllegalArgumentException{
        return getRegistry().getInstanceByID(id);
    }

    /**
     *
     * @param type          tipo del conto
     * @param name          nome del conto
     * @param description   descrizione del conto
     * @param opening       bilancio iniziale con cui viene creato il conto
     * @return              l'istanza collegata ai parametri passati
     */
    public static AccountImplementation getInstance(AccountType type, String name, String description, double opening){
        return getRegistry().getInstance(type, name, description, opening);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountImplementation that = (AccountImplementation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() { return name.hashCode() + description.hashCode() + type.name().hashCode(); }

    /**
     *
     * @return  nome del conto
     */
    @Override
    public String getName() { return name; }

    /**
     *
     * @return  descrizione del conto
     */
    @Override
    public String getDescription() { return description; }

    /**
     *
     * @return  Id univoco del conto
     */
    @Override
    public int getID() { return ID; }

    /**
     *
     * @return  bilancio iniziale con cui viene creato il conto
     */
    @Override
    public double getOpeningBalance() { return opening; }

    /**
     *
     * @return  tipo del conto
     */
    @Override
    public AccountType getType() {
        return type;
    }

    /**
     *
     * @return                          bilancio attuale del conto
     */
    @Override
    public synchronized double getBalance() {
        if(movements.isEmpty()) return opening;
        double balance = opening;
        for( Movement m : getMovements()) {
            if (type.equals(AccountType.ASSETS)) {
                if (m.type().equals(MovementType.CREDITS)) balance += m.getAmount();
                else balance -= m.getAmount();
            }
            else {
                if (m.type().equals(MovementType.DEBIT)) balance -= m.getAmount();
                else balance -= m.getAmount();
            }
        }
        return new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     *
     * @return                          lista dei movimenti legata al conto
     */
    @Override
    public List<Movement> getMovements() {
        return movements;
    }

    /**
     *
     * @param p                         predicato da applicare alla lista dei movimenti
     * @return                          lista dei moviemnti su cui e stato applicato il predicato
     */
    @Override
    public List<Movement> getMovements(Predicate<? super Movement> p) {
        return getMovements().stream().parallel().filter(p).collect(Collectors.toList());
    }

    /**
     *
     * @param m                         riferimento al moviemnto da aggiungere alla lista dei moviementi
     * @throws IllegalArgumentException se il riferimento passato per argomneto e gia presente nella lista
     */
    @Override
    public synchronized void addMovement(Movement m) throws IllegalArgumentException {
        if(movements.contains(Objects.requireNonNull(m, "Null Movement")))
            throw new IllegalArgumentException("Movement already exist");
        if(type.equals(AccountType.ASSETS) && m.type().equals(MovementType.DEBIT) && getBalance() < m.getAmount())
                    throw new IllegalStateException("Insufficient balance");
        if(type.equals(AccountType.LIABILITIES) && m.type().equals(MovementType.CREDITS))
            throw new IllegalStateException("No credit Movement are accepts in Liabilities Account");
        movements.add(m);
        m.setAccount(this);
    }

    /**
     *
     * @param m                         riferimento al moviemnto da rimuovere dalla lista
     * @throws IllegalArgumentException se il riferiemnto passato per argomento non e presente nella lista
     */
    @Override
    public synchronized void removeMovement(Movement m) throws IllegalArgumentException {
        if(!(getMovements().contains(Objects.requireNonNull(m, "Null Movement"))))
            throw new IllegalArgumentException("Movement not exist");
        movements.remove(m);
    }

    @Override
    public String toString() {
        StringBuilder movementsString = new StringBuilder();
        for(Movement m : movements){
            movementsString.append(m.toString()).append("\n");
        }

        return "Account {" +
                "ID: " + ID +
                ", name: " + name +
                ", description: " + description +
                ", opening: " + opening +
                ", type: " + type +
                ", movements: " + movementsString +
                " }";
    }
}
