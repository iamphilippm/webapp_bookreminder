/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

/**
 * Spezielle EJB zum Anlegen eines Benutzers und Aktualisierung des Passworts.
 */

package dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb;

import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Stateless
public class UserBean {

    @PersistenceContext
    EntityManager em;
    
    @Resource
    EJBContext ctx;

    /**
     * Gibt das Datenbankobjekt des aktuell eingeloggten Benutzers zurück,
     *
     * @return Eingeloggter Benutzer oder null
     */
    public User getCurrentUser() {
        return this.em.find(User.class, this.ctx.getCallerPrincipal().getName());
    }
    
        public List<User> searchUser(String search) {
        return em.createQuery("SELECT u FROM User u WHERE u.vorname LIKE :search OR u.nachname LIKE :search")
                 .setParameter("search", "%"+search+"%")
                 .getResultList();
        
    }
    
    public List<User> findAllUsers(){
        return this.em.createQuery("SELECT u FROM User u").getResultList();
    }
    
    public User findByUsername(String userName){
    CriteriaBuilder cb = this.em.getCriteriaBuilder();
    CriteriaQuery<User> query = cb.createQuery(User.class);
    Root<User> from = query.from(User.class);
    query.select(from);
    query.where(cb.and(
    cb.equal(from.get("username"), userName)));
    List<User> result = em.createQuery(query).getResultList(); // getResultList() verhindert Nullpointer
    User user = result != null && result.size() == 1 ? result.get(0) : null;
        
    if(user!= null){
      user.addToGroup("app-user"); // Defaultgruppe
        }
        
        return  user;
    }

    /**
     *
     * @param username
     * @param password
     * @throws UserBean.UserAlreadyExistsException
     */
    public void signup(String username, String password, String vorname, String nachname) throws UserAlreadyExistsException {
        if (em.find(User.class, username) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", username));
        }

        User user = new User(username, password, vorname, nachname);
        user.addToGroup("app-user");
        em.persist(user);
    }
    
    

    /**
     * Passwort ändern (ohne zu speichern)
     * @param user
     * @param oldPassword
     * @param newPassword
     * @throws UserBean.InvalidCredentialsException
     */
    @RolesAllowed("app-user")
    public void changePassword(User user, String oldPassword, String newPassword) throws InvalidCredentialsException {
        if (user == null || !user.checkPassword(oldPassword)) {
            throw new InvalidCredentialsException("Benutzername oder Passwort sind falsch.");
        }

        user.setPassword(newPassword);
    }
    
    /**
     * Benutzer löschen
     * @param user Zu löschender Benutzer
     */
    @RolesAllowed("app-user")
    public void delete(User user) {
        this.em.remove(user);
    }
    
    /**
     * Benutzer aktualisieren
     * @param user Zu aktualisierender Benutzer
     * @return Gespeicherter Benutzer
     */
    @RolesAllowed("app-user")
    public User update(User user) {
        return em.merge(user);
    }

    /**
     * Fehler: Der Benutzername ist bereits vergeben
     */
    public class UserAlreadyExistsException extends Exception {

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Fehler: Das übergebene Passwort stimmt nicht mit dem des Benutzers
     * überein
     */
    public class InvalidCredentialsException extends Exception {

        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

}
