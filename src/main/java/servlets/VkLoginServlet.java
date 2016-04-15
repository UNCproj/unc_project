/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import unc.helpers.Param;
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
            throws ServletException, IOException, OAuthSystemException, OAuthProblemException, SQLException, PropertyVetoException {
        response.setContentType("text/html;charset=UTF-8");
        VkOauthLogin vk = new VkOauthLogin(request.getServletPath());
        OAuthClientRequest oacr = OAuthClientRequest.tokenLocation("https://oauth.vk.com/authorize")
                .setClientId(vk.getClientID())
                .setClientSecret(vk.getClientSecret())
                .setRedirectURI("http://localhost:8081/unc-project/vklogin")
                .buildQueryMessage();
        if ((request.getParameter("code") == null) && (request.getSession().getAttribute(BeansHelper.VK_CODE) == null)) {
            //response.getWriter().println(oacr.getLocationUri());
            response.sendRedirect(oacr.getLocationUri()+"&scope=email");
        } else if ((request.getParameter("code") != null) && (request.getSession().getAttribute(BeansHelper.VK_CODE) == null)) {
            request.getSession().setAttribute(BeansHelper.VK_CODE, request.getParameter("code"));
            response.sendRedirect("http://localhost:8081/unc-project/vklogin");
        } else if (request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY) == null) {
            try{
            OAuthJSONAccessTokenResponse token = getAuthKeyInfo(response, request, vk);
            request.getSession().setAttribute(BeansHelper.VK_AUTHORIZATION_KEY, token.getAccessToken());
            request.getSession().setAttribute(BeansHelper.VK_TOKEN, token);
            response.getWriter().println((String)request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY));
            response.sendRedirect("http://localhost:8081/unc-project/vklogin");
            }
            catch(Exception e){
                response.getWriter().println(e.toString());
            }
        } else {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse authToken = (OAuthJSONAccessTokenResponse)(request.getSession().getAttribute(BeansHelper.VK_TOKEN));
            response.getWriter().println("запросы");
            response.getWriter().println((String)request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY));
            response.getWriter().println(((OAuthJSONAccessTokenResponse)(request.getSession().getAttribute(BeansHelper.VK_TOKEN))).getExpiresIn());
            
            String user_id = ((OAuthJSONAccessTokenResponse)(request.getSession().getAttribute(BeansHelper.VK_TOKEN))).getParam("user_id");
            String auth = (String)request.getSession().getAttribute(BeansHelper.VK_AUTHORIZATION_KEY);
            
            ArrayList<Param> list = new ArrayList<>();
            list.add(new Param("owner_id", user_id));
            list.add(new Param("album_id", "profile"));
            JsonNode ava = doQuery(response, "photos.get", auth, list);
            ava = ava.get(ava.size()-1);
            String avatar = ava.get("src").toString();
            
            response.getWriter().println(avatar);
            response.getWriter().println(authToken.getBody());
            Connection con = DataSource.getInstance().getConnection();
            Statement statement = con.createStatement();
            String[] types = new String[2];
            types[0] = "1";
            types[1] = "2";
            ResultSet result = statement.executeQuery(SQLQueriesHelper.selectFullObjectInformationByName(types, user_id));
            
            list = new ArrayList<>();
            list.add(new Param("user_ids", user_id));
            JsonNode user = doQuery(response, "users.get", auth, list);
            response.getWriter().println("user="+user.get(0));
            String fname = user.get(0).get("first_name").toString().replace('"', ' ');
            String sname = user.get(0).get("last_name").toString().replace('"', ' ');
            
            if (result.next()) {
                response.getWriter().println(login(request, user_id, authToken.getParam("email"), avatar.replace('"', ' ')));
                response.getWriter().println();
                UserAccountBean ub = (UserAccountBean) request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
                response.getWriter().println(ub.getId()+" "+ub.getLogin()+" "+ub.getUserPicFile());
                response.sendRedirect("/unc-project/unc_object.jsp?id="+ub.getId());
            }
            else{
                response.sendRedirect("/unc-project/registration"
                        +"?email="+authToken.getParam("email")
                        +"&login="+user_id
                        +"&pass="+authToken.getAccessToken()
                        +"&retypePass="+authToken.getAccessToken()
                        +"&ava="+avatar.replace('"', ' ')
                        +"&fname="+fname
                        +"&sname="+sname
                        );
            }
            
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
    
    
    public JsonNode doQuery(HttpServletResponse response, String f, String auth, List<Param> prms) throws OAuthSystemException, OAuthProblemException, IOException{
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        String path = "https://api.vk.com/method/"+ f + "?";
        for (Param p : prms){
            path += p.getName();
            path += "=";
            path += p.getValue();
            path += "&";
        }
        response.getWriter().println(path);
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(path)
                .setAccessToken(auth)
                .buildQueryMessage();
        OAuthResourceResponse fr = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(fr.getBody());
            jn = jn.get("response");
        return jn;
    }
    
    boolean login(HttpServletRequest request, String login, String email, String userPicFile ) throws ServletException{
            BigDecimal userId = null;
            Connection connection = null;
            Statement selectObjInfoStatement = null;
            Statement updateLastLoginDateStatement = null;

            try {
                connection = DataSource.getInstance().getConnection();
                selectObjInfoStatement = connection.createStatement();
                String[] types = new String[1];
                types[0] = SQLQueriesHelper.USER_TYPE_ID;
                ResultSet results = selectObjInfoStatement.executeQuery(
                        SQLQueriesHelper.selectFullObjectInformationByName(types, login));
                while(results.next()) {
                    String attrName = results.getString("attr_name");
                    userId = results.getBigDecimal("object_id");
                }
                updateLastLoginDateStatement = connection.createStatement();
                updateLastLoginDateStatement.executeUpdate(
                        SQLQueriesHelper.updateParam(userId, SQLQueriesHelper.LAST_VISIT_DATE_ATTR_ID, null, new Date()));
            }
            catch (Exception e) {
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                throw new ServletException(e);
            }
            finally {
                try {
                    if (connection != null)
                        connection.close();

                    if (selectObjInfoStatement != null)
                        selectObjInfoStatement.close();

                    if (updateLastLoginDateStatement != null)
                        updateLastLoginDateStatement.close();
                }
                catch (SQLException e) {
                    throw new ServletException(e);
                }
            }

            UserAccountBean userAccountBean = new UserAccountBean();
            userAccountBean.initialize(userId.toString(), login,
                    "",
                    email, userPicFile, true, new Date());
            request.getSession().setAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY, userAccountBean);
            return true;
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
            try {
                processRequest(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            try {
                processRequest(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(VkLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
