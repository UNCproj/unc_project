/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author artem
 */
@WebServlet(name = "testServlet", urlPatterns = {"/testServlet"})
public class testServlet extends HttpServlet {

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
            throws ServletException, IOException, OAuthSystemException, OAuthProblemException {
        response.setContentType("text/html;charset=UTF-8");
        OAuthClientRequest oacr = OAuthClientRequest.tokenLocation("https://oauth.vk.com/authorize")
                .setClientId("5388232")
                .setClientSecret("UcKEKFIuI0Qh7in1sBBb")
                .setRedirectURI("http://localhost:8081/unc-project/testServlet")
                .buildQueryMessage();
        if (request.getParameter("code") == null){
            //response.getWriter().println(oacr.getLocationUri());
            response.sendRedirect(oacr.getLocationUri());
        }
        if (request.getParameter("code") != null){
            response.getWriter().println("1");
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            OAuthClientRequest rq = OAuthClientRequest.tokenLocation("https://oauth.vk.com/access_token")
                    .setClientId("5388232")
                    .setClientSecret("UcKEKFIuI0Qh7in1sBBb")
                    .setCode(oar.getCode())
                    .setRedirectURI("http://localhost:8081/unc-project/testServlet")
                    .buildQueryMessage();
            //response.sendRedirect(rq.getLocationUri());
            
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse tokenResponse = oAuthClient.accessToken(rq, OAuthJSONAccessTokenResponse.class);
            //response.getWriter().println(tokenResponse.getParam("user_id"));
            response.getWriter().println("123");
            response.getWriter().println(tokenResponse.getAccessToken() + "\n");
            response.getWriter().println(tokenResponse.getExpiresIn() + "\n");
            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://api.vk.com/method/friends.get?"
                    +   "user_id="+tokenResponse.getParam("user_id")
                    +   "&fields=city,domain"
                    )
                    .setAccessToken(tokenResponse.getAccessToken())
                    .buildQueryMessage();
            //response.sendRedirect(bearerClientRequest.getLocationUri());
            OAuthResourceResponse fr = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            //response.getWriter().println(fr.getBody());
            /*
            try{
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(fr.getBody());
            Iterator<JsonNode> it = jn.get("response").getElements();
            while (it.hasNext()){
                //response.getWriter().println(it.next().get("id"));
            }
            }
            catch (Exception e){
                response.getWriter().println(e.getMessage());
            }
            */
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
        try {
            processRequest(request, response);
        } catch (OAuthSystemException ex) {
            Logger.getLogger(testServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
            Logger.getLogger(testServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (OAuthSystemException ex) {
            Logger.getLogger(testServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
            Logger.getLogger(testServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
