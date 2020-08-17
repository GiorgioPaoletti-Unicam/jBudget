package it.unicam.cs.pa.jbudget105056.model;

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
class MovementImplementationTest {

    /**
     * si occupa di verificare il corretto comportamento dei metodi getIstance e getIstanceById
     */
    @Test
    void getIstance() {

        Movement telefonoNuovo = MovementImplementation.getInstance("Telefono nuovo", 249.99, MovementType.DEBIT);

        Movement telefonoNuovoCopy = MovementImplementation.getInstanceByID(telefonoNuovo.getID());

        assertEquals(telefonoNuovo, telefonoNuovoCopy);
    }

    /**
     * si occupa di verificare le relazioni che intercorrono tra la classe Transaction e Movement
     */
    @Test
    void getTagOfTransaction() {

        Movement telefonoNuovo = MovementImplementation.getInstance("Telefono nuovo", 249.99, MovementType.DEBIT);
        Tag tag1 = TagImplementation.getInstance("Scarpe", "Shopping");


        //il movimento non e collegato a una transazione
        assertThrows(NullPointerException.class, telefonoNuovo::getDate);
        assertThrows(NullPointerException.class, telefonoNuovo::tags);
        assertThrows(NullPointerException.class, () -> telefonoNuovo.addTag(tag1));

        Transaction telefonoNuovoTrans = TransactionImplementation.getInstance(new Date());
        telefonoNuovoTrans.addMovement(telefonoNuovo);
        telefonoNuovo.addTag(tag1);
        assertEquals(telefonoNuovo.tags(), telefonoNuovoTrans.tags());
        assertEquals(telefonoNuovo.getDate(), telefonoNuovoTrans.getDate());

        //si svuotano tutte e due le liste dei tag perche sono collegate
        telefonoNuovoTrans.removeTag(TagImplementation.getInstanceByID(tag1.getID()));
        assertThrows(IllegalStateException.class, () -> telefonoNuovo.tags());

    }

}