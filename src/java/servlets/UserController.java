/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Account;
import entity.Person;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jsonbuilder.AccountJsonBuilder;
import jsonbuilder.UserJsonBuilder;
import session.AccountFacade;
import session.PersonFacade;
import session.UserFacade;
import util.EncryptPass;
import util.JsonResponse;

/**
 *
 * @author artjo
 */
@WebServlet(name = "UserController", urlPatterns = {
    "/changeUserProfile",
    "/listResources",
    "/resource",
    "/createAccount",
    "/changeAccount"
    
})
public class UserController extends HttpServlet {
@EJB private UserFacade userFacade;
@EJB private PersonFacade personFacade;
@EJB private AccountFacade accountFacade;
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
        String json = "";
        JsonReader jsonReader = Json.createReader(request.getReader());
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        JsonResponse jsonResponse = new JsonResponse();
        User user = null;
        HttpSession session  = request.getSession();
        if(session != null){
            user = (User) session.getAttribute("user");
        }
        if(user == null){
            job.add("authStatus", "false")
                .add("user", "null")
                .add("dataJson", "null");
            try (Writer writer = new StringWriter()){
                Json.createWriter(writer).write(job.build());
                json = writer.toString();
            }
            try (PrintWriter out = response.getWriter()) {
                out.println(json);  
                out.flush();
                return;
            }
        }
        EncryptPass ep = new EncryptPass();
        String salts = null;
        String path = request.getServletPath();
        switch (path) {
            case "/changeUserProfile":
                JsonObject jsonObject = jsonReader.readObject();
                String login = jsonObject.getString("login");
                String password = jsonObject.getString("password","");
                String email = jsonObject.getString("email");
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                user = userFacade.find(user.getId());
                
                if(!"".equals(password)){
                    password = ep.setEncriptPass(password, user.getSalts());
                    user.setPassword(password);
                }
                user.setLogin(login);
                Person person = personFacade.find(user.getPerson().getId());
                person.setFirstname(firstname);
                person.setLastname(lastname);
                person.setEmail(email);
                personFacade.edit(person);
                user.setPerson(person);
                userFacade.edit(user);
                session.setAttribute("user", user);
                UserJsonBuilder ujb = new UserJsonBuilder();
                json = jsonResponse.getJsonResponse(session, ujb.cerateJsonUser(user));
                break;
            case "/listResources":
                List<Account>listAccounts = accountFacade.findByUser(user);
                JsonArrayBuilder jab = Json.createArrayBuilder();
                AccountJsonBuilder ajb = new AccountJsonBuilder();
                for(Account account : listAccounts){
                    jab.add(ajb.createJsonAccount(account));
                }
                json = jsonResponse.getJsonResponse(session, jab.build());
                break;
            case "/resource":
                String accountId = request.getParameter("accountId");
                Account account = accountFacade.find(Long.parseLong(accountId));
                ajb = new AccountJsonBuilder();
                json = jsonResponse.getJsonResponse(session, ajb.createJsonAccount(account));
                break;
            case "/createAccount":
                jsonObject = jsonReader.readObject();
                login = jsonObject.getString("login");  
                password = jsonObject.getString("password");  
                String resourceUrl = jsonObject.getString("resourceUrl");  
                account = new Account(resourceUrl, login, password, Calendar.getInstance().getTime(), user);
                accountFacade.create(account);
                json = jsonResponse.getJsonResponse(session,"true");
                break;
            case "/changeAccount":
                jsonObject = jsonReader.readObject();
                login = jsonObject.getString("login");  
                password = jsonObject.getString("password");  
                accountId = jsonObject.getString("accountId");  
                resourceUrl = jsonObject.getString("resourceUrl");  
                account = accountFacade.find(Long.parseLong(accountId));
                account.setLogin(login);
                account.setPassword(password);
                account.setResourceUrl(resourceUrl);
                account.setRegistrationDate(Calendar.getInstance().getTime());
                accountFacade.edit(account);
                json = jsonResponse.getJsonResponse(session,"true");
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
