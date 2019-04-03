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

import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import service.dto.UserDTO;

/**
 *
 * @author D070495
 */

@Stateless
public class UserFacade {
    
    
    @EJB
    UserBean userBean;
    
    public List<UserDTO> findAllDTO(){
        List<User> users = userBean.findAllUsers();
        return users.stream().map((user) -> {
            return new UserDTO(user);
        }).collect(Collectors.toList());
    }
    
    public List<UserDTO> searchUser(String search){
        List<User> users = userBean.searchUser(search);
        return users.stream().map((user) -> {
            return new UserDTO(user);
        }).collect(Collectors.toList());
    }
    
}
