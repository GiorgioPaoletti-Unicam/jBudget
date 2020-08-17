package it.unicam.cs.pa.jbudget105056.model;

/**
 * La tipologia di movimento determina l’effetto di un movimento su un conto.
 * Infatti, il saldo d'un conto di tipo ASSET crescera con movimenti di tipo CREDITS e diminuira con movimenti di tipo DEBITS.
 * Viceversa, il saldo d’un conto di tipo LIABILITIES aumentera con movimenti di tipo DEBITS e diminuira con movimenti di tipo CREDITS.
 * All’interno di una transazione i movimenti DEBITS saranno trattati sempre come negativi, quelli CREDITS come positivi.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/06/2020
 *
 */
public enum MovementType {
    DEBIT,
    CREDITS
}
