package it.unicam.cs.pa.jbudget105056.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test verificare il funzionamento della classe AccountImplementation
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
class AccountImplementationTest {

    private static Account cassa;
    private static Account contoBancario;
    private static Account hype;
    private static Account cartaDiDebito;

    /**
     * si occupa di creare le istanze degli account utilizzati nei test a seguire
     */
    @BeforeEach
    void BeforeEach() {
        cassa = AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 0);
        contoBancario = AccountImplementation.getInstance(AccountType.ASSETS, "Conto Bancario", "Conto Bancario Personale", 2500);
        hype = AccountImplementation.getInstance(AccountType.ASSETS, "HYPE", "Carta Prepagata", 500);
        cartaDiDebito = AccountImplementation.getInstance(AccountType.LIABILITIES, "Carta di Debito", "Carta di Debito", 0);
    }

    /**
     * si occupa di verificare il corretto comportamento dei metodi getIstance e getIstanceById
     */
    @Test
    void getIstance() {

        Account cassaCopy = AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 0);
        assertEquals(cassa, cassaCopy);

        Account cassaCopyByID = AccountImplementation.getInstanceByID(cassa.getID());
        assertEquals(cassa, cassaCopyByID);

    }

    /**
     * si occupa di verificare il corretto comportamento dei metodi addMovement in presenza di un account di tipo Assets
     */
    @Test
    void addMovementToAssetAccount() {

        Movement cenaPizza = MovementImplementation.getInstance("Cena pizza", 12.50, MovementType.DEBIT);

        //Movimento non appartiene a nessuna transazione
        assertThrows(IllegalStateException.class, () -> cassa.addMovement(cenaPizza));

        Transaction cenaPizzaTrans = TransactionImplementation.getInstance(new Date());
        cenaPizzaTrans.addMovement(cenaPizza);
        //Insufficient balance
        assertThrows(IllegalStateException.class, () -> cassa.addMovement(cenaPizza));

        cassa.addMovement(createRegaloNonnaMovement());
        cassa.addMovement(cenaPizza);
        assertEquals(37.50, cassa.getBalance());

    }

    /**
     * si occupa di verificare il corretto comportamento dei metodi addMovement in presenza di un account di tipo Liabilities
     */
    @Test
    void addMovementToLiabilitiesAccount() {

        Movement stivaliNuovi = MovementImplementation.getInstance("Stivali Nuovi", 69.99, MovementType.DEBIT);
        Transaction stivaliNuoviTrans = TransactionImplementation.getInstance(new Date());
        stivaliNuoviTrans.addMovement(stivaliNuovi);
        cartaDiDebito.addMovement(stivaliNuovi);
        assertEquals(cartaDiDebito.getBalance(), -69.99);

        //non posso aggiungere movimenti di tipo CREDIT a un Account LIABILITIES
        assertThrows(IllegalStateException.class, () -> cartaDiDebito.addMovement(createRegaloNonnaMovement()));

    }

    /**
     * si occupa di vicare il corretto funzionamento del metodo getMovements a cui viene passato un predicato
     */
    @Test
    void getMovementsByPredicate() {

        Movement regaloNonna = createRegaloNonnaMovement();

        cassa.addMovement(regaloNonna);

        assertEquals(cassa.getMovements(m -> m.getID() == regaloNonna.getID()).get(0), regaloNonna);

    }

    /**
     * si occupa di verificare il corretto funzionamento del metodo removeMovement
     */
    @Test
    void removeMovement() {

        Movement regaloNonna = createRegaloNonnaMovement();

        cassa.addMovement(regaloNonna);

        cassa.removeMovement(MovementImplementation.getInstanceByID(regaloNonna.getID()));

        assertTrue(cassa.getMovements().isEmpty());

        assertEquals(0.00, cassa.getBalance());
    }

    /**
     *
     * @return Movimento giocattolo per i test
     */
    Movement createRegaloNonnaMovement() {
        Movement regaloNonna = MovementImplementation.getInstance("Regalo Nonna", 50.00, MovementType.CREDITS);
        Transaction regaloNonnaTrans = TransactionImplementation.getInstance(new Date());
        regaloNonnaTrans.addMovement(regaloNonna);
        return regaloNonna;
    }

}