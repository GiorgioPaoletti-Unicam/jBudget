package it.unicam.cs.pa.jbudget105056.model;

import it.unicam.cs.pa.jbudget105056.model.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

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
class TransactionImplementationTest {

    /**
     * Il metodo verifica il corretto comportamento della classe TransactionImplementation
     */
    @Test
    void transactionImplementationTest() {

        Transaction t1 = TransactionImplementation.getInstance(new Date());

        Movement m1 = MovementImplementation.getInstance("Incremento Conto Cassa", 50.10, MovementType.CREDITS);
        Movement m2 = MovementImplementation.getInstance("Decremento Conto Bancario", 50.10, MovementType.DEBIT);
        Movement m3 = MovementImplementation.getInstance("Commissione", 2.75, MovementType.DEBIT);

        t1.addMovement(m1);
        t1.addMovement(m2);
        t1.addMovement(m3);

        assertTrue(t1.getTotalAmount() == -2.75);

    }
}