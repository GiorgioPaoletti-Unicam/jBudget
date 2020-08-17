package it.unicam.cs.pa.jbudget105056.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * implementa l'interfaccia ClassRegistry
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class ClassRegistryImplementation <T extends ClassRegistryImplementation.ObjectWithGetId> implements ClassRegistry<T>{

    private Map< Integer, T> registry;
    private Function<Object[], T> factory;

    /**
     *
     * @param factory   Funzione che a partire da un array di oggetti (Argomenti del costruttore) produce un oggetto
     */
    public ClassRegistryImplementation(Function<Object[], T> factory){
        registry = new HashMap<>();
        this.factory = factory;
    }

    /**
     *
     * @param id                        id dell'istanza
     * @return                          istanza collegata all'id passato come parametro
     * @throws IllegalArgumentException se l'id non esiste nel rigistro che tine traccia delle istanze
     */
    public T getInstanceByID(int id) throws IllegalArgumentException {
        if(!registry.containsKey(id)) throw new IllegalArgumentException("Id not exist");
        else return registry.get(id);
    }

    /**
     *
     * @param arguments array di oggetti che costituiscono gli argomenti del costruttore privato della classe T parametrizzata
     * @return
     */
    public T getInstance(Object... arguments){
        T o = factory.apply(arguments);
        registry.put(o.getID(), o);
        return o;
    }

    /**
     * interfaccia che ha la responsabilita di far si che le classi che la implemetano posseggano un metodo che restituisce un ID
     */
    public interface ObjectWithGetId{
        int getID();
    }

}
