package it.unicam.cs.pa.jbudget105056.model;

import java.util.*;
import java.util.function.Predicate;

/**
 * Implemnta l'interfaccia Budget
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class BudgetImplementation implements Budget{

    private Map<Tag, Double> budgets;

    public BudgetImplementation() {
        budgets = new HashMap<>();
    }

    /**
     *
     * @return lista dei tag a cui e stato collegato un valore nella lista dei budget
     */
    @Override
    public List<Tag> tags() {
        return new ArrayList<>(getBudgets().keySet());
    }

    /**
     *
     * @param tag                       Tag a cui collegare un valore aspettato
     * @param expected                  Valore aspettato da collegare a un tag
     * @throws IllegalArgumentException se il tag passato per parametro e gia stato collegato a un valore
     */
    @Override
    public synchronized void set(Tag tag, double expected) throws IllegalArgumentException {
        if(budgets.containsKey(Objects.requireNonNull(tag, "Tag is Null")))
            throw new IllegalArgumentException("Tag already exit");
        if(expected < 0) throw new IllegalArgumentException("Expected is negative");
        budgets.put(tag, expected);
    }

    /**
     *
     * @param tag                       tag di cui si vuole conoscere il valore aspettato
     * @return                          valore aspettato collegato al tag passato per parametro
     * @throws IllegalArgumentException se il il tag passato per argomento non e presente nella lista dei budget
     * @throws IllegalStateException    se la lista dei budget e vuota
     */
    @Override
    public double get(Tag tag) throws IllegalArgumentException, IllegalStateException {
        if(budgets.isEmpty()) throw new IllegalStateException("Budget list is Empty");
        if(!budgets.containsKey(Objects.requireNonNull(tag, "Tag is Null")))
            throw new IllegalArgumentException("Tag non exit");
        return budgets.get(tag);
    }

    /**
     *
     * @return ritorna un predicato che indica i tag a cui e stato collegato un valore aspettato partendo da una lista di tag più grande
     */
    @Override
    public Predicate<? super Transaction> getPredicate() {
        return t -> !Collections.disjoint(t.tags(), tags());
    }

    /**
     *
     * @return  mappa che collega i tag e i corrispettivi valori aspettati
     */
    @Override
    public Map<Tag, Double> getBudgets() {
        return budgets;
    }

    /**
     *
     * @param t                         tag che si vuole eliminare dalla lista dei budget
     * @throws IllegalStateException    se la lista dei budget e vuota
     * @throws IllegalArgumentException se il tag passato per parametro non e collagato a nessun valore aspettato
     */
    @Override
    public synchronized void removeBudget(Tag t) throws IllegalStateException, IllegalArgumentException{
        if(budgets.isEmpty()) throw new IllegalStateException("Budget list is empty");
        if(!budgets.containsKey(t)) throw new IllegalArgumentException();
        budgets.remove(t);
    }

    @Override
    public String toString() {
        StringBuilder budgetsString = new StringBuilder();
        for(Map.Entry<Tag, Double> entry : budgets.entrySet())
            budgetsString.append(" { TagID: ").append(entry.getKey().getID())
                            .append(", TagNAme: ").append(entry.getKey().getName())
                            .append(", TagDescription: ").append(entry.getKey().getDescription())
                            .append(" }").append(", ExpectedValue: ")
                            .append(entry.getValue());

        return "BudgetImplementation{" +
                "budgets=" + budgetsString +
                '}';
    }
}
