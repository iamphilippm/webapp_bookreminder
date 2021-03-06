package dhbwka.wwi.vertsys.javaee.bookreminder.book.web;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.BookBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.GenreBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Genre;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Medium;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author D070495
 */

/*
Dieses Servlet dient als Controller für die Listenansicht der Bücher. Das JSP stellt dem Benutzer
über verschiedene Felder die Möglichkeit bereit Bücher zu suchen bzw. zu filtern. Dieses Servlet
reagiert auf diese Benutzerinteraktionen und liefert entsprechende Ergebnisse für die Listenansicht an das 
booklist.jsp. Demzufolge sind hier die doGet() und doPost() Methoden implementiert, die auf Browseranfragen reagieren.
*/

@WebServlet(name = "BookListServlet", urlPatterns = {"/app/books/list/"})
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

        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("mediums", Medium.values());

        String searchText = request.getParameter("search_text");
        String searchGenre = request.getParameter("search_genre");
        String searchMedium = request.getParameter("search_medium");

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

        List<Book> books = this.bookBean.searchAllBooksOfUser(searchText, genre, medium, this.userBean.getCurrentUser());
        
        
        request.setAttribute("books", books);  
       
        
        
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/books/book_list.jsp").forward(request, response);
    }
}
