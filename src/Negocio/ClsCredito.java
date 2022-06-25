/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class ClsCredito {

    private Connection connection = new ClsConexion().getConection();

    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarCredito(ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_I_Credito(?,?,?,?,?,?,?,?,?,?,?,?)}");
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
            statement.execute();

            JOptionPane.showMessageDialog(null, "¡Crédito Realizado con éxito!", "Mensaje del Sistema", 1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void modificarCredito(String codigo, ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_U_Credito(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
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
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "¡Crédito Actualizado!", "Mensaje del Sistema", 1);
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
            ex.printStackTrace();
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
            throw SQLex;
        }
    }

    public ResultSet obtenerUltimoIdCredito() throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_S_UltimoIdCredito()}");
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
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
            throw SQLex;
        }
    }

    public void actualizarCreditoEstado(String codigo, ClsEntidadCredito credito) {
        try {
            CallableStatement statement = connection.prepareCall("{call 000_SP_U_ActualizarCreditoEstado(?,?)}");
            statement.setString("pidcredito", codigo);
            statement.setString("pestado", credito.getStrEstadoCredito());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "¡Credito Anulado!", "Mensaje del Sistema", 1);
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
            throw SQLex;
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
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }
}
