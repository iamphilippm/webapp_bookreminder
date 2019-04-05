/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

/*
Das ist die eigentliche Datenbank-Klasse (Entity) "Medium".
Hier werden relevante Felder definiert, mindestens ein Konstruktor geschrieben,
sowie die Setter und Getter für die einzelnen Felder implementiert.
Die Felder repräsentieren die spätere Datenbank für die Entity "Medium".
*/

package dhbwka.wwi.vertsys.javaee.bookreminder.book.jpa;

/**
 * Medium eines Buches.
 */
public enum Medium {
    Buch, Zeitung, Magazin, eBook;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case Buch:
                return "Bücher";
            case Zeitung:
                return "Zeitungen";
            case Magazin:
                return "Magazine";
            case eBook:
                return "eBooks";
            default:
                return this.toString();
        }
    }

}
