/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;
import static dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium.BUCH;
import static dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium.FACHLITERATUR;
import static dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium.HOERBUCH;
import static dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium.MAGAZIN;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.bookreminder.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.bookreminder.dashboard.ejb.DashboardSection;
import dhbwka.wwi.vertsys.javaee.bookreminder.dashboard.ejb.DashboardTile;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * EJB zur Definition der Dashboard-Kacheln für 'Bücher'.
 */
@Stateless(name = "books")
public class DashboardContent implements DashboardContentProvider {

    @EJB
    private GenreBean genreBean;

    @EJB
    private BookBean bookBean;
    
    @EJB
    private UserBean userBean; 

    /**
     * Vom Dashboard aufgerufenen Methode, um die anzuzeigenden Rubriken und
     * Kacheln zu ermitteln.
     *
     * @param sections Liste der Dashboard-Rubriken, an die die neuen Rubriken
     * angehängt werden müssen
     */
    @Override
    public void createDashboardContent(List<DashboardSection> sections) {
        // Zunächst einen Abschnitt mit einer Gesamtübersicht aller Bücher
        // in allen Genres erzeugen
        DashboardSection section = this.createSection(null);
        sections.add(section);

        // Anschließend je Genre einen weiteren Abschnitt erzeugen
        List<Genre> genres = this.genreBean.findAllSorted();

        for (Genre genre : genres) {
            section = this.createSection(genre);
            sections.add(section);
        }
    }

    /**
     * Hilfsmethode, die für die übergebene Buch-Genres eine neue Rubrik
     * mit Kacheln im Dashboard erzeugt. Je Medium wird eine Kachel
     * erzeugt. Zusätzlich eine Kachel für alle Bücher innerhalb des
     * jeweiligen Genres.
     *
     * Ist das Genre null, bedeutet dass, dass eine Rubrik für alle Bücher
     * aus allen Genres erzeugt werden soll.
     *
     * @param genre Buch-Genre, für das Kacheln erzeugt werden soll
     * @return Neue Dashboard-Rubrik mit den Kacheln
     */
    private DashboardSection createSection(Genre genre) {
        // Neue Rubrik im Dashboard erzeugen
        DashboardSection section = new DashboardSection();
        String cssClass = "";

        if (genre != null) {
            section.setLabel(genre.getName());
        } else {
            section.setLabel("Alle Genre");
            cssClass = "overview";
        }

        // Eine Kachel für alle Bücher in dieser Rubrik erzeugen
        DashboardTile tile = this.createTile(genre, null, "Alle", cssClass + " status-all", "calendar");
        section.getTiles().add(tile);

        // Ja Medium eine weitere Kachel erzeugen
        for (Medium medium : Medium.values()) {
            String cssClass1 = cssClass + " status-" + medium.toString().toLowerCase();
            String icon = "";

            switch (medium) {
                case BUCH:
                    icon = "doc-text";
                    break;
                case FACHLITERATUR:
                    icon = "rocket";
                    break;
                case MAGAZIN:
                    icon = "ok";
                    break;
                case HOERBUCH:
                    icon = "cancel";
                    break;
                //case POSTPONED:
                //    icon = "bell-off-empty";
                //    break;
            }

            tile = this.createTile(genre, medium, medium.getLabel(), cssClass1, icon);
            section.getTiles().add(tile);
        }

        // Erzeugte Dashboard-Rubrik mit den Kacheln zurückliefern
        return section;
    }

    /**
     * Hilfsmethode zum Erzeugen einer einzelnen Dashboard-Kachel. In dieser
     * Methode werden auch die in der Kachel angezeigte Anzahl sowie der Link,
     * auf den die Kachel zeigt, ermittelt.
     *
     * @param genre
     * @param medium
     * @param label
     * @param cssClass
     * @param icon
     * @return
     */
    private DashboardTile createTile(Genre genre, Medium medium, String label, String cssClass, String icon) {
        int amount = bookBean.searchAllBooksOfUser(null, genre, medium, this.userBean.getCurrentUser()).size();
        String href = "/app/books/list/";

        if (genre != null) {
            href = WebUtils.addQueryParameter(href, "search_genre", "" + genre.getId());
        }

        if (medium != null) {
            href = WebUtils.addQueryParameter(href, "search_medium", medium.toString());
        }

        DashboardTile tile = new DashboardTile();
        tile.setLabel(label);
        tile.setCssClass(cssClass);
        tile.setHref(href);
        tile.setIcon(icon);
        tile.setAmount(amount);
        tile.setShowDecimals(false);
        return tile;
    }

}
