/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package service.customFacades;

import dhbwka.wwi.vertsys.javaee.bookreminder.book.ejb.BookBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa.Book;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import service.dto.BookDTO;

/**
 *
 * @author D070495
 */

@Stateless
public class BookFacade {
    
    
    @EJB
    BookBean bookBean;
    
    public List<BookDTO> findAll(){
        List<Book> books = bookBean.findAll();
        return books.stream().map((book) -> {
            return new BookDTO(book);
        }).collect(Collectors.toList());
    }
    
    public List<BookDTO> searchBooks(String search){
        List<Book> books = bookBean.searchTitle(search);
        return books.stream().map((book) -> {
            return new BookDTO(book);
        }).collect(Collectors.toList());
    }
    
}
