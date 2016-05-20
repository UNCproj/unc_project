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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import unc.helpers.Crypt2;

/**
 *
 * @author Andrey
 */
public class Parser {
    private ParserParams params = new ParserParams();
    
    public ParserParams getParams() {
        return params;
    }
    
    public void migrationUser(InputStream in) {
        try {
            ArrayList<ArrayList<Object>> list = readFromExcel(in);
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
    
    public void migrationAdvertAuto(InputStream in) {
        try {
            ArrayList<ArrayList<Object>> list = readFromExcel(in);
            if (list != null)
                InsertAdvert(list);
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
    
    private ArrayList<ArrayList<Object>> readFromExcel(InputStream in) {
        try (HSSFWorkbook wb = new HSSFWorkbook(in)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            ArrayList<ArrayList<Object>> list = new ArrayList<>();
            int i = 0;
            System.out.println("Fail read success");
            while (it.hasNext()) {
                list.add(new ArrayList<>());
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING: {
                            list.get(i).add(cell.getStringCellValue());
                            break;
                        }
                        case Cell.CELL_TYPE_NUMERIC: {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    list.get(i).add(cell.getDateCellValue());
                            }
                            else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                list.get(i).add(cell.getStringCellValue());
                            }
                            break;
                        }
                    }
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
                
    private void InsertUser(ArrayList<ArrayList<Object>> list) throws IOException, SQLException, PropertyVetoException, ParseException{
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            for (int i = 1; i < list.size(); i++){
                if (checkLogin((String)list.get(i).get(0)) == null) {//проверить если этот логин
                    ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
                    results.next();
                    BigDecimal id = results.getBigDecimal("id");
                    statement.executeUpdate(SQLQueriesHelper.insertUser(id, (String)list.get(i).get(0)));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.LOGIN_ATTR_ID, (String)list.get(i).get(0), null));
                    statement.executeUpdate(SQLQueriesHelper.insertPass(id.toString()));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.EMAIL_ATTR_ID, (String)list.get(i).get(1) , null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.REG_DATE_ATTR_ID, null, (Date)list.get(i).get(2)));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PHONE_ATTR_ID, (String)list.get(i).get(3), null));
                    params.countRowUser++;
                }
                else{
                    params.listErrUser.add((String)list.get(i).get(0));
                }
            }
            connection.commit();
        }
    }
    
    private void InsertAdvert(ArrayList<ArrayList<Object>> list) throws IOException, SQLException, PropertyVetoException, ParseException{
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            for (int i = 1; i < list.size(); i++){
                BigDecimal userid = checkLogin((String)list.get(i).get(4));
                if (userid != null) {//проверить если этот логин
                    ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
                    results.next();
                    BigDecimal id = results.getBigDecimal("id");
                    if ((((String)list.get(i).get(2)).toLowerCase()).equals("null"))
                        results = statement.executeQuery(SQLQueriesHelper.getTypePropertyAdvertByTypeNameWithOutSubCategory((String)list.get(i).get(1), (String)list.get(i).get(3)));
                    else
                        results = statement.executeQuery(SQLQueriesHelper.getTypePropertyAdvertByTypeNameWithSubCategory((String)list.get(i).get(1), (String)list.get(i).get(2), (String)list.get(i).get(3)));
                    results.next();
                    String typeid = results.getString("OT_ID");
                    statement.executeUpdate(SQLQueriesHelper.insertPropertyAdvert(id, typeid, (String)list.get(i).get(0)));
                    
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.CITY_ADVERT_ATTR_ID, (String)list.get(i).get(5), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.DESCRIPTION_ATTR_ID, (String)list.get(i).get(7), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.PRICE_ADVERT_ATTR_ID , (String)list.get(i).get(8), null));
                    statement.executeUpdate(SQLQueriesHelper.insertParam(id, SQLQueriesHelper.REG_DATE_ATTR_ID , null, (Date)list.get(i).get(6)));
                    
                    statement.executeUpdate(SQLQueriesHelper.newReference(id.toString(), userid.toString(), "11"));
                    
                    params.countRowAdvert++;
                }
                else {
                    params.listErrAdvert.add((String)list.get(i).get(0));
                }
            }
            connection.commit();
        }
    }
    
    private BigDecimal checkLogin(String login) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ResultSet results = statement.executeQuery(SQLQueriesHelper.checkLogin(login));
            if (results.next())
                return results.getBigDecimal("OBJECT_ID");
            else
                return null;
        }
    }
    
    private boolean checkEmail(String email) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ResultSet results = statement.executeQuery(SQLQueriesHelper.checkEmail(email));
            if (results.next())
                return true;
            else
                return false;
        }
    }
}
