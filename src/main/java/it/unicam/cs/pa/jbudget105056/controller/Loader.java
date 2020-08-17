package it.unicam.cs.pa.jbudget105056.controller;

import it.unicam.cs.pa.jbudget105056.model.Budget;
import it.unicam.cs.pa.jbudget105056.model.Ledger;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione del caricamento dati
 *  riguardanti le istanze della classe Ledger e Budget
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Loader {

    Ledger loadLedger() throws FileNotFoundException, IOException;

    Budget loadBudget() throws FileNotFoundException, IOException;

}
