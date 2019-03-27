/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa;

/**
 * Medium eines Buches.
 */
public enum Medium {
    BUCH, ZEITUNG, MAGAZIN, EBOOK;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case BUCH:
                return "Bücher";
            case ZEITUNG:
                return "Zeitungen";
            case MAGAZIN:
                return "Magazine";
            case EBOOK:
                return "eBooks";
            default:
                return this.toString();
        }
    }

}
