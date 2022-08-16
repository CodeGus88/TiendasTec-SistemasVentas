/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;
import java.sql.Connection;
import com.mysql.jdbc.jdbc2.optional.*;
import java.sql.SQLException;
import java.util.logging.Level;
import statics.Message;

public class ClsConexion {
    
    private static Connection conection=null;
    public Connection getConection(){
        try{
            MysqlConnectionPoolDataSource ds=new MysqlConnectionPoolDataSource();
            ds.setServerName("localhost");
            ds.setPort(3306);
            ds.setDatabaseName("db_store_desktop_app");
            conection=ds.getConnection("root","");
        }catch(SQLException e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return conection;
    }
    
}
