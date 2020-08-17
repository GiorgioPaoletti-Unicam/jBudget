package it.unicam.cs.pa.jbudget105056.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.*;

/**
 * Implementa l'interfaccia LogManager
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public class LoggerManagerImplementation implements LoggerManager{

    private LogManager lgMan;
    private Logger Logr;
    private FileHandler fh;

    /**
     * Il metodo crea un istanza di un LogManager, crea un FileHandler a cui assegna il un SimpleFormqtter
     * I dati vengono salvati nella cartella log delle risorse
     */
    public LoggerManagerImplementation() throws IOException {
        this.lgMan = LogManager.getLogManager();

        String LoggerName = Logger.GLOBAL_LOGGER_NAME;
        Logr = lgMan.getLogger(LoggerName);

        Logr.setLevel(Level.ALL);

        fh = new FileHandler(Paths.get("src", "main", "resources", "log", "Log_%g.log" ).toAbsolutePath().toString(), 50000, 10, true
        );

        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.FINE);

        Logr.addHandler(fh);
    }

    /**
     *
     * @param level                     livello del messaggio del log
     * @param message                   testo del messaggio del log
     * @throws IllegalArgumentException se il messaggio passato e vuoto
     */
    @Override
    public void setMessage(Level level, String message) throws IllegalArgumentException{
        if(message.length() == 0) throw new IllegalArgumentException("Message is Empty");
        Logr.log(   Objects.requireNonNull(level, "Level is Null"),
                    Objects.requireNonNull(message, "Message is Null") + "\n"
        );
    }
}
