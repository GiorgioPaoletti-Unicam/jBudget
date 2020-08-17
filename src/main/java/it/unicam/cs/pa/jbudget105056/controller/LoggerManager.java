package it.unicam.cs.pa.jbudget105056.controller;

import java.util.logging.Level;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione del Logger
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface LoggerManager {

    void setMessage(Level level, String message);

}
