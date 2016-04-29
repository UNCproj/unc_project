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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author artem
 */
@WebServlet(name = "ModerServlet", urlPatterns = {"/ModerServlet/delAdvert", "/ModerServlet/deleted"})
public class ModerServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ModerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ModerServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        Logger log = Logger.getLogger("moderServletLogger");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        //processRequest(request, response);
        log.info("moder servlet start");
        log.info(request.getServletPath());
        log.info("MSG=" + request.getParameter("msg") + "_" + request.getParameter("uid"));
        Connection connection = null;
        Statement st = null;
        
        if (request.getServletPath().equals("/ModerServlet/delAdvert")) {
            String delMsg = request.getParameter("msg");
        String delId = request.getParameter("uid");
        UserAccountBean user = (UserAccountBean) request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        
        if ((user == null) || (!user.isIsModer())) {
            return;
        }
            try {
                connection = DataSource.getInstance().getConnection();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            String id = request.getParameter("id");
            String attr_id = SQLQueriesHelper.INVALID_ATTR_ID;

            try {
                st = connection.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //String comm = SQLQueriesHelper.updateParam(new BigDecimal(id), attr_id, "true", null);
                String comm = SQLQueriesHelper.getTypeIdByObjectId(id);
                log.info("checktype1 " + comm);
                ResultSet res = st.executeQuery(comm);
                res.next();
                String typeId = res.getString(1);
                comm = SQLQueriesHelper.getParentTypeIdByObjectTypeId(typeId);
                log.info("checktype2 " + comm);
                res = st.executeQuery(comm);
                res.next();
                String type = res.getString(1);
                if (!type.equals(SQLQueriesHelper.ADVERT_TYPE_ID)&&!type.equals(SQLQueriesHelper.USER_TYPE_ID)) {
                    log.info("Некорректный тип объекта!");
                    log.info(type);
                    return;
                }
                comm = "select value from unc_params where "
                        + "object_id=" + id + " "
                        + "and attr_id=" + SQLQueriesHelper.INVALID_ATTR_ID;
                log.info("getvalue " + comm);
                res = st.executeQuery(comm);
                if (!res.next()) {
                    comm = "insert into unc_params (object_id, attr_id, value)"
                            + "values(" + id + "," + SQLQueriesHelper.INVALID_ATTR_ID + "," + "'true'" + ")";
                    log.info("insert= " + comm);
                    int r = st.executeUpdate(comm);
                    log.info("res= " + r);
                } else {
                    comm = SQLQueriesHelper.updateParam(new BigDecimal(id), attr_id, "true", null);
                    log.info("update= " + comm);
                    int r = st.executeUpdate(comm);
                    log.info("res= " + r);
                }

                comm = "select value from unc_params where "
                        + "object_id=" + id + " "
                        + "and attr_id=" + SQLQueriesHelper.DEL_ID_ATTR_ID;
                log.info("getvalue " + comm);
                res = st.executeQuery(comm);
                if (!res.next()) {
                    comm = "insert into unc_params (object_id, attr_id, value)"
                            + "values(" + id + "," + SQLQueriesHelper.DEL_ID_ATTR_ID + "," + "'" + delId + "'" + ")";
                    log.info("insert= " + comm);
                    int r = st.executeUpdate(comm);
                    log.info("res= " + r);
                    comm = "insert into unc_params (object_id, attr_id, value)"
                            + "values(" + id + "," + SQLQueriesHelper.DEL_MSG_ATTR_ID + "," + "'" + delMsg + "'" + ")";
                    log.info("insert= " + comm);
                    r = st.executeUpdate(comm);
                    log.info("res= " + r);
                } else {
                    comm = SQLQueriesHelper.updateParam(new BigDecimal(id), SQLQueriesHelper.DEL_ID_ATTR_ID, delId, null);
                    log.info("update= " + comm);
                    int r = st.executeUpdate(comm);
                    log.info("res= " + r);
                    comm = SQLQueriesHelper.updateParam(new BigDecimal(id), SQLQueriesHelper.DEL_MSG_ATTR_ID, delMsg, null);
                    log.info("update= " + comm);
                    r = st.executeUpdate(comm);
                    log.info("res= " + r);
                }

            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getServletPath().equals("/ModerServlet/deleted")) {
            log.info("deleted_start");
            String del_id = request.getParameter("del_id");
            String del_msg = request.getParameter("del_msg");
            try {
                connection = DataSource.getInstance().getConnection();
                st = connection.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rs = null;
            String fname = null, sname = null;
            String getfname = SQLQueriesHelper.getParamValue(del_id, SQLQueriesHelper.FIRST_NAME_ATTR_ID);
            try {
                log.info("getfname=" + getfname);
                rs = st.executeQuery(getfname);
                rs.next();
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fname = rs.getString(1);
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            String getsname = SQLQueriesHelper.getParamValue(del_id, SQLQueriesHelper.SURNAME_ATTR_ID);
            try {
                log.info("getsname=" + getsname);
                rs = st.executeQuery(getsname);
                rs.next();
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sname = rs.getString(1);
            } catch (SQLException ex) {
                Logger.getLogger(ModerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            log.info("NAME=" + fname + "_" + sname);
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Объявление удалено</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Объявление удалено модератором " + "<a href=../unc_object.jsp?id="+del_id+">" + fname + " " + sname +"</a> \n" + "</h1> \n");
                out.println("<h1>Причина удаления:</h1>");
                out.println("<h2>"+del_msg+"</h2>");
                out.println("</body>");
                out.println("</html>");
            }
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
