package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105056.model.Movement;
import it.unicam.cs.pa.jbudget105056.model.MovementImplementation;
import it.unicam.cs.pa.jbudget105056.model.MovementType;

import java.lang.reflect.Type;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione e serializzazione della classe Movement
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class MovementTypeAdapter implements JsonSerializer<Movement>, JsonDeserializer<Movement> {

    /**
     * il metodo in primo luogo utilizza il metodo della classe Movement getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     * di seguito provvede ad assegnare la lista dei movimenti utilizzando il TypeToken appropriato
     */
    @Override
    public Movement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Movement m;
        try{ m = MovementImplementation.getInstanceByID(jsonObject.get("ID").getAsInt()); }
        catch (IllegalArgumentException e){
            m = MovementImplementation.getInstance(jsonObject.get("description").getAsString(),
                    jsonObject.get("amount").getAsDouble(),
                    context.deserialize(jsonObject.get("type"), MovementType.class)
            );
        }
        return m;
    }

    /**
     * Il medoto provvede a creare un JsonObject che rapprenti l'istanza della classe Movement
     */
    @Override
    public JsonElement serialize(Movement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ID", new JsonPrimitive(src.getID()));
        jsonObject.add("description", new JsonPrimitive((src.getDescription())));
        jsonObject.add("amount", new JsonPrimitive(src.getAmount()));
        jsonObject.add("type", context.serialize(src.type(), MovementType.class));
        return jsonObject;
    }
}
