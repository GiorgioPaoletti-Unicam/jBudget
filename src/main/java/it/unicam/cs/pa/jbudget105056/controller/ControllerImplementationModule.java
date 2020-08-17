package it.unicam.cs.pa.jbudget105056.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import it.unicam.cs.pa.jbudget105056.model.Budget;
import it.unicam.cs.pa.jbudget105056.model.BudgetImplementation;
import it.unicam.cs.pa.jbudget105056.model.Ledger;
import it.unicam.cs.pa.jbudget105056.model.LedgerImplementation;

import java.io.IOException;

/**
 * Rappresenta il Modulo da utilizzare per istanziare la classe ControllerImplementation
 * utilizzando il framework di Dipendecy Injection offerto da google Guice
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 */
public class ControllerImplementationModule extends AbstractModule {

    /**
     *
     * @return istanza di una classe che implementa Ledger
     */
    @Provides
    @LedgerInject
    static Ledger provideLedger(){
        return new LedgerImplementation();
    }


    /**
     *
     * @return istanza di una classe che implementa Budget
     */
    @Provides
    @BudgetInject
    static Budget provideBudget(){
        return new BudgetImplementation();
    }

    /**
     *
     * @return istanza di una classe che implementa LoggerManager
     */
    @Provides
    @LoggerManagerInject
    static LoggerManager provideLoggerManager() throws IOException {
        return new LoggerManagerImplementation();
    }

    /**
     *
     * @return istanza di una classe che implementa Saver
     */
    @Provides
    @SaverInject
    static Saver provideSaver(){
        return new SaverGSon();
    }

    /**
     *
     * @return istanza di una classe che implementa Loader
     */
    @Provides
    @LoaderInject
    static Loader provideLoader(){
        return new LoaderGSon();
    }

}

@interface LedgerInject {}

@interface BudgetInject {}

@interface LoggerManagerInject {}

@interface LoaderInject {}

@interface SaverInject {}


