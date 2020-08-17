package it.unicam.cs.pa.jbudget105056.model;

/**
 * ha la responsabilita di gestire le istanze della classe T parametrizzata
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface ClassRegistry<T> {

    T getInstanceByID(int id);

    public T getInstance(Object... arguments);
}
