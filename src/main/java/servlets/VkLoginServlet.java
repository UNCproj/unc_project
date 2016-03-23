/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.BeansHelper;
import java.io.IOException;
import java.io.PrintWriter;
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
import unc.helpers.VkOauthLogin;

/**
 *
 * @author artem
 */
@WebServlet(name = "VkLoginServlet", urlPatterns = {"/vklogin"})
public class VkLoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ObjectMapper om = new ObjectMapper();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException, OAuthProblemException {
        response.setContentType("text/html;charset=UTF-8");
        VkOauthLogin vk = new VkOauthLogin(request.getServletPath());
        OAuthClientRequest oacr = OAuthClientRequest.tokenLocation("https://oauth.vk.com/authorize")
                .setClientId(vk.getClientID())
                .setClientSecret(vk.getClientSecret())
                .setRedirectURI("http://localhost:8081/unc-project/vklogin")
                .buildQueryMessage();
        if ((request.getParameter("code") == null) && (request.getSession().getAttribute(BeansHelper.VK_CODE) == null)) {
            //response.getWriter().println(oacr.getLocationUri());
            response.sendRedirect(oacr.getLocationUri());
        } else if ((request.getParameter("code") != null) && (request.getSession().getAttribute(BeansHelper.VK_CODE) == null)) {
            request.getSession().setAttribute(BeansHelper.VK_CODE, request.getParameter("code"));
            
            response.sendRedirect("http://localhost:8081/unc-project/vklogin");
        } else if (request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY) == null) {
            try{
            OAuthJSONAccessTokenResponse token = getAuthKeyInfo(response, request, vk);
            request.getSession().setAttribute(BeansHelper.VK_AUTHORIZATION_KEY, token.getAccessToken());
            request.getSession().setAttribute(BeansHelper.VK_TOKEN, token);
            response.getWriter().println((String)request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY));
            }
            catch(Exception e){
                response.getWriter().println(e.toString());
            }
        } else {
            response.getWriter().println("запросы");
            response.getWriter().println((String)request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY));
            response.getWriter().println(((OAuthJSONAccessTokenResponse)(request.getSession().getAttribute(BeansHelper.VK_TOKEN))).getExpiresIn());
        }
    }

    OAuthJSONAccessTokenResponse getAuthKeyInfo(HttpServletResponse response, HttpServletRequest request, VkOauthLogin vk) throws OAuthSystemException, OAuthProblemException, IOException {
        OAuthJSONAccessTokenResponse tokenResponse = null;
        OAuthClientRequest rq = OAuthClientRequest.tokenLocation("https://oauth.vk.com/access_token")
                .setClientId(vk.getClientID())
                .setClientSecret(vk.getClientSecret())
                .setCode((String) request.getSession().getAttribute(BeansHelper.VK_CODE))
                .setRedirectURI("http://localhost:8081/unc-project/vklogin")
                .buildQueryMessage();
        response.getWriter().println(rq.getLocationUri());
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        tokenResponse = oAuthClient.accessToken(rq, OAuthJSONAccessTokenResponse.class);
        return tokenResponse;
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
            Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
            Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
            Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
