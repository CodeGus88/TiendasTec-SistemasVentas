/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import Entidad.dtos.accountsreceivable.AccountsReceivableItem;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import statics.Message;

public class ClsCredito {

    private Connection connection = new ClsConexion().getConection();

    public boolean agregarCredito(ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_I_Credito(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidtipodocumento", credito.getStrIdTipoDocumento());
            statement.setString("pidcliente", credito.getStrIdCliente());
            statement.setString("pidempleado", credito.getStrIdEmpleado());
            statement.setString("pserie", credito.getStrSerieCredito());
            statement.setString("pnumero", credito.getStrNumeroCredito());
            statement.setDate("pfecha", new java.sql.Date(credito.getStrFechaCredito().getTime()));
            statement.setString("ptotalcredito", credito.getStrTotalCredito());
            statement.setString("pdescuento", credito.getStrDescuentoCredito());
            statement.setString("psubtotal", credito.getStrSubTotalCredito());
            statement.setString("pigv", credito.getStrIgvCredito());
            statement.setString("ptotalpagar", credito.getStrTotalPagarCredito());
            statement.setString("pestado", credito.getStrEstadoCredito());
            statement.setBoolean("pporcobrar", credito.isPorCobrar());
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public boolean modificarCredito(String codigo, ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_U_Credito(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidcredito", codigo);
            statement.setString("pidtipodocumento", credito.getStrIdTipoDocumento());
            statement.setString("pidcliente", credito.getStrIdCliente());
            statement.setString("pidempleado", credito.getStrIdEmpleado());
            statement.setString("pserie", credito.getStrSerieCredito());
            statement.setString("pnumero", credito.getStrNumeroCredito());
            statement.setDate("pfecha", new java.sql.Date(credito.getStrFechaCredito().getTime()));
            statement.setString("ptotalcredito", credito.getStrTotalCredito());
            statement.setString("pdescuento", credito.getStrDescuentoCredito());
            statement.setString("psubtotal", credito.getStrSubTotalCredito());
            statement.setString("pigv", credito.getStrIgvCredito());
            statement.setString("ptotalpagar", credito.getStrTotalPagarCredito());
            statement.setString("pestado", credito.getStrEstadoCredito());
            statement.setBoolean("pporcobrar", credito.isPorCobrar());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }
    
    public boolean payCredit(int creditId, boolean payable, java.sql.Date date) {
        try {
            CallableStatement statement = connection.prepareCall("{call 001_SP_U_PagarCredito(?,?,?)}");
            statement.setInt("pidcredito", creditId);
            statement.setDate("pfechacobro", date);
            statement.setBoolean("pporcobrar", payable);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }
    
    public ClsEntidadCredito findById(int creditId){
        try {
            CallableStatement statement = connection.prepareCall("{call 000_CreditoPorId(?)}");
            statement.setInt("pidcredito", creditId);
            ResultSet resultSet = statement.executeQuery();
            ClsEntidadCredito credito = null;
            while (resultSet.next()) {
                credito = new ClsEntidadCredito();
                credito.setStrIdCredito(resultSet.getString("idcredito"));
                credito.setStrIdTipoDocumento(resultSet.getString("idtipodocumento"));
                credito.setStrIdCliente(resultSet.getString("idcliente"));
                credito.setStrIdEmpleado(resultSet.getString("idempleado"));
                credito.setStrSerieCredito(resultSet.getString("serie"));
                credito.setStrNumeroCredito(resultSet.getString("numero"));
                credito.setStrFechaCredito(resultSet.getDate("fecha"));
                credito.setStrTotalCredito(resultSet.getString("totalcredito"));
                credito.setStrDescuentoCredito(resultSet.getString("descuento"));
                credito.setStrSubTotalCredito(resultSet.getString("subtotal"));
                credito.setStrIgvCredito(resultSet.getString("igv"));
                credito.setStrTotalPagarCredito(resultSet.getString("totalpagar"));
                credito.setStrEstadoCredito(resultSet.getString("estado"));
                credito.setStrFechaCobro(resultSet.getDate("fechacobro"));
                credito.setPorCobrar(resultSet.getBoolean("porcobrar"));
            }
            return credito;
        } catch (SQLException e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public ArrayList<ClsEntidadCredito> listarCredito() {
        ArrayList<ClsEntidadCredito> creditos = new ArrayList<ClsEntidadCredito>();
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_Credito}");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClsEntidadCredito credito = new ClsEntidadCredito();
                credito.setStrIdCredito(resultSet.getString("IdCredito"));
                credito.setStrTipoDocumento(resultSet.getString("TipoDocumento"));
                credito.setStrCliente(resultSet.getString("Cliente"));
                credito.setStrEmpleado(resultSet.getString("Empleado"));
                credito.setStrSerieCredito(resultSet.getString("Serie"));
                credito.setStrNumeroCredito(resultSet.getString("Numero"));
                credito.setStrFechaCredito(resultSet.getDate("Fecha"));
                credito.setStrTotalCredito(resultSet.getString("TotalCredito"));
                credito.setStrDescuentoCredito(resultSet.getString("Descuento"));
                credito.setStrSubTotalCredito(resultSet.getString("SubTotal"));
                credito.setStrIgvCredito(resultSet.getString("Igv"));
                credito.setStrTotalPagarCredito(resultSet.getString("TotalPagar"));
                credito.setStrEstadoCredito(resultSet.getString("Estado"));
                creditos.add(credito);
            }
            return creditos;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }
    
    /**
     * Lista todos los dréditos
     * @return listaCreditos
     */
    public ArrayList<AccountsReceivableItem> listarCreditoPagable(boolean porCobrar) {
        ArrayList<AccountsReceivableItem> items = new ArrayList<AccountsReceivableItem>();
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_CreditoPagable(?)}");
            statement.setBoolean("pporcobrar", porCobrar);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AccountsReceivableItem receivableItem = new AccountsReceivableItem();
                receivableItem.setId(resultSet.getInt("idcredito"));
                receivableItem.setClientName(resultSet.getString("nombrecliente"));
                receivableItem.setClientDni(resultSet.getString("dnicliente"));
                receivableItem.setClientRuc(resultSet.getString("ruccliente"));
                receivableItem.setEmployeeName(resultSet.getString("nombreempleado"));
                receivableItem.setIgv(resultSet.getFloat("igv"));
                receivableItem.setSubtotal(resultSet.getFloat("subtotal"));
                receivableItem.setTotalCredit(resultSet.getFloat("totalcredito"));
                receivableItem.setDiscount(resultSet.getFloat("descuento"));
                receivableItem.setTotal(resultSet.getFloat("totalpagar"));
                receivableItem.setDate(resultSet.getDate("fecha"));
                items.add(receivableItem);
            }
            return items;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }


    public ResultSet listarCreditoPorParametro(String criterio, String busqueda) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_CreditoPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            Message.LOGGER.log(Level.SEVERE, SQLex.getMessage());
            return null;
        }
    }

    public ResultSet obtenerUltimoIdCredito() throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_UltimoIdCredito()}");
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            Message.LOGGER.log(Level.SEVERE, SQLex.getMessage());
            return null;
        }
    }

    public ResultSet listarCreditoPorFecha(String criterio, Date fechaini, Date fechafin, String doc) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_CreditoPorFecha(?,?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setDate("pfechaini", new java.sql.Date(fechaini.getTime()));
            statement.setDate("pfechafin", new java.sql.Date(fechafin.getTime()));
            statement.setString("pdocumento", doc);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            Message.LOGGER.log(Level.SEVERE, SQLex.getMessage());
            return null;
        }
    }

    public boolean actualizarCreditoEstado(String codigo, ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_U_ActualizarCreditoEstado(?,?)}");
            statement.setString("pidcredito", codigo);
            statement.setString("pestado", credito.getStrEstadoCredito());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public ResultSet listarCreditoPorDetalle(String criterio, Date fechaini, Date fechafin) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_CreditoPorDetalle(?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setDate("pfechaini", new java.sql.Date(fechaini.getTime()));
            statement.setDate("pfechafin", new java.sql.Date(fechafin.getTime()));
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            return null;
        }
    }

    public ResultSet listarCreditoMensual(String criterio, String fecha_ini, String fecha_fin) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_CreditoMensual(?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pfecha_ini", fecha_ini);
            statement.setString("pfecha_fin", fecha_fin);
            rs = statement.executeQuery();
        } catch (SQLException SQLex) {
            Message.LOGGER.log(Level.SEVERE, SQLex.getMessage());
        }
        return rs;
    }
}
