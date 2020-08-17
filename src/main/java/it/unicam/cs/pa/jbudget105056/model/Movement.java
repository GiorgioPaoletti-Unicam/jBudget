package it.unicam.cs.pa.jbudget105056.model;

import java.util.Date;
import java.util.List;

/**
 * l’interfaccia verra implementata dalle classi responsabili della gestione del singolo movimento.
 * Il movimento e associato a una transazione che ne gestisce la data e la lista dei tag (categoria).
 * La classe ha la responsabilita di gestire la modifica delle informazioni associate al movimento:
 * descrizione, importo, account associato e il tipo di movimento.
 *
 * @author      Giorgio Paoletti
 *              giorgio.paoletti@studenti.unicam.it
 *              matricola: 105056
 *
 * @version     Terza Consegna 18/07/2020
 *
 */
public interface Movement{

    String getDescription();

    MovementType type();

    double getAmount();

    Transaction getTransaction();

    Account getAccount();

    int getID();

    Date getDate();

    List<Tag> tags();

    void addTag(Tag t);

    void removeTag(Tag t);

    void setAccount(Account a);

    void setTransaction(Transaction t);

}
