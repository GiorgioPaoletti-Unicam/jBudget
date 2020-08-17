package it.unicam.cs.pa.jbudget105056.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
class LedgerImplementationTest {

    private static Ledger LibroMastro;


    /**
     * crea accounts e tags che verranno usati nei test a seguire
     */
    @BeforeEach
    void BeforeEach() {

        LibroMastro = new LedgerImplementation();

        //account List
        LibroMastro.addAccount(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50);
        LibroMastro.addAccount(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78);
        LibroMastro.addAccount(AccountType.LIABILITIES, "Carta di Debito", "Carta di debito", 0);

        //tag list
        LibroMastro.addTag("Shopping", "Abbigliamento & Scarpe");
        LibroMastro.addTag("Cibo & Bevande", "Generi alimentari");
        LibroMastro.addTag("Spese Fianaziarie", "Prelievo");
    }

    /**
     * si occupa di verificare il corretto funzionamneto di creazione di una transazione
     */
    @Test
    void createTransaction() {

        Transaction prelievo = TransactionImplementation.getInstance(new Date());

        Movement incrementoCassa = MovementImplementation.getInstance("Incremento Cassa", 50.10, MovementType.CREDITS);
        Movement decrementoContoBancario = MovementImplementation.getInstance("Decremento Conto Bancario", 50.10, MovementType.DEBIT);
        Movement commissione = MovementImplementation.getInstance("Commissione", 2.75, MovementType.DEBIT);

        for(Account a : LibroMastro.getAccounts()){
            if(a.getName().equals("Cassa")) a.addMovement(incrementoCassa);
            if(a.getName().equals("Conto Bancario")) a.addMovement(decrementoContoBancario);
            if(a.getName().equals("Conto Bancario")) a.addMovement(commissione);
        }

        prelievo.addMovement(incrementoCassa);
        prelievo.addMovement(decrementoContoBancario);
        prelievo.addMovement(commissione);

        prelievo.addTag(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"));

        LibroMastro.addTransaction(prelievo);

        assertThrows(IllegalArgumentException.class, () -> LibroMastro.addTransaction(prelievo));

    }

    /**
     * Il metodo verifica il corretto comportamento del metodo schedule e generateTransactions
     */
    @Test
    void ScheduledTrasaction() {
        ScheduledTransaction st = ScheduledTransactionImplementation.getInstance("Iphone",
                AccountImplementation.getInstance(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78));

        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setDate(23);

        st.generateTransactions(2, 10, date,500.00);

        Ledger ledger = new LedgerImplementation();
        ledger.addAccount(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78);
        ledger.addScheduledTransaction(st);

        ledger.schedule();

        System.out.println(ledger);
    }
}