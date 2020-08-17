package it.unicam.cs.pa.jbudget105056.controller;

import com.google.inject.Guice;
import it.unicam.cs.pa.jbudget105056.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControllerImplementationTest {

    private static Controller controller;

    /**
     * Prepara gli account e i tag che verrano utilizzati dai test
     */
    @BeforeEach
    void BeforeEach() {

        controller = Guice.createInjector(new ControllerImplementationModule()).getInstance(ControllerImplementation.class);

        controller.addAccount("Cassa", "Portafoglio Personale", 30.50, AccountType.ASSETS);
        controller.addAccount("Conto Bancario", "Conto Bancario", 2489.78, AccountType.ASSETS);
        controller.addAccount("Carta di Debito", "Carta di debito", 0, AccountType.LIABILITIES);

        controller.addTag("Shopping", "Abbigliamento & Scarpe");
        controller.addTag("Cibo & Bevande", "Generi alimentari");
        controller.addTag("Spese Fianaziarie", "Prelievo");

        controller.addBudget(TagImplementation.getInstance("Shopping", "Abbigliamento & Scarpe"), 200);
        controller.addBudget(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"), 40);
        controller.addBudget(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"), 2500);
    }

    /**
     * Verifica il corretto funzioanmento dei metodi addTransaction e addTrasferimento della classe controller
     */
    @Test
    void createTransaction() {

        List<Tag> tagListCibo = new ArrayList<>();
        tagListCibo.add(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"));

        List<Tag> tagListPrelievo = new ArrayList<>();
        tagListPrelievo.add(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"));

        //Aggiungo uscita
        controller.addTransaction(  AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                                    MovementType.DEBIT, 10.56, tagListCibo, new Date(), "Pizza con Amici"
        );

        //Non bastano i soldi
        assertThrows(IllegalStateException.class, () -> controller.addTransaction(  AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                MovementType.DEBIT, 300, tagListCibo, new Date(125, Calendar.OCTOBER, 15), "CenaCracco")
        );

        //Vado a fare un prelievo
        controller.addTrasferimento(AccountImplementation.getInstance(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78),
                                    AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                                    400, "Prelievo", 1.50, tagListPrelievo, new Date(125, Calendar.OCTOBER, 13)
        );

        //Ora mi bastano i soldi
        controller.addTransaction(  AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                MovementType.DEBIT, 300, tagListCibo, new Date(125, Calendar.OCTOBER, 15), "CenaCracco");

        assertEquals(controller.getAccounts(a -> a.getName().equals("Cassa")).get(0).getBalance(), 119.94);


    }

    /**
     * Verifica il corretto funziomento della generazione di un report
     */
    @Test
    void generateReportBudget() {

        List<Tag> tagListCibo = new ArrayList<>();
        tagListCibo.add(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari"));

        List<Tag> tagListPrelievo = new ArrayList<>();
        tagListPrelievo.add(TagImplementation.getInstance("Spese Fianaziarie", "Prelievo"));

        //Aggiungo uscita
        controller.addTransaction(  AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                MovementType.DEBIT, 10.56, tagListCibo, new Date(), "Pizza con Amici"
        );

        //Vado a fare un prelievo
        controller.addTrasferimento(AccountImplementation.getInstance(AccountType.ASSETS, "Conto Bancario", "Conto Bancario", 2489.78),
                AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                400, "Prelievo", 1.50, tagListPrelievo, new Date(125, Calendar.OCTOBER, 13)
        );

        //cena dopo il prelievo
        controller.addTransaction(  AccountImplementation.getInstance(AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                MovementType.DEBIT, 300, tagListCibo, new Date(125, Calendar.OCTOBER, 15), "CenaCracco");


        BudgetReport bR = controller.getReportBudgets();
        assertEquals(3, bR.report().size());
        assertEquals(bR.report().get(TagImplementation.getInstance("Cibo & Bevande", "Generi alimentari")), -270.56);
    }

    /**
     * Verifica il corretto funzioanmento dei meccanismi per la creazione di Transazioni Schedulate
     */
    @Test
    void createScheduleTransaction() {

        controller.addScheduledTransaction("Iphone nuovo",
                                            AccountImplementation.getInstance( AccountType.ASSETS, "Cassa", "Portafoglio Personale", 30.50),
                                            10, 30,new Date(125, Calendar.OCTOBER, 28), 599.00
        );

    }
}
