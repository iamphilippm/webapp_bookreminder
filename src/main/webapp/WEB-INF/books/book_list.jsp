<%-- 
    Document   : book_list
    Created on : Mar 27, 2019, 11:19:53 AM
    Author     : D070694
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Liste der Bücher
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/book_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/books/book/"/>">Buch anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/books/genres/"/>">Genre bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/editProfile/"/>">Profil bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_genre">
                <option value="">Alle Genres</option>

                <c:forEach items="${genres}" var="genre">
                    <option value="${genre.id}" ${param.search_genre == genre.id ? 'selected' : ''}>
                        <c:out value="${genre.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_medium">
                <option value="">Alle Medienarten</option>

                <c:forEach items="${mediums}" var="medium">
                    <option value="${medium}" ${param.search_medium == medium ? 'selected' : ''}>
                        <c:out value="${medium.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button  type="submit">
                Suchen
            </button>
        </form>

        <c:choose>
            <c:when test="${empty books}">
                <p>
                    Es wurden keine Bücher gefunden.
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.bookreminder.common.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Titel</th>
                            <th>Autor</th>
                            <th>Genre</th>
                            <th>Medium</th>
                            <th>Seitenanzahl</th>
                        </tr>
                    </thead>
                    <c:forEach items="${books}" var="book">
                        <tr>
                            <td>
                            <a href="<c:url value="/app/books/book/${book.id}"/>">
                                    <c:out value="${book.title}"/>
                            </td>
                            <td>
                                <c:out value="${book.author}"/> 
                            </td>
                            <td>
                                <c:out value="${book.genre.name}"/>
                            </td>
                            <td>
                                <c:out value="${book.medium}"/>
                            </td>
                            <td>
                                <c:out value="${book.current_page}"/>
                                /
                                <c:out value="${book.total_pages}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>