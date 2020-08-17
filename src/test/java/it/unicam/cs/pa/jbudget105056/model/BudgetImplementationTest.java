package it.unicam.cs.pa.jbudget105056.model;
import org.junit.jupiter.api.BeforeEach;
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
class BudgetImplementationTest {

    private static Ledger LibroMastro;

    private static Budget budget;

    /**
     * crea accounts e tags che verranno usati nei test a seguire
     */
    @BeforeEach
    void BeforeEach() {

        LibroMastro = new LedgerImplementation();

        budget = new BudgetImplementation();

        //account List
        LibroMastro.addAccount(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50);
        LibroMastro.addAccount(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78);
        LibroMastro.addAccount(AccountType.LIABILITIES, "Carta di Debito", "Carta di debito", 0);

        //tag list
        LibroMastro.addTag("Shopping", "Abbigliamento & Scarpe");
        LibroMastro.addTag("Cibo & Bevande", "Generi alimentari");
        LibroMastro.addTag("Spese Fianaziarie", "Prelievo");

        //budget list
        budget.set(TagImplementation.getInstance("Shopping", "Abbigliamento & Scarpe"), 200);
        budget.set(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"), 40);
        budget.set(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"), 2500);
    }

    /**
     * verifica il corretto funzionamento del metodo getPredicate dell classe BudgetImplementation
     */
    @Test
    void predicate() {

        LibroMastro.addTransaction(createPrelievoTransaction());

        assertEquals(1, LibroMastro.getTransactions(budget.getPredicate()).size());


    }

    /**
     * verifica il comportamento del metodo generateReport della classe BudgetManagerImplementation
     */
    @Test
    void generateReport() {

        LibroMastro.addTransaction(createPrelievoTransaction());

        BudgetReport bR = new BudgetManagerImplementation().generateReport(LibroMastro, budget);
        assertEquals(3, bR.report().size());
    }

    /**
     *
     * @return Transazione giocattolo da utilizzare nel budget
     */
    Transaction createPrelievoTransaction(){
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

        return prelievo;
    }
}