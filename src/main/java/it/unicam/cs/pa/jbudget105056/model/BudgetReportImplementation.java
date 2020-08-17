package it.unicam.cs.pa.jbudget105056.model;

import java.util.*;

/**
 * Implementa l'interfaccia BudgetReport
 *
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class BudgetReportImplementation implements BudgetReport{

    private Map<Tag, Double> reports;
    private Budget budget;

    /**
     *
     * @param budget    budget collegata al report
     * @param reports   mappa che rappresenta il report
     */
    public BudgetReportImplementation(Budget budget, Map<Tag, Double> reports) {
        this.budget = Objects.requireNonNull(budget, " Budget is Null");
        this.reports = Objects.requireNonNull(reports, " Reports is Null");
    }

    /**
     *
     * @return  list dei tag del report
     */
    @Override
    public List<Tag> tags() {
        return new ArrayList<>(reports.keySet());
    }

    /**
     *
     * @return mappa che rappresenta il report
     */
    @Override
    public Map<Tag, Double> report() {
        return reports;
    }

    /**
     *
     * @return  il budget collegato al report
     */
    @Override
    public Budget getBudget() {
        return budget;
    }

    /**
     *
     * @param tag                       tag di cui si vuole sapere il valore reale
     * @return                          valore reale collegato al tag passato per argomento
     * @throws IllegalArgumentException se il tag non e presente nel report
     */
    @Override
    public double get(Tag tag) throws IllegalArgumentException {
        if(!reports.containsKey(Objects.requireNonNull(tag, "Tag is null")))
            throw new IllegalArgumentException("Tag non exit");
        return reports.get(tag);
    }

}
