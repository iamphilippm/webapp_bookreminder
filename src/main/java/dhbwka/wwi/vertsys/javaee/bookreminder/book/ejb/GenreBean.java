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
Fachliche Anwendungslogik zur Erstellung der Genres-Liste. Es werden Inhalte aus der Genre-DB
ausgelesen und bereitgestellt. Die eigentliche View der Daten wird hier nicht programmiert.
*/

package dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.EntityBean;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Genres.
 */
@Stateless
@RolesAllowed("app-user")
public class GenreBean extends EntityBean<Genre, Long> {

    public GenreBean() {
        super(Genre.class);
    }

    /**
     * Auslesen aller Genres, alphabetisch sortiert.
     *
     * @return Liste mit allen Genres
     */
    public List<Genre> findAllSorted() {
        return this.em.createQuery("SELECT g FROM Genre g ORDER BY g.name").getResultList();
    }
}
