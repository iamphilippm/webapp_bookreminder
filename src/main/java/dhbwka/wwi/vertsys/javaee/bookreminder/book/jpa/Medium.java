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
    BUCH, FACHLITERATUR, MAGAZIN, HOERBUCH;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case BUCH:
                return "Bücher";
            case FACHLITERATUR:
                return "Fachliteratur";
            case MAGAZIN:
                return "Magazine";
            case HOERBUCH:
                return "Hörbücher";
            default:
                return this.toString();
        }
    }

}
