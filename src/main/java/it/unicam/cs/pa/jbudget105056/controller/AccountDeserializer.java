package it.unicam.cs.pa.jbudget105056.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.pa.jbudget105056.model.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * La classe ha la responsabilita di descrivere le modalita di deserializzazione della classe Account
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class AccountDeserializer implements JsonDeserializer<Account> {

    /**
     * il metodo in primo luogo utilizza il metodo della classe Account getInstanceByID per recuperare tramite l'ID un eventuale istanza gia creata
     * in caso contrario provede a crearne una nuova
     * di seguito provvede ad assegnare la lista dei movimenti utilizzando il TypeToken appropriato
     */
    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Account a;
        try{ a = AccountImplementation.getInstanceByID(jsonObject.get("ID").getAsInt()); }
        catch (IllegalArgumentException e){
            a = AccountImplementation.getInstance(AccountType.valueOf(jsonObject.get("type").getAsString()),
                                            jsonObject.get("name").getAsString(),
                                            jsonObject.get("description").getAsString(),
                                            jsonObject.get("opening").getAsDouble()
            );
        }
        List<Movement> movementList =
                context.deserialize(jsonObject.get("movements"), new TypeToken<List<Movement>>(){}.getType());
        for(Movement m : movementList) a.addMovement(m);
        return a;
    }
}
