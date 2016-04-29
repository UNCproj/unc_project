/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Andrey
 */
public class Parser {
    private ArrayList<String> listErr;
    
    public ArrayList<String> getListErr() {
        return listErr;
    }
    
    public void migrationUser(String file) {
        try {
            ArrayList<ArrayList<String>> list = readFromExcel(file);
            if (list != null)
                InsertUser(list);
            else {
                
            }
        } catch (PropertyVetoException | SQLException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {  
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    private ArrayList<ArrayList<String>> readFromExcel(String file) {
        try (InputStream in = new FileInputStream(file);
             HSSFWorkbook wb = new HSSFWorkbook(in)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            ArrayList<ArrayList<String>> list = new ArrayList<>();
            int i = 0;
            while (it.hasNext()) {
                list.add(new ArrayList<>());
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    list.get(i).add(cell.getStringCellValue());
                }
                i++;
            }
            return list;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    private void InsertUser(ArrayList<ArrayList<String>> list) throws IOException, SQLException, PropertyVetoException, ParseException{
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            //String[] mass = typeParamsbyName(list.get(0));
            for (int i = 1; i < list.size(); i++){
                if (checkLogin(list.get(i).get(0))) {//проверить если этот логин
                    ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
                    results.next();
                    String id = results.getString("id");
                    statement.executeUpdate(SQLQueriesHelper.insertUser(id, list.get(i).get(0)));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.LOGIN_ATTR_ID, list.get(i).get(0), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PASSWORD_ATTR_ID, String.valueOf(list.get(i).get(1).hashCode()), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.REG_DATE_ATTR_ID, null, list.get(i).get(2)));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PHONE_ATTR_ID, list.get(i).get(3), null));
                }
                else{
                    listErr.add(list.get(i).get(0));
                }
            }
            connection.commit();
        }
    }
    
    private void InsertAdvertProperty(ArrayList<ArrayList<String>> list) throws IOException, SQLException, PropertyVetoException, ParseException{
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            //String[] mass = typeParamsbyName(list.get(0));
            for (int i = 1; i < list.size(); i++){
                if (!checkLogin(list.get(i).get(1))) {//проверить если этот логин
                    ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
                    results.next();
                    String id = results.getString("id");
                    statement.executeUpdate(SQLQueriesHelper.insertAdvert(id, list.get(i).get(0), typeId));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.LOGIN_ATTR_ID, list.get(i).get(0), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PASSWORD_ATTR_ID, String.valueOf(list.get(i).get(1).hashCode()), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.REG_DATE_ATTR_ID, null, list.get(i).get(2)));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PHONE_ATTR_ID, list.get(i).get(3), null));
                }
                else {
                    listErr.add(list.get(i).get(0));
                }
            }
            connection.commit();
        }
    }
    
    private String[] typeParamsbyName(ArrayList<String> names) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            String[] mass = new String[names.size()];
            for (int i = 0; i < names.size(); i++){ 
                ResultSet results = statement.executeQuery(SQLQueriesHelper.getTypeIdByName(names.get(i)));
                while (results.next()) {
                    mass[i] = results.getString("ATTR_ID");
                }
            }
            return mass;
        }
    }
    
        private String typeObjectbyName(String category, String action) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            
            
            return "";
        }
    }
    
    private boolean checkLogin(String login) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ResultSet results = statement.executeQuery(SQLQueriesHelper.checkLogin(login));
            return results.next();
        }
    }
}
