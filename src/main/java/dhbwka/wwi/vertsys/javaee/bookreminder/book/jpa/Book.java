/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

/*
Das ist die eigentliche Datenbank-Klasse (Entity) "Buch".
Hier werden relevante Felder definiert, mindestens ein Konstruktor geschrieben,
sowie die Setter und Getter für die einzelnen Felder implementiert.
Die Felder repräsentieren die spätere Datenbank für die Entity "Book".
*/

package dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa;

import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;
import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "buch_ids")
    @TableGenerator(name = "buch_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @Column(length = 64)
    private String title;
    
   // @Column(length = 64)
    @ManyToOne
    @NotNull
    private User owner;

    @Column(length = 64)
    private String author;

    @ManyToOne
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Medium medium;

    @Column(length = 64)
    @NotNull(message = "Das Buch muss eine Seitenzahl haben.")
    private int total_pages;

    @Column(length = 64)
    @NotNull(message = "Du musst angeben, wie weit du schon gelesen hast.")
    private int current_page;

    @Lob
    private String comment = "";

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Book() {
    }

    public Book(String title, User owner, String author, Genre genre, Medium medium, int total_pages, int current_page) {
        this.title = title;
        this.owner = owner;
        this.author = author;
        this.genre = genre;
        this.medium = medium;
        this.total_pages = total_pages;
        this.current_page = current_page;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public User getOwner() {
        return owner;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public Medium getMedium() {
        return medium;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public String getComment(){
        return comment;
    }    
}
