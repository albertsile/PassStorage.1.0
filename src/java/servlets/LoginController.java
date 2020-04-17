/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Person;
import entity.User;
import util.EncryptPass;
import util.JsonResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jsonbuilder.UserJsonBuilder;
import session.PersonFacade;
import session.UserFacade;

/**
 *
 * @author artjo
 */
@WebServlet(name = "LoginController", urlPatterns = {
    "/login",
    "/logout",
    "/createUser",
    
})
public class LoginController extends HttpServlet {
@EJB private PersonFacade personFacade;
@EJB private UserFacade userFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session  = request.getSession();
        String json = "";
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonResponse jsonResponse = new JsonResponse();
        JsonReader jsonReader = Json.createReader(request.getReader());
        EncryptPass ep = new EncryptPass();
        String path = request.getServletPath();
        switch (path) {
            case "/login":
                JsonObject jsonObject = jsonReader.readObject();
                String login = jsonObject.getString("login","");
                String password = jsonObject.getString("password","");
                User user = userFacade.findByLogin(login);
                if(user == null){
                  json = jsonResponse.getJsonResponse(session);
                  break;
                }
                password = ep.setEncriptPass(password,user.getSalts());
                if(!password.equals(user.getPassword())){
                  json = jsonResponse.getJsonResponse(session);
                  break;
                }
                session = request.getSession(true);
                session.setAttribute("user", user);
                UserJsonBuilder ujb = new UserJsonBuilder();
                json = jsonResponse.getJsonResponse(session,ujb.cerateJsonUser(user));
                break;
            case "/logout":
                if(session != null){
                    session.invalidate();
                    json = jsonResponse.getJsonResponse(session, "true");
                }
                break;
            case "/createUser":
                jsonObject = jsonReader.readObject();
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String email = jsonObject.getString("email");
                login = jsonObject.getString("login");
                password = jsonObject.getString("password");
                Person person = new Person(firstname, lastname, email);
                personFacade.create(person);
                String salts = ep.createSalts();
                password = ep.setEncriptPass(password, salts);
                user = new User(login, password, salts, true, person);
                userFacade.create(user);
                ujb = new UserJsonBuilder();
                json = jsonResponse.getJsonResponse(session, "true");
                break;
            
        }
        if(!"".equals(json)){
          try (PrintWriter out = response.getWriter()) {
            out.println(json);  
            out.flush();
          }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
