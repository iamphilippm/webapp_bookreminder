/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.book.web;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.BookBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.ejb.TaskBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.jpa.Task;
import dhbwka.wwi.vertsys.javaee.bookreminder.tasks.jpa.TaskStatus;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author D070495
 */
@WebServlet(name = "BookListServlet", urlPatterns = {"/app/tasks/list/"})
public class BookListServlet extends HttpServlet {
 @EJB
    private GenreBean genreBean;
     
    @EJB
    private BookBean bookBean;
    
    @EJB
    private UserBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Genren und Medien für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("mediums", Medium.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchGenre = request.getParameter("search_genre");
        String searchMedium = request.getParameter("search_medium");

        // Anzuzeigende Aufgaben suchen
        Genre genre = null;
        Medium medium = null;

        if (searchGenre != null) {
            try {
                genre = this.genreBean.findById(Long.parseLong(searchGenre)); 
            } catch (NumberFormatException ex) {
                genre = null;
            }
        }

        if (searchMedium != null) {
            try {
                medium = Medium.valueOf(searchMedium); 
            } catch (IllegalArgumentException ex) {
                medium = null;
            }

        }

        //List<Book> books = this.bookBean.search(searchText, genre, medium);
        //List<Book> books2 = this.bookBean.findByUsername(this.userBean.getCurrentUser().getUsername());
        List<Book> books = this.bookBean.searchAllBooksOfUser(searchText, genre, medium, this.userBean.getCurrentUser().getUsername());
        
        
        request.setAttribute("books", books);  
       
        
        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/tasks/task_list.jsp").forward(request, response);
    }
}
