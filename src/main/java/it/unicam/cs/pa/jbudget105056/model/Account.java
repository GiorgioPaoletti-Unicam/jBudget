package it.unicam.cs.pa.jbudget105056.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione del singolo conto.
 * La classe ha la responsabilita di gestire la modifica delle informazioni associate al conto:
 * descrizione, saldo iniziale, la lista dei movimenti e il tipo di conto.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Account{

    String getName();

    String getDescription();

    int getID();

    double getOpeningBalance();

    AccountType getType();

    /**
     * @return  ricavare il saldo attuale del conto.
     */
    double getBalance();

    List<Movement> getMovements();

    /**
     * @return  lista di movimenti che rispetta un certo predicato
     */
    List<Movement> getMovements(Predicate<? super Movement> p);

    void addMovement(Movement m);

    void removeMovement(Movement m);

}
