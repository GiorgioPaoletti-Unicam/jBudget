package it.unicam.cs.pa.jbudget105056.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
class ScheduledTransactionImplementationTest {

    /**
     * Il metodo verifica il corretto comportamento della classe ScheduledTransaction
     */
    @Test
    void generateTransactions() {

        ScheduledTransaction scheduledTransaction =
                ScheduledTransactionImplementation.getInstance("Rata Iphone 11 Pro",
                        AccountImplementation.getInstance(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78)
                );

        assertThrows(IllegalStateException.class, scheduledTransaction::isCompleted);

        assertThrows(IllegalStateException.class, scheduledTransaction::getTransactions);

        scheduledTransaction.generateTransactions(  5, 30, new Date(125, 9, 13), 500.00);

        for(Transaction t : scheduledTransaction.getTransactions())
            System.out.println(t);

    }
}