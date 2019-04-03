/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.bookreminder.common.web;

import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.bookreminder.common.jpa.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author D070415
 */
@WebServlet(name = "EditProfileServlet", urlPatterns = {"/app/editProfile/"})
public class EditProfileServlet extends HttpServlet{
    
    
    
    @EJB
    ValidationBean validationBean;
            
    @EJB
    UserBean userBean;
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = this.userBean.getCurrentUser();
        
        if (session.getAttribute("profile_form") == null) {
            session.setAttribute("profile_form", this.createUserForm(user));
        }
        
        request.getRequestDispatcher("/WEB-INF/profile/profile_edit.jsp").forward(request, response);
        session.removeAttribute("profile_form");   
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> errors = new ArrayList<>();
        User user = this.userBean.getCurrentUser();
        
        // Formulareingaben auslesen        
        
        /*
        String password1 = request.getParameter("signup_password1");
        String password2 = request.getParameter("signup_password2");
        String vorname = request.getParameter("signup_vorname");
        String nachname = request.getParameter("signup_nachname");
        */
        
        String vorname = request.getParameter("vorname");
        String nachname = request.getParameter("nachname");
        String passwordOld = request.getParameter("old_password");
        String password1 = request.getParameter("new_password");
        String password2 = request.getParameter("validate_password");
        
        user.setVorname(vorname);
        user.setNachname(nachname);
        
        validationBean.validate(user, errors);
        
        if(!password1.isEmpty() && !password2.isEmpty())
        try {
            if(password1.equals(password2)){
                 userBean.changePassword(user, passwordOld, password2);
            }else{
                errors.add("Passwort und Passwort-Wiederholung müssen gleich sein");
            }
        } catch (UserBean.InvalidCredentialsException ex) {
            errors.add("Passwort konnte nicht geändert werden.");
        }
        
        if(errors.isEmpty()){
            userBean.update(user);
        }
        
        if(errors.isEmpty()){
            response.sendRedirect(WebUtils.appUrl(request, "/app/dashboard/"));
           //response.sendRedirect(request.getRequestURI()); 
        }else{
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);
            HttpSession session = request.getSession();
            session.setAttribute("profile_form", formValues);
            String url = request.getRequestURI();
            response.sendRedirect(url);
        }
        
       }

    public FormValues createUserForm(User user) {
        Map<String, String[]> values = new HashMap<String, String[]>();

        values.put("vorname", new String[]{
            user.getVorname()
        });

        values.put("nachname", new String[]{
            user.getNachname()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);

        return formValues;
    } 

}
