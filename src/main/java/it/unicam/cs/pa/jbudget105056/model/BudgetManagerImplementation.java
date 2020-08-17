package it.unicam.cs.pa.jbudget105056.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementa L'interfaccia BudgetManager
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class BudgetManagerImplementation implements BudgetManager{

    /**
     *
     * @param ledger                    ledger da cui prendere le transazioni collegate ai tag presenti nel budget passato per argomento
     * @param budget                    budget
     * @return                          mappa che rappresenta il report generato dal budget e ledger passati per argomento
     * @throws IllegalStateException    se la lista dei budget o la lista delle trasazioni e vuota
     */
    @Override
    public BudgetReport generateReport(Ledger ledger, Budget budget) throws IllegalStateException{

        if(budget.getBudgets().isEmpty()) throw new IllegalStateException("Budget list is empty");
        if(ledger.getTransactions().isEmpty()) throw new IllegalStateException("Transactions list is empty");

        Map<Tag, Double> map = new HashMap<>();
        for(Tag tag : Objects.requireNonNull(budget, "Budget is Null").tags()){
            DoubleSummaryStatistics summary =
                    (Objects.requireNonNull(ledger, "Ledger is Null")
                                    .getTransactions(budget.getPredicate()))
                    .stream().parallel()
                    .filter(t -> t.tags().contains(tag))
                    .collect(Collectors.summarizingDouble(Transaction::getTotalAmount));
            map.put(tag, budget.get(tag) + summary.getSum());
        }
        return new BudgetReportImplementation(budget, map);
    }
}
