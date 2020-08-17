package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione della classe Transaction
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class TransactionDeserializer implements JsonDeserializer<Transaction> {

    /**
     * il metodo in primo luogo utilizza il metodo della classe Transaction getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     * di seguito provvede ad assegnare la lista dei movimenti utilizzando il TypeToken appropriato
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Transaction t;
        try { t = TransactionImplementation.getInstanceByID(jsonObject.get("ID").getAsInt()); }
        catch (IllegalArgumentException e) {
            t = TransactionImplementation.getInstance(context.deserialize(jsonObject.get("date"), Date.class));
        }
        try {
            List<Tag> tagList =
                    context.deserialize(jsonObject.get("tags"), new TypeToken<List<Tag>>() {}.getType());
            for (Tag tag : tagList) t.addTag(tag);


            List<Movement> movementList =
                    context.deserialize(jsonObject.get("movements"), new TypeToken<List<Movement>>() {}.getType());
            for (Movement m : movementList) t.addMovement(m);
        }
        catch (Exception e) {return t;}
        return t;
    }
}