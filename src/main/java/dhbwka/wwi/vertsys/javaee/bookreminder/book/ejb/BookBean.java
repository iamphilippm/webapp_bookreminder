/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für 'Bücher'
 */
@Stateless
@RolesAllowed("app-user")
public class BookBean extends EntityBean<Book, Long> { 
   
    
    
    public BookBean() {
        super(Book.class);
    }
    
    /**
     * Alle Bücher eines Benutzers, nach Titel sortiert zurückliefern.
     * @param username Nickname
     * @return Alle Aufgaben des Benutzers
     */
    public List<Book> findByUsername(String username) {
        return em.createQuery("SELECT b FROM Book b WHERE b.owner = :username ORDER BY b.title")
                 .setParameter("username", username)
                 .getResultList();
        
    }
    
    /**
     * Suche nach Büchern anhand ihrer Beschreibung, Genre und Medium.
     * 
     * Anders als in der Vorlesung behandelt, wird die SELECT-Anfrage hier
     * mit der CriteriaBuilder-API vollkommen dynamisch erzeugt.
     * 
     * @param search In der Kurzbeschreibung enthaltener Text (optional)
     * @param genre Genre (optional)
     * @param medium Medium (optional)
     * @return Liste mit den gefundenen Büchern
     */
    public List<Book> search(String search, Genre genre, Medium medium) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Book t
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> from = query.from(Book.class);
        query.select(from);

        // ORDER BY title
        query.orderBy(cb.asc(from.get("title")));
        
        // WHERE t.title LIKE :search
        Predicate p = cb.conjunction();
        
        if (search != null && !search.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + search + "%"));
            query.where(p);
        }
        
        // WHERE t.genre = :genre
        if (genre != null) {
            p = cb.and(p, cb.equal(from.get("genre"), genre));
            query.where(p);
        }
        
        // WHERE t.medium = :medium
        if (medium != null) {
            p = cb.and(p, cb.equal(from.get("medium"), medium));
            query.where(p);
        }
        
        List<Book> books = em.createQuery(query).getResultList();
        return books;
    }
    
    public List<Book> searchAllBooksOfUser(String search,Genre genre, Medium medium, User username){
                // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Book t
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> from = query.from(Book.class);
        query.select(from);

        // ORDER BY title
        query.orderBy(cb.asc(from.get("title")));
        
        // WHERE t.title LIKE :search
        Predicate p = cb.conjunction();
        
        if (search != null && !search.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("title"), "%" + search + "%"));
            query.where(p);
        }
        
        // WHERE t.genre = :genre
        if (genre != null) {
            p = cb.and(p, cb.equal(from.get("genre"), genre));
            query.where(p);
        }
        
        // WHERE t.medium = :medium
        if (medium != null) {
            p = cb.and(p, cb.equal(from.get("medium"), medium));
            query.where(p);
        }
        
        p = cb.and(p, cb.equal(from.get("owner").get("username") , username.getUsername()));
        query.where(p);
        
        List<Book> books = em.createQuery(query).getResultList();
        return books;
        
     
    }
}
