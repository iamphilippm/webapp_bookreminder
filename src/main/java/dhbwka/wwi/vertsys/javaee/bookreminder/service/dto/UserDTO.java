/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.service.dto;

import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;

/**
 *
 * @author D070495
 */
public class UserDTO {
    
    private String username;
    private String vorname;
    private String nachname;
    
    
    public UserDTO(User user){
        this.username = user.getUsername();
        this.vorname = user.getVorname();
        this.nachname = user.getNachname();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    
    
    
    
}
