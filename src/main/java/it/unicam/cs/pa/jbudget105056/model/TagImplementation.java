package it.unicam.cs.pa.jbudget105056.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementa l'interfaccia Tag
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class TagImplementation implements Tag, ClassRegistryImplementation.ObjectWithGetId{

    private final int ID;
    private String name;
    private String description;

    private static ClassRegistryImplementation<TagImplementation> registry;

    /**
     *
     * @param name                      Nome della categoria
     * @param description               Descrizione delle categoria
     * @throws IllegalArgumentException Se nome e descrizione hanno lunghezza uguale a 0
     */
    private TagImplementation(String name, String description) throws IllegalArgumentException {
        if(Objects.requireNonNull(name, "Name is null")
                .length() == 0) throw new IllegalArgumentException("Name is empty");
        this.name = name;
        if(Objects.requireNonNull(description, "Description is null")
                .length() == 0) throw new IllegalArgumentException("Description is empty");
        this.description = description;
        this.ID = hashCode();
    }

    /**
     *
     * @return  l'instanza della classe ClassRegistry parametrizzata a TagImplementation
     *          che si occupa della gestione delle istanze della classe correntev e dei loro ID
     */
    private synchronized static ClassRegistryImplementation<TagImplementation> getRegistry(){
        if(registry == null) registry = new ClassRegistryImplementation<>(
                arguments -> new TagImplementation((String) arguments[0], (String) arguments[1]));
        return registry;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public static TagImplementation getInstanceByID(int id) throws IllegalArgumentException{
        return getRegistry().getInstanceByID(id);
    }

    /**
     *
     * @param name          nome della categoria
     * @param description   descrizione della categoria
     * @return              l'istanza collegata ai parametri passati
     */
    public static TagImplementation getInstance(String name, String description){
        return getRegistry().getInstance(name, description);
    }

    /**
     *
     * @return     descrizione della categoria
     */
    @Override
    public String getDescription() { return description; }

    /**
     *
     * @return      Nome della categoria
     */
    @Override
    public String getName() { return name; }

    /**
     *
     * @return      Id univoco legato alla categoria
     */
    @Override
    public int getID() { return ID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagImplementation that = (TagImplementation) o;
        return ID == that.ID &&
                name.equals(that.name) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public String toString() {
        return "Tag { " +
                "ID: " + ID +
                ", name: " + name +
                ", description: " + description +
                " }";
    }
}
