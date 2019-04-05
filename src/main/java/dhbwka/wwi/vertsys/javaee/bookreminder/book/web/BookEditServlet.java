package dhbwka.wwi.vertsys.javaee.bookreminder.book.web;

/**
 *
 * @author D070694
 */

/*
Dieses Servlet regiert auf den HTTP Request zum Anlegen eines neuen Buches.
Es stellt den Content (das Formular) bereit, in dem der User ein neues Buch anlegen
oder vorhandene bearbeiten bzw. löschen kann.
Deshalb sind hier auch die Methoden doGet() und doPost implementiert,
die klassischerweise zur Browserkommunikation dienen.
Einzelne Methoden sind inklusive Bedingungen (Errorhandling) programmiert.
Letztendlich stellt das Servlet die Daten für das bookedit.jsp bereit.
*/

import dhbwka.wwi.vertsys.javaee.bookreminder.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.BookBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/app/books/book/*")
public class BookEditServlet extends HttpServlet {

    @EJB
    BookBean bookBean;

    @EJB
    GenreBean genreBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Genres und Medien für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("mediums", Medium.values());

        // Zu bearbeitendes Buch einlesen
        HttpSession session = request.getSession();

        Book book = this.getRequestedBook(request);
        request.setAttribute("edit", book.getId() != 0);

        if (session.getAttribute("book_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("book_form", this.createBookForm(book));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/books/book_edit.jsp").forward(request, response);

        session.removeAttribute("book_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveBook(request, response);
                break;
            case "delete":
                this.deleteBook(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neues oder vorhandenes Buch speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String genre = request.getParameter("book_genre");
        String author = request.getParameter("book_author");
        String sumpages = request.getParameter("book_sumpages");
        String curpages = request.getParameter("book_curpages");
        String medium = request.getParameter("book_medium");
        String title = request.getParameter("book_title");
        String comment = request.getParameter("book_comment");

        Book book = this.getRequestedBook(request);

        if (genre != null && !genre.trim().isEmpty()) {
            try {
                book.setGenre(this.genreBean.findById(Long.parseLong(genre)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        } else {
            errors.add("Bitte wähle eine Genre aus.");
        }

        try {
            book.setMedium(Medium.valueOf(medium));
        } catch (IllegalArgumentException ex) {
            errors.add("Das ausgewählte Medium ist nicht vorhanden.");

        }

        if (title != "") {
            book.setTitle(title);
        } else {
            errors.add("Bitte gib einen Titel ein.");
        }

        if (author != "") {
            book.setAuthor(author);
        } else {
            errors.add("Bitte gib einen Autor ein.");
        }

        book.setComment(comment);

        try {
            int sumpages_int = Integer.parseInt(sumpages);
            int curpages_int = Integer.parseInt(curpages);

            if (curpages_int > sumpages_int) {
                errors.add("Die aktuelle Seitenzahl darf nicht größer sein, als die maximale Seitenzahl.");
            } else {
                book.setTotal_pages(sumpages_int);
                book.setCurrent_page(curpages_int);
            }
        } catch (NumberFormatException nfe) {
            errors.add("Die Seitenzahlen dürfen nicht leer sein.");
        }

        this.validationBean.validate(book, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.bookBean.update(book);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/books/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("book_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandenes Buch löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Book book = this.getRequestedBook(request);
        this.bookBean.delete(book);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/books/list/"));
    }

    private Book getRequestedBook(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Datensatz angelegt werden soll
        Book book = new Book();
        book.setOwner(this.userBean.getCurrentUser());

        // ID aus der URL herausschneiden
        String bookId = request.getPathInfo();

        if (bookId == null) {
            bookId = "";
        }

        bookId = bookId.substring(1);

        if (bookId.endsWith("/")) {
            bookId = bookId.substring(0, bookId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            book = this.bookBean.findById(Long.parseLong(bookId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return book;
    }

    private FormValues createBookForm(Book book) {
        Map<String, String[]> values = new HashMap<>();
        int total = book.getTotal_pages();
        int current = book.getCurrent_page();

        values.put("book_owner", new String[]{
            book.getOwner().getUsername()
        });

        if (book.getGenre() != null) {
            values.put("book_genre", new String[]{
                "" + book.getGenre().getId()
            });
        }

        if (book.getMedium() != null) {
            values.put("book_medium", new String[]{
                "" + book.getMedium().toString()
            });
        }

        values.put("book_title", new String[]{
            book.getTitle()
        });

        values.put("book_author", new String[]{
            book.getAuthor()
        });

        values.put("book_sumpages", new String[]{
            String.valueOf(book.getTotal_pages())
        });

        values.put("book_curpages", new String[]{
            String.valueOf(book.getCurrent_page())
        });

        values.put("book_comment", new String[]{
            book.getComment()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
