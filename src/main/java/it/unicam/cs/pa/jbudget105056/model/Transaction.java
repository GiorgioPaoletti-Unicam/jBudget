package it.unicam.cs.pa.jbudget105056.model;

import java.util.Date;
import java.util.List;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione della singola transazione.
 * La transazione gestisce una lista di movimento a cui passa la lista dei tag e la data.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Transaction {

    int getID();

    List<Movement> movements();

    List<Tag> tags();

    void addTag(Tag t);

    void removeTag(Tag t);

    Date getDate();

    void addMovement(Movement m);

    void removeMovement(Movement m);

    void setDate(Date d);

    /**
     *
     * @return  saldo (la variazione totale dei movimenti) della transazione.
     */
    double getTotalAmount();

}
