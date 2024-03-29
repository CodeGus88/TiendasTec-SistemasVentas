/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Entidad.*;
import Negocio.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.Message;
import statics.ScreenUses;
import statics.TableConfigurator;

public class FrmCompra extends javax.swing.JInternalFrame {

//    String Total;
//    String strCodigo;
    String accion;
    String numCompra;
    int registros;
    String id[] = new String[50];

    static int intContador;
    public String IdEmpleado, NombreEmpleado;
    int idventa;
    public String codigo;
    static Connection conn = null;

    static ResultSet rs = null;
   private DefaultTableModel defaultTableModel;

    private String[] titles = {"ID", "CÓDIGO", "PRODUCTO", "DESCRIPCIÓN", "CANTIDAD", "PRECIO", "TOTAL"};
    private float[] widths = {4.95F, 10F, 25F, 30F, 10F, 10F, 10F};

    public FrmCompra() {
        initComponents();
        //---------------------FECHA ACTUAL-------------------------------
        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);

        txtFecha.setDate(date);
        //---------------------GENERA NUMERO DE VENTA---------------------
        numCompra = generaNumCompra();
        txtNumero.setText(numCompra);
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(1004, 612);
        cargarComboTipoDocumento();

        lblIdProducto.setVisible(false);
        lblIdProveedor.setVisible(false);
        txtDescripcionProducto.setVisible(false);
        mirar();
        defaultTableModel = new DefaultTableModel(null, titles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalleProducto.setModel(defaultTableModel);
        CrearTablaDetalleProducto();
        panelConfigurator();
        tableConfigurator();
        design();
    }
    
    private void panelConfigurator() {
        Container container = getContentPane();
        container.removeAll();
        container.setLayout(new GridLayout());
        JPanel panelBackground = new JPanel(new BorderLayout(5, 5));
        JPanel panelContent = new JPanel(new BorderLayout(5, 5));
        panelBackground.setOpaque(false);
        panelContent.setOpaque(false);
//        panelContent.add(panelHead, BorderLayout.NORTH);
        panelContent.add(scrollHead, BorderLayout.NORTH);
        panelContent.add(panelBody, BorderLayout.CENTER);
        panelContent.add(panelFooter, BorderLayout.SOUTH);
        panelBackground.add(panelContent);
        panelBackground.add(panelRightButtons, BorderLayout.EAST);
        panelBackground.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelRightButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.add(panelBackground);
    }
    
    private void design(){
        getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        panelHead.setBackground(Design.COLOR_PRIMARY_DARK);
        btnAgregarProducto.setBackground(Design.COLOR_ACCENT);
        btnBuscarProducto.setBackground(Design.COLOR_ACCENT);
        btnBuscarProveedor.setBackground(Design.COLOR_ACCENT);
        btnCancelar.setBackground(Design.COLOR_ACCENT);
        btnEliminarProducto.setBackground(Design.COLOR_ACCENT);
        btnGuardar.setBackground(Design.COLOR_ACCENT);
        btnLimpiarTabla.setBackground(Design.COLOR_ACCENT);
        btnNuevo.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBackground(Design.COLOR_ACCENT);
        
        btnAgregarProducto.setBorder(Design.BORDER_BUTTON);
        btnBuscarProducto.setBorder(Design.BORDER_BUTTON);
        btnBuscarProveedor.setBorder(Design.BORDER_BUTTON);
        btnCancelar.setBorder(Design.BORDER_BUTTON);
        btnEliminarProducto.setBorder(Design.BORDER_BUTTON);
        btnGuardar.setBorder(Design.BORDER_BUTTON);
        btnLimpiarTabla.setBorder(Design.BORDER_BUTTON);
        btnNuevo.setBorder(Design.BORDER_BUTTON);
        btnSalir.setBorder(Design.BORDER_BUTTON);
    }

    private void tableConfigurator() {
        panelBody.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
                    tblDetalleProducto.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), widths[i]));
                }
            }
        });
    }

    public String generaNumCompra() {
        ClsCompra oCompra = new ClsCompra();
        try {
            rs = oCompra.obtenerUltimoIdCompra();
            while (rs.next()) {
                if (rs.getString(1) != null) {
                    Scanner s = new Scanner(rs.getString(1));
                    int c = s.useDelimiter("C").nextInt() + 1;

                    if (c < 10) {
                        return "C0000" + c;
                    }
                    if (c < 100) {
                        return "C000" + c;
                    }
                    if (c < 1000) {
                        return "C00" + c;
                    }
                    if (c < 10000) {
                        return "C0" + c;
                    } else {
                        return "C" + c;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        }
        return "C00001";
    }

    void CrearTablaDetalleProducto() {
        //Agregar Render
        for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
            Hashtable<Integer, Integer> hash = new Hashtable<>();
            hash.put(0, SwingConstants.CENTER);
            hash.put(1, SwingConstants.CENTER);
            hash.put(4, SwingConstants.CENTER);
            hash.put(5, SwingConstants.CENTER);
            hash.put(6, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(hash);
            tblDetalleProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        //Activar ScrollBar
        tblDetalleProducto.setAutoResizeMode(tblDetalleProducto.AUTO_RESIZE_OFF);
    }

    void limpiarCampos() {
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalCompra.setText("0.0");
        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtCodigoProducto.requestFocus();
    }

    void mirar() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);

        cboTipoDocumento.setEnabled(false);
        txtCodigoProducto.setEnabled(false);
        txtCantidadProducto.setEnabled(false);
        txtFecha.setEnabled(false);
        txtNumero.setEnabled(false);

        btnBuscarProveedor.setEnabled(false);
        btnBuscarProducto.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnEliminarProducto.setEnabled(false);
        btnLimpiarTabla.setEnabled(false);
        chkCambiarNumero.setEnabled(false);
        chkCambiarNumero.setSelected(false);

        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalCompra.setText("0.0");
        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtCodigoProducto.requestFocus();
    }

    void modificar() {

        btnNuevo.setEnabled(false);

        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);

        cboTipoDocumento.setEnabled(true);
        txtCodigoProducto.setEnabled(true);
        txtCantidadProducto.setEnabled(true);
        txtFecha.setEnabled(true);

        btnBuscarProveedor.setEnabled(true);
        btnBuscarProducto.setEnabled(true);
        btnAgregarProducto.setEnabled(true);
        btnEliminarProducto.setEnabled(true);
        btnLimpiarTabla.setEnabled(true);
        chkCambiarNumero.setEnabled(true);

        txtCodigoProducto.requestFocus();
    }
    
    void cargarComboTipoDocumento() {
        ClsTipoDocumento tipodocumento = new ClsTipoDocumento();
        ArrayList<ClsEntidadTipoDocumento> tipodocumentos = tipodocumento.listarTipoDocumento();
        Iterator iterator = tipodocumentos.iterator();
        DefaultComboBoxModel DefaultComboBoxModel = new DefaultComboBoxModel();
        DefaultComboBoxModel.removeAllElements();

        cboTipoDocumento.removeAll();
        String fila[] = new String[2];
        intContador = 0;

        while (iterator.hasNext()) {
            ClsEntidadTipoDocumento TipoDocumento = new ClsEntidadTipoDocumento();
            TipoDocumento = (ClsEntidadTipoDocumento) iterator.next();
            id[intContador] = TipoDocumento.getStrIdTipoDocumento();
            fila[0] = TipoDocumento.getStrIdTipoDocumento();
            fila[1] = TipoDocumento.getStrDescripcionTipoDocumento();
            DefaultComboBoxModel.addElement(TipoDocumento.getStrDescripcionTipoDocumento());
            intContador++;
        }
        cboTipoDocumento.setModel(DefaultComboBoxModel);
    }

    void BuscarProductoPorCodigo() {
        String busqueda = null;
        busqueda = txtCodigoProducto.getText();
        try {
            ClsProducto oProducto = new ClsProducto();

            rs = oProducto.listarProductoActivoPorParametro("codigo", busqueda);
            while (rs.next()) {
                if (rs.getString(2).equals(busqueda)) {

                    lblIdProducto.setText(rs.getString(1));
                    txtNombreProducto.setText(rs.getString(3));
                    txtDescripcionProducto.setText(rs.getString(4));
                    txtStockProducto.setText(rs.getString(5));
                    txtPrecioProducto.setText(rs.getString(7));
                }
                break;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }

    }

    void BuscarProveedorPorDefecto() {
        try {
            ClsProveedor oProveedor = new ClsProveedor();
            rs = oProveedor.listarProveedorPorParametro("id", "1");
            while (rs.next()) {
                lblIdProveedor.setText(rs.getString(1));
                txtNombreProveedor.setText(rs.getString(2));
                break;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    void CalcularTotal() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        double precio_prod = 0, cant_prod = 0, total_prod = 0;
        precio_prod = Double.parseDouble(txtPrecioProducto.getText());
        cant_prod = Double.parseDouble(txtCantidadProducto.getText());
        total_prod = precio_prod * cant_prod;
        txtTotalProducto.setText(String.valueOf(formateador.format(total_prod)));
    }

    public int recorrer(int id) {
        int fila = 0, valor = -1;

        fila = tblDetalleProducto.getRowCount();

        for (int f = 0; f < fila; f++) {
            if (Integer.parseInt(String.valueOf(defaultTableModel.getValueAt(f, 0))) == id) {
                valor = f;
                break;

            } else {
                valor = -1;
            }

        }
        return valor;
    }

    void agregardatos(int item, String cod, String nom, String descrip, double cant, double pre, double tot) {

        int p = recorrer(item);
        double n_cant, n_total;
        if (p > -1) {

            n_cant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) + cant;
            tblDetalleProducto.setValueAt(n_cant, p, 4);

            n_total = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) * Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 5)));
            tblDetalleProducto.setValueAt(n_total, p, 6);

        } else {
            String Datos[] = {String.valueOf(item), cod, nom, descrip, String.valueOf(cant), String.valueOf(pre), String.valueOf(tot)};
            defaultTableModel.addRow(Datos);
        }
        tblDetalleProducto.setModel(defaultTableModel);
    }

    void CalcularSubTotal() {
        double subtotal = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        subtotal = Double.parseDouble(txtTotalCompra.getText()) / 1.18;
        txtSubTotal.setText(String.valueOf(formateador.format(subtotal)));
    }

    void CalcularIGV() {
        double igv = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        igv = Double.parseDouble(txtSubTotal.getText()) * 0.18;
        txtIGV.setText(String.valueOf(formateador.format(igv)));
    }

    void CalcularTotal_Compra() {
        int fila = 0;
        double valorCompra = 0;
        fila = defaultTableModel.getRowCount();
        for (int f = 0; f < fila; f++) {
            valorCompra += Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 6)));
        }
        txtTotalCompra.setText(String.valueOf(valorCompra));
    }

    void limpiarTabla() {
        try {
            int filas = tblDetalleProducto.getRowCount();
            for (int i = 0; filas > i; i++) {
                defaultTableModel.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    void obtenerUltimoIdCompra() {
        try {
            ClsCompra oCompra = new ClsCompra();
            rs = oCompra.obtenerUltimoIdCompra();
            while (rs.next()) {
                idventa = rs.getInt(1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    void guardarDetalle() {
        obtenerUltimoIdCompra();
        ClsDetalleCompra detallecompras = new ClsDetalleCompra();
        ClsEntidadDetalleCompra detallecompra = new ClsEntidadDetalleCompra();
        ClsProducto productos = new ClsProducto();
        String codigo, strId;
        ClsEntidadProducto producto = new ClsEntidadProducto();
        int fila = 0;
        double cant = 0, ncant, stock;
        fila = tblDetalleProducto.getRowCount();
        for (int f = 0; f < fila; f++) {
            detallecompra.setStrIdCompra(String.valueOf(idventa));
            detallecompra.setStrIdProducto(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 0)));
            detallecompra.setStrCantidadDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            detallecompra.setStrPrecioDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 5)));
            detallecompra.setStrTotalDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 6)));
            detallecompras.agregarDetalleCompra(detallecompra);

            try {
                ClsProducto oProducto = new ClsProducto();

                rs = oProducto.listarProductoActivoPorParametro("id", ((String) tblDetalleProducto.getValueAt(f, 0)));
                while (rs.next()) {

                    cant = Double.parseDouble(rs.getString(5));
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }

            strId = ((String) tblDetalleProducto.getValueAt(f, 0));
            ncant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            stock = cant + ncant;
            producto.setStrStockProducto(String.valueOf(stock));
            productos.actualizarProductoStock(strId, producto);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRightButtons = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        panelBody = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalleProducto = new javax.swing.JTable();
        panelFooter = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtIGV = new javax.swing.JTextField();
        txtTotalCompra = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        scrollHead = new javax.swing.JScrollPane();
        panelHead = new javax.swing.JPanel();
        panel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTotalProducto = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnLimpiarTabla = new javax.swing.JButton();
        txtNumero = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JLabel();
        txtStockProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        lblIdProducto = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarProveedor = new javax.swing.JButton();
        txtNombreProveedor = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        lblIdProveedor = new javax.swing.JLabel();
        chkCambiarNumero = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Compras");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(null);

        panelRightButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelRightButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelRightButtons.setForeground(new java.awt.Color(255, 255, 255));
        panelRightButtons.setMinimumSize(new java.awt.Dimension(92, 407));
        panelRightButtons.setOpaque(false);
        panelRightButtons.setPreferredSize(new java.awt.Dimension(92, 407));
        panelRightButtons.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        btnNuevo.setText("Nueva Compra");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        panelRightButtons.add(btnNuevo);

        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setIconTextGap(0);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        panelRightButtons.add(btnCancelar);

        jPanel4.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        panelRightButtons.add(jPanel4);

        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        panelRightButtons.add(btnSalir);

        getContentPane().add(panelRightButtons);
        panelRightButtons.setBounds(810, 10, 170, 570);

        panelBody.setForeground(new java.awt.Color(255, 255, 255));
        panelBody.setOpaque(false);
        panelBody.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

        tblDetalleProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDetalleProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDetalleProducto.setRowHeight(22);
        jScrollPane3.setViewportView(tblDetalleProducto);

        panelBody.add(jScrollPane3);

        getContentPane().add(panelBody);
        panelBody.setBounds(10, 380, 790, 140);

        panelFooter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        panelFooter.setOpaque(false);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("SUB TOTAL");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 5, 10, 0));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("I.G.V.");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 5, 0, 10));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Total a pagar:  ");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 140, 50));

        txtSubTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setEnabled(false);
        jPanel6.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 28, 10, 0));

        txtIGV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtIGV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIGV.setEnabled(false);
        jPanel6.add(txtIGV, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 28, 10, 0));

        txtTotalCompra.setBackground(java.awt.Color.darkGray);
        txtTotalCompra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTotalCompra.setForeground(new java.awt.Color(0, 255, 102));
        txtTotalCompra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCompra.setDisabledTextColor(new java.awt.Color(0, 255, 102));
        txtTotalCompra.setEnabled(false);
        jPanel6.add(txtTotalCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 390, 50));

        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/money_22144 (1).png"))); // NOI18N
        btnGuardar.setText("Comprar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel6.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, 166, 70));

        panelFooter.add(jPanel6);

        getContentPane().add(panelFooter);
        panelFooter.setBounds(10, 560, 800, 82);

        scrollHead.setOpaque(false);
        scrollHead.setPreferredSize(new java.awt.Dimension(1000, 330));

        panelHead.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        panelHead.setForeground(new java.awt.Color(255, 255, 255));

        panel.setForeground(new java.awt.Color(255, 255, 255));
        panel.setOpaque(false);
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pre transacción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Cantidad: ");
        jPanel3.add(jLabel21);
        jLabel21.setBounds(0, 10, 100, 40);

        txtCantidadProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCantidadProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });
        jPanel3.add(txtCantidadProducto);
        txtCantidadProducto.setBounds(100, 20, 250, 40);

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Total a parar: ");
        jPanel3.add(jLabel24);
        jLabel24.setBounds(0, 50, 100, 40);

        txtTotalProducto.setBackground(new java.awt.Color(204, 255, 204));
        txtTotalProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotalProducto.setForeground(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalProducto.setDisabledTextColor(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setEnabled(false);
        jPanel3.add(txtTotalProducto);
        txtTotalProducto.setBounds(100, 60, 250, 40);

        jPanel5.setMaximumSize(new java.awt.Dimension(144, 92));
        jPanel5.setMinimumSize(new java.awt.Dimension(144, 92));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(144, 92));
        jPanel5.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Agregar_p1.png"))); // NOI18N
        btnAgregarProducto.setText("Agregar producto");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btnAgregarProducto);

        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Remove.png"))); // NOI18N
        btnEliminarProducto.setText("Borrar producto");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btnEliminarProducto);

        btnLimpiarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/nuevo1.png"))); // NOI18N
        btnLimpiarTabla.setText("Limpiar tabla");
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
            }
        });
        jPanel5.add(btnLimpiarTabla);

        jPanel3.add(jPanel5);
        jPanel5.setBounds(440, 10, 150, 90);

        panel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 600, 110));

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel.add(txtNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 240, 110, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nº de compra");
        panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 220, 110, 20));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Código");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 13, 70, -1));

        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });
        jPanel2.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 28, 190, 30));

        btnBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 28, 120, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Nonmbre:");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, 120, 20));

        txtNombreProducto.setEnabled(false);
        jPanel2.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 320, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Stock:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 90, -1));
        jPanel2.add(txtDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 30, 20));

        txtStockProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtStockProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStockProducto.setEnabled(false);
        jPanel2.add(txtStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 30, 90, 50));

        txtPrecioProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.setEnabled(false);
        jPanel2.add(txtPrecioProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 90, 50));
        jPanel2.add(lblIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 20, 20));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Costo:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 90, -1));

        panel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, 600, 110));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Proveedor:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 190, 20));

        btnBuscarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarProveedor.setText("Buscar");
        btnBuscarProveedor.setAlignmentY(1.0F);
        btnBuscarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedorActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 35, 120, 30));

        txtNombreProveedor.setEnabled(false);
        jPanel1.add(txtNombreProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 190, 30));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Documento:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 15, 100, -1));

        cboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cboTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 35, 130, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fecha:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 15, 80, 20));
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 35, 100, 30));
        jPanel1.add(lblIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 20, 20));

        panel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 600, 70));

        chkCambiarNumero.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCambiarNumero.setForeground(new java.awt.Color(255, 255, 255));
        chkCambiarNumero.setText("Cambiar Número");
        chkCambiarNumero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkCambiarNumero.setOpaque(false);
        chkCambiarNumero.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkCambiarNumeroStateChanged(evt);
            }
        });
        panel.add(chkCambiarNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 260, 180, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cprs.png"))); // NOI18N
        jLabel4.setAlignmentY(0.0F);
        panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, -1, 180));

        panelHead.add(panel);

        scrollHead.setViewportView(panelHead);

        getContentPane().add(scrollHead);
        scrollHead.setBounds(10, 0, 790, 370);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion = "Nuevo";
        modificar();
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        limpiarTabla();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtCantidadProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyReleased
        CalcularTotal();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            btnAgregarProducto.requestFocus();
    }//GEN-LAST:event_txtCantidadProductoKeyReleased

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        double cant; //stock, 

        if (!txtCantidadProducto.getText().equals("")) {
            if (txtCantidadProducto.getText().equals("")) {
                txtCantidadProducto.setText("0");
                cant = Double.parseDouble(txtCantidadProducto.getText());
            } else {
                cant = Double.parseDouble(txtCantidadProducto.getText());
            }
            if (cant > 0) {
                int d1 = Integer.parseInt(lblIdProducto.getText());
                String d2 = txtCodigoProducto.getText();
                String d3 = txtNombreProducto.getText();
                String d4 = txtDescripcionProducto.getText();
                double d5 = Double.parseDouble(txtCantidadProducto.getText());
                double d6 = Double.parseDouble(txtPrecioProducto.getText());
                double d7 = Double.parseDouble(txtTotalProducto.getText());
                agregardatos(d1, d2, d3, d4, d5, d6, d7);

                CalcularTotal_Compra();
                CalcularSubTotal();
                CalcularIGV();

                txtCantidadProducto.setText("");
                txtTotalProducto.setText("");
                txtCodigoProducto.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad mayor a 0");
                txtCantidadProducto.requestFocus();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
            txtCantidadProducto.requestFocus();
        }

    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        int fila = tblDetalleProducto.getSelectedRow();
        if (fila > 0) {
            defaultTableModel.removeRow(fila);
            CalcularTotal_Compra();
            CalcularSubTotal();
            CalcularIGV();
        } else if (fila == 0) {
            defaultTableModel.removeRow(fila);
            txtSubTotal.setText("0.0");
            txtIGV.setText("0.0");
            txtTotalCompra.setText("0.0");

            CalcularTotal_Compra();
            CalcularSubTotal();
            CalcularIGV();
        }

        CalcularTotal_Compra();
        CalcularSubTotal();
        CalcularIGV();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTablaActionPerformed
        limpiarTabla();
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalCompra.setText("0.0");
    }//GEN-LAST:event_btnLimpiarTablaActionPerformed

    private void txtCodigoProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyReleased
        BuscarProductoPorCodigo();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtCantidadProducto.requestFocus();
    }//GEN-LAST:event_txtCodigoProductoKeyReleased

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        Consultas.FrmBuscarProducto_Compra producto = new Consultas.FrmBuscarProducto_Compra();
        Presentacion.FrmPrincipal.Escritorio.add(producto);
        producto.toFront();
        producto.setVisible(true);
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnBuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProveedorActionPerformed
        Consultas.FrmBuscarProveedor_Compra proveedor = new Consultas.FrmBuscarProveedor_Compra();
        Presentacion.FrmPrincipal.Escritorio.add(proveedor);
        proveedor.toFront();
        proveedor.setVisible(true);
    }//GEN-LAST:event_btnBuscarProveedorActionPerformed

    private void chkCambiarNumeroStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkCambiarNumeroStateChanged
        if (chkCambiarNumero.isSelected()) {
            txtNumero.setText("");
            txtNumero.setEnabled(true);
        } else {
            txtNumero.setEnabled(false);
            numCompra = generaNumCompra();
            txtNumero.setText(numCompra);
        }
    }//GEN-LAST:event_chkCambiarNumeroStateChanged

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "¿Desea Generar la Compra?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (accion.equals("Nuevo")) {
                ClsCompra compras = new ClsCompra();
                ClsEntidadCompra venta = new ClsEntidadCompra();
                venta.setStrIdTipoDocumento(id[cboTipoDocumento.getSelectedIndex()]);
                venta.setStrIdProveedor(lblIdProveedor.getText());
                venta.setStrIdEmpleado(IdEmpleado);
                venta.setStrNumeroCompra(txtNumero.getText());
                venta.setStrFechaCompra(txtFecha.getDate());
                venta.setStrSubTotalCompra(txtSubTotal.getText());
                venta.setStrIgvCompra(txtIGV.getText());
                venta.setStrTotalCompra(txtTotalCompra.getText());
                venta.setStrEstadoCompra("NORMAL");
                compras.agregarCompra(venta);
                guardarDetalle();
            }
            mirar();
            limpiarTabla();
            numCompra = generaNumCompra();
            txtNumero.setText(numCompra);
            BuscarProveedorPorDefecto();
        }
        if (result == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Compra Anulada!");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        BuscarProveedorPorDefecto();
        cargarComboTipoDocumento();
    }//GEN-LAST:event_formComponentShown

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9'))
            evt.consume();
    }//GEN-LAST:event_txtCodigoProductoKeyTyped

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9'))
            evt.consume();
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnBuscarProveedor;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboTipoDocumento;
    private javax.swing.JCheckBox chkCambiarNumero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblIdProducto;
    public static javax.swing.JLabel lblIdProveedor;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHead;
    private javax.swing.JPanel panelRightButtons;
    private javax.swing.JScrollPane scrollHead;
    private javax.swing.JTable tblDetalleProducto;
    private javax.swing.JTextField txtCantidadProducto;
    public static javax.swing.JTextField txtCodigoProducto;
    public static javax.swing.JLabel txtDescripcionProducto;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIGV;
    public static javax.swing.JTextField txtNombreProducto;
    public static javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNumero;
    public static javax.swing.JTextField txtPrecioProducto;
    public static javax.swing.JTextField txtStockProducto;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotalCompra;
    private javax.swing.JTextField txtTotalProducto;
    // End of variables declaration//GEN-END:variables
}
