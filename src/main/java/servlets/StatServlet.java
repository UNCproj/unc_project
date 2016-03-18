/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.AdvertBean;
import beans.VisitBean;
import com.google.gson.Gson;
import db.DBConnect;
import db.DataSource;
import db.SQLQueriesHelper;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author craftic
 */
@WebServlet(name = "StatServlet", urlPatterns = {"/StatServlet/getList", "/StatServlet/getStat"})
public class StatServlet extends HttpServlet {

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
        ArrayList<VisitBean> visits;
        visits = new ArrayList<VisitBean>();
        ArrayList<AdvertBean> names;
        names = new ArrayList<AdvertBean>();
        String object_id;
        object_id = request.getParameter("object_id");
        switch (request.getServletPath()) {
            case "/StatServlet/getList": {
                try {
                    String comm =   "select  ref.OBJECT_ID advert,\n" +
                                    "        o.OBJECT_NAME name\n" +
                                    "            from  unc_objects obj join\n" +
                                    "                  UNC_REFERENCES ref on\n" +
                                    "                    obj.OBJECT_ID = ref.OBJECT_REFERENCE_ID\n" +
                                    "                  join unc_objects o on\n" +
                                    "                    o.OBJECT_ID = ref.OBJECT_ID\n" +
                                    "where ref.ATTR_ID = 11 and\n" +
                                    "      ref.OBJECT_REFERENCE_ID = " + object_id +"\n"  ;
                    //response.getWriter().println(comm);
                    Connection connection = null;
                    try {
                        connection = DataSource.getInstance().getConnection();
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(comm);
                    while (resultSet.next()) {
                        AdvertBean adb = new AdvertBean();
                        adb.setId(resultSet.getBigDecimal(1).toString());
                        adb.setName(resultSet.getString(2));
                        names.add(adb);
                    }
                    String jsonvis = new Gson().toJson(names);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(jsonvis);
                } catch (SQLException e) {
                    throw new ServletException(e.getMessage(), e);
                }
                break;
            }
            case "/StatServlet/getStat": {
                try {
                    BigInteger ad_id = new BigInteger(request.getParameter("ad_id"));
                    String comm
                            = "select  o.object id,\n"
                            + "        to_char(s.visit_date, 'YYYY-MM-DD') d,\n"
                            + "        count(s.id) count\n"
                            + "from  \n"
                            + "        (\n"
                            + "            select obj.OBJECT_ID object, ref.OBJECT_ID refobject\n"
                            + "            from  unc_objects obj join\n"
                            + "                  UNC_REFERENCES ref on\n"
                            + "                    obj.OBJECT_ID = ref.OBJECT_REFERENCE_ID\n"
                            + "            where ref.OBJECT_ID in\n"
                            + "                    ( select  u.OBJECT_ID\n"
                            + "                      from  unc_objects u \n"
                            + "                      where 4 = (SELECT\n"
                            + "                                CONNECT_BY_ROOT uot.OT_ID as praparent\n"
                            + "                                FROM unc_object_types uot\n"
                            + "                                where uot.ot_id = u.object_type_id\n"
                            + "                                START WITH uot.PARENT_ID is null\n"
                            + "                                CONNECT BY PRIOR ot_id = parent_id)\n"
                            + "                    ) and\n"
                            + "                    (\n"
                            + "                      obj.OBJECT_ID =" + (object_id.toString().length()>0 ? object_id.toString() : "obj.OBJECT_ID") + "\n"
                            + "                    )\n"+"and\n"
                            + "                    (\n"
                            + "                      ref.object_id = "+ ad_id +"\n"
                            +"                    )\n"
                            + "        ) o join\n"
                            + "        unc_stat s on\n"
                            + "          s.OBJ_ID = o.refobject\n"
                            + "group by  o.object,\n"
                            + "          to_char(s.visit_date, 'YYYY-MM-DD')\n"
                            + "order by  to_date(to_char(s.visit_date, 'YYYY-MM-DD'), 'YYYY-MM-DD')";
                    //response.getWriter().write(comm);
                    //DBConnect dc = new DBConnect("unc_user", "pass123");
                    Connection connection = null;
                    try {
                        connection = DataSource.getInstance().getConnection();
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(comm);
                    while (resultSet.next()) {
                        VisitBean vis = new VisitBean();
                        vis.date = resultSet.getString("d");
                        vis.count = resultSet.getInt("count");
                        visits.add(vis);
                    }
                } catch (SQLException e) {
                    throw new ServletException(e.getMessage(), e);
                }

                //response.getWriter().write("123");
                String jsonvis = new Gson().toJson(visits);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonvis);
                break;
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
