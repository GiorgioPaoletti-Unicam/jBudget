package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * implementa l'interfaccia Saver utilizzando le JSon google API
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class SaverGSon implements Saver{

    private Gson gson;

    /**
     *
     * @param ledger    Instanza della classe Ledger da savare sul file Ledger.json
     */
    @Override
    public void saveLedger(Ledger ledger) {
        gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Movement.class, new MovementTypeAdapter())
                .registerTypeAdapter(ScheduledTransaction.class, new ScheduledTransactionTypeAdapter())
                .create();
        WriteFile("Ledger.json", ledger);
    }

    /**
     *
     * @param budget    Instanza della classe Budget da savare su file Budget.json
     */
    @Override
    public void saveBudget(Budget budget) {
        gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BudgetImplementation.class, new BudgetTypeAdapter())
                .create();
        WriteFile("Budget.json", budget);
    }

    /**
     *
     * @param fileName  Nome file json su cui salvare l'istanza passata per parametro
     * @param obj       Instanza da salvare su file json
     * @param <T>       Classe dell'istanza da savare su file json
     */
    private <T> void WriteFile(String fileName, T obj){
        Path path = Paths.get("src", "main", "resources", "data", fileName).toAbsolutePath();
        try {
            if(!Files.exists(path)) Files.createFile(path);
            Writer writer = new FileWriter(new File(path.toString()));
            gson.toJson(obj, writer);
            writer.close();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
}
