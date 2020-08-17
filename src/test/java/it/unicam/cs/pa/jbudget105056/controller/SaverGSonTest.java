package it.unicam.cs.pa.jbudget105056.controller;

import it.unicam.cs.pa.jbudget105056.model.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
class SaverGSonTest {

    /**
     * il test si occupa di verificare il corretto salvataggio dell'instaza del Ledger
     */
    @Test
    void saveLedger() {

        Ledger LibroMastro = new LedgerImplementation();

        LibroMastro.addAccount(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50);
        LibroMastro.addAccount(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78);
        LibroMastro.addAccount(AccountType.LIABILITIES, "Carta di Debito", "Carta di credito", 0);

        LibroMastro.addTag("Veicoli", "Manutenzione");
        LibroMastro.addTag("Shopping", "Abbigliamento & Scarpe");
        LibroMastro.addTag("Cibo & Bevande", "Generi alimentari");
        LibroMastro.addTag("Spese Fianaziarie", "Prelievo");

        Transaction t1 = TransactionImplementation.getInstance(new Date());

        Movement m1 = MovementImplementation.getInstance("Incremento Conto Cassa", 50.10, MovementType.CREDITS);
        Movement m2 = MovementImplementation.getInstance("Decremento Conto Bancario", 50.10, MovementType.DEBIT);
        Movement m3 = MovementImplementation.getInstance("Commissione", 2.75, MovementType.DEBIT);

        t1.addMovement(m1);
        t1.addMovement(m2);
        t1.addMovement(m3);

        for(Account a : LibroMastro.getAccounts()){
            if(a.getName().equals("Cassa")) a.addMovement(m1);
            if(a.getName().equals("Conto Bancario")) a.addMovement(m2);
            if(a.getName().equals("Conto Bancario")) a.addMovement(m3);
        }

        t1.addTag(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"));

        LibroMastro.addTransaction(t1);

        //Sovrascrive il file Ledger dell'applicazione
        //new SaverGSon().saveLedger(LibroMastro);

    }

    /**
     * il test si occupa di verificare il corretto savataggio dell'instaza del Budget
     */
    @Test
    void saveBudget() {
        Budget b = new BudgetImplementation();
        b.set(TagImplementation.getInstance("Veicoli", "Manutenzione"), 2500);
        b.set(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"), 40);
        b.set(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"), 500);

        //Sovrascrive il file Budget dell'applicazione
        //new SaverGSon().saveBudget(b);
    }
}