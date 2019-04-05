<%-- 
    Document   : book_edit
    Created on : Mar 23, 2019, 5:25:33 PM
    Author     : D070694
--%>

<!-- Anzeige des Formulars zum Anlegen und Bearbeiten eines Buches -->

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Buch bearbeiten
            </c:when>
            <c:otherwise>
                Buch anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/book_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/books/list/"/>">Liste</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/books/genres/"/>">Genre bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/editProfile/"/>">Profil bearbeiten</a>
        </div>

    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="book_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="book_owner" value="${book_form.values["book_owner"][0]}" readonly="readonly">
                </div>

                <label for="book_title">
                    Titel:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="book_title" value="${book_form.values["book_title"][0]}">
                </div>

                <label for="book_author">Autor:
                <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="book_author" value="${book_form.values["book_author"][0]}">
                </div>

                <label for="book_genre">Genre:
                <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <select name="book_genre">
                        <option value="">Bitte auswählen...</option>

                        <c:forEach items="${genres}" var="genre">
                            <option value="${genre.id}" ${book_form.values["book_genre"][0] == genre.id.toString() ? 'selected' : ''}>
                                <c:out value="${genre.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="book_medium">Medium:
                <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <select name="book_medium">
                        <option value="">Bitte auswählen...</option>
                        <c:forEach items="${mediums}" var="medium">
                            <option value="${medium}" ${book_form.values["book_medium"][0] == medium ? 'selected' : ''}>
                                <c:out value="${medium.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="book_sumpages"> Seitenzahl [gesamt]:</label>
                    <div class="side-by-side">
                        <input name="book_sumpages" type="number" min="0" max="6000" step="1" value="${book_form.values['book_sumpages'][0]}"></label>
                    </div> 
                    
                    <label for="book_curpages"> Seitenzahl [aktuell]:</label>
                    <div class="side-by-side">
                        <input name="book_curpages" type="number" min="0" max="6000" step="1" value="${book_form.values['book_curpages'][0]}"></label>
                    </div>
                    
                    <label for="book_comment">
                        Kommentar:
                    </label>
                    <div class="side-by-side">
                        <textarea name="book_comment"><c:out value="${book_form.values['book_comment'][0]}"/></textarea>
                    </div>

                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit" name="action" value="save">
                            Sichern
                        </button>

                        <c:if test="${edit}">
                            <button class="icon-trash" type="submit" name="action" value="delete">
                                Löschen
                            </button>
                        </c:if>
                    </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty book_form.errors}">
                <ul class="errors">
                    <c:forEach items="${book_form.errors}" var="error">
                        <li>${error}</li>
                        </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>
