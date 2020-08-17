package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione e serializzazione della classe ScheduledTransaction
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class ScheduledTransactionTypeAdapter implements JsonSerializer<ScheduledTransaction>, JsonDeserializer<ScheduledTransaction> {

    /**
     * Il medoto provvede a creare un JsonObject che rapprenti l'istanza della classe ScheduledTransaction
     * Utilizza il TypeToken appropriato per serializzare la lista delle transazioni
     * Viene salvati soltanto l'ID del riferimento all'istanza della classe Account per non produrre un eventuale loop
     */
    @Override
    public JsonElement serialize(ScheduledTransaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ID", new JsonPrimitive(src.getID()));
        jsonObject.add("description", new JsonPrimitive((src.getDescription())));
        jsonObject.add("accountID", new JsonPrimitive(src.getAccount().getID()));
        jsonObject.add("transactions", context.serialize(src.getTransactions(), new TypeToken<List<Transaction>>() {}.getType()));
        return jsonObject;
    }

    /**
     * il metodo in primo luogo utilizza il metodo della classe ScheduledTransaction getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     * di seguito provvede ad assegnare la lista dei movimenti utilizzando il TypeToken appropriato
     */
    @Override
    public ScheduledTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ScheduledTransaction st = null;
        try{
            Account a = AccountImplementation.getInstanceByID(jsonObject.get("accountID").getAsInt());
            st = ScheduledTransactionImplementation.getInstance(jsonObject.get("description").getAsString(),
                                                                    AccountImplementation.getInstanceByID(jsonObject.get("accountID").getAsInt()));
            st.setTransactionsList(context.deserialize(jsonObject.get("transactions"), new TypeToken<List<Transaction>>() {}.getType()));
        }
        catch (IllegalArgumentException i ){ i.printStackTrace(); }
        return st;
    }
}
