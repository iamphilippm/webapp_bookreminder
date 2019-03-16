<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
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
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/tasks/task/new/"/>">Aufgabe anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/tasks/categories/"/>">Kategorien bearbeiten</a>
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

        <%-- Gefundene Aufgaben --%>
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
                              <!--  <a href="<c:url value="/app/tasks/task/${book.id}"/>"> -->
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
                                <p>
                                /
                                </p>
                                <c:out value="${book.total_pages}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>