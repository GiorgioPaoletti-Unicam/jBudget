package it.unicam.cs.pa.jbudget105056.controller;

import it.unicam.cs.pa.jbudget105056.model.Budget;
import it.unicam.cs.pa.jbudget105056.model.BudgetImplementation;
import it.unicam.cs.pa.jbudget105056.model.Ledger;
import it.unicam.cs.pa.jbudget105056.model.TagImplementation;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
class LoaderGSonTest {


    /**
     * il test si occupa di verificare il corretto caricamento dell'instaz del Ledger
     */
    @Test
    void loadLedger() throws IOException {

        Ledger LibroMastro;
        LibroMastro = new LoaderGSon().loadLedger();

        System.out.println(LibroMastro);

    }

    /**
     * il test si occupa di verificare il corretto caricamento dell'instaz del Budget
     */
    @Test
    void loadBudget() throws IOException {
        Budget b = new BudgetImplementation();
        b.set(TagImplementation.getInstance("Veicoli", "Manutenzione"), 2500);
        b.set(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"), 40);
        b.set(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"), 500);

        new SaverGSon().saveBudget(b);

        Budget b1;
        b1 = new LoaderGSon().loadBudget();

        System.out.println(b1);
    }
}