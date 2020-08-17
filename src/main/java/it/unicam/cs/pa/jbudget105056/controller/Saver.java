package it.unicam.cs.pa.jbudget105056.controller;

import it.unicam.cs.pa.jbudget105056.model.*;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione del savaggio dati
 *  riguardanti le istanze della classe Ledger e Budget passate come parametro ai rispettivi metodi
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Saver {

    void saveLedger(Ledger ledger);

    void saveBudget(Budget budget);

}
