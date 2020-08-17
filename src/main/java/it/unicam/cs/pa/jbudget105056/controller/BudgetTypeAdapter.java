package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105056.model.Budget;
import it.unicam.cs.pa.jbudget105056.model.BudgetImplementation;
import it.unicam.cs.pa.jbudget105056.model.Tag;
import it.unicam.cs.pa.jbudget105056.model.TagImplementation;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione e serializzazione della classe Budget
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class BudgetTypeAdapter implements JsonSerializer<Budget>, JsonDeserializer<Budget> {

    /**
     * Il metodo in primo luogo converte il JsonElement in un JsonArray così da recuperarne i dati
     * Utilizza il metodo della classe Tag getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     */
    @Override
    public Budget deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Budget budget = new BudgetImplementation();
        JsonArray jsonArray = json.getAsJsonArray();
        for(JsonElement jsonElement : jsonArray){
            Tag tag;
            try{ tag = TagImplementation.getInstanceByID(jsonElement.getAsJsonObject().get("ID").getAsInt());}
            catch(IllegalArgumentException e){
                tag = TagImplementation.getInstance(jsonElement.getAsJsonObject().get("name").getAsString(), jsonElement.getAsJsonObject().get("description").getAsString());
            }
            budget.set(tag, jsonElement.getAsJsonObject().get("value").getAsDouble());
        }
        return budget;
    }

    /**
     * Il medoto provvede a creare un JsonArray che rapprenti l'istanza della classe Budget
     */
    @Override
    public JsonElement serialize(Budget src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<Tag, Double> entry : src.getBudgets().entrySet()){
            JsonObject jsonBudget = new JsonObject();
            jsonBudget.add("ID", new JsonPrimitive(entry.getKey().getID()));
            jsonBudget.add("name", new JsonPrimitive(entry.getKey().getName()));
            jsonBudget.add("description", new JsonPrimitive(entry.getKey().getDescription()));
            jsonBudget.add("value", new JsonPrimitive(entry.getValue()));
            jsonArray.add(jsonBudget);
        }
        return jsonArray;
    }
}
