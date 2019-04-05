/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.service.dto;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;

/**
 *
 * @author D070495
 */

public class BookDTO {
    
    private long id;
    private String title;
    private String owner_username;
    private String author;
    private Genre genre;
    private Medium medium;
    private int total_pages;
    private int current_page;
    private String comment = "";
    
    
    
    public BookDTO(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.owner_username = book.getOwner().getUsername();
        this.author = book.getAuthor();
        this.genre = book.getGenre();
        this.medium = book.getMedium();
        this.total_pages = book.getTotal_pages();
        this.current_page = book.getCurrent_page();
        this.comment = book.getComment();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_user_id) {
        this.owner_username = owner_user_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
    
    
    
    
    
}
