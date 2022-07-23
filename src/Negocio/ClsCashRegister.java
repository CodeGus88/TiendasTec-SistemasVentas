
package Negocio;

import Conexion.ClsConexion;
import Entidad.dtos.CashRegisterItem;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import statics.Message;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ClsCashRegister {
    
    private Connection connection = new ClsConexion().getConection();
    
    public ArrayList<CashRegisterItem> listCashRegisterItem(String tableName, Date date){
        ArrayList<CashRegisterItem> list = new ArrayList<>();
        try {
            CallableStatement statement = connection.prepareCall("{call 099_SP_S_CreditoVentaCajaPorFecha(?,?)}");
            statement.setString("pnombretabla", tableName);
            statement.setDate("pfecha", new java.sql.Date(date.getTime()));
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                CashRegisterItem cash = new CashRegisterItem();
                cash.setAmount(rs.getFloat("cantidad"));
                cash.setName(rs.getString("producto"));
                cash.setPrice(rs.getFloat("precio"));
                cash.setPrice(rs.getFloat("precio"));
                cash.setTotal(rs.getFloat("total"));
                cash.setUtility(rs.getFloat("ganancia"));
                cash.setDate(rs.getDate("fechacobro"));
                list.add(cash);
            }
            return list;
        } catch (Exception e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}
