/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.tasks.web;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.BookBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.ejb.TaskBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.jpa.Task;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anzeigen und Bearbeiten der Kategorien. Die Seite besitzt ein
 * Formular, mit dem ein neue Kategorie angelegt werden kann, sowie eine Liste,
 * die zum Löschen der Kategorien verwendet werden kann.
 */
@WebServlet(urlPatterns = {"/app/tasks/categories/"})
public class GenreListServlet extends HttpServlet {

    @EJB
    GenreBean genreBean;

    @EJB
    BookBean bookBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alle vorhandenen Kategorien ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/tasks/category_list.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("categories_form");
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
            case "create":
                this.createGenre(request, response);
                break;
            case "delete":
                this.deleteGenres(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue Kategorie anlegen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void createGenre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        String name = request.getParameter("name");

        Genre genre = new Genre(name);
        List<String> errors = this.validationBean.validate(genre);

        // Neue Kategorie anlegen
        if (errors.isEmpty()) {
            this.genreBean.saveNew(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        if (!errors.isEmpty()) {
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("categories_form", formValues);
        }

        response.sendRedirect(request.getRequestURI());
    }

    /**
     * Aufgerufen in doPost(): Markierte Kategorien löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteGenres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Markierte Kategorie IDs auslesen
        String[] genreIds = request.getParameterValues("genre");

        if (genreIds == null) {
            genreIds = new String[0];
        }

        // Kategorien löschen
        for (String genreId : genreIds) {
            // Zu löschende Kategorie ermitteln
            Genre genre;

            try {
                genre = this.genreBean.findById(Long.parseLong(genreId));
            } catch (NumberFormatException ex) {
                continue;
            }

            if (genre == null) {
                continue;
            }

            // Bei allen betroffenen Aufgaben, den Bezug zur Kategorie aufheben
            List<Book> books = genre.getBooks();

            if (books != null) {
                books.forEach((Book book) -> {
                    book.setGenre(null);
                    this.bookBean.update(book);
                });
            }

            // Und weg damit
            this.genreBean.delete(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        response.sendRedirect(request.getRequestURI());
    }

}
