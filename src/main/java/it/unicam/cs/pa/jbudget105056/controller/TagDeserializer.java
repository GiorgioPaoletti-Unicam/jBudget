package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105056.model.Tag;
import it.unicam.cs.pa.jbudget105056.model.TagImplementation;

import java.lang.reflect.Type;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione della classe Tag
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class TagDeserializer implements JsonDeserializer<Tag> {

    /**
     * il metodo in primo luogo utilizza il metodo della classe Tag getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     * di seguito provvede ad assegnare la lista dei movimenti utilizzando il TypeToken appropriato
     */
    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return TagImplementation.getInstance(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());
    }
}
