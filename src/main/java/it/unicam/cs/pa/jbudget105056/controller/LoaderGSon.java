package it.unicam.cs.pa.jbudget105056.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * implementa l'interfaccia Loader utilizzando le JSon google API
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class LoaderGSon implements Loader{

    private Gson gson;

    /**
     *
     * @return l'istanza della classe Ledger caricata dal file Ledger.json
     */
    @Override
    public Ledger loadLedger() throws IOException, IllegalStateException{
        gson =  new GsonBuilder()
                .registerTypeAdapter(Movement.class, new MovementTypeAdapter())
                .registerTypeAdapter(Transaction.class, new TransactionDeserializer())
                .registerTypeAdapter(Account.class, new AccountDeserializer())
                .registerTypeAdapter(Tag.class, new TagDeserializer())
                .registerTypeAdapter(ScheduledTransaction.class, new ScheduledTransactionTypeAdapter())
                .create();
        return gson.fromJson(ReadFile("data/Ledger.json"), LedgerImplementation.class);
    }

    /**
     *
     * @return l'istanza della classe Budget caricata dal file Budget.json
     */
    @Override
    public Budget loadBudget() throws IOException, IllegalStateException{
        gson =  new GsonBuilder()
                .registerTypeAdapter(BudgetImplementation.class, new BudgetTypeAdapter())
                .create();
        return gson.fromJson(ReadFile("data/Budget.json"), BudgetImplementation.class);
    }

    /**
     *
     * @param fileName  Nome del file json da cui prendere i datui da restituire
     * @return          restituisce l'instanza della classe letta dal file json
     */
    private String ReadFile(String fileName) throws IOException, IllegalStateException{
        Path path = Paths.get("src", "main", "resources", fileName).toAbsolutePath();
        String jsonString = "";
        if(!Files.exists(path)) throw new FileNotFoundException(fileName + "not Exist");
        jsonString = Files.readString(path);
        if(jsonString.length() == 0) throw new IllegalStateException(fileName + "is Empty");
        return jsonString;
    }
}
