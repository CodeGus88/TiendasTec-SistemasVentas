/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FrmEmpleado extends javax.swing.JInternalFrame {
    private Connection connection=new ClsConexion().getConection();
    //algoritmos
    public static String MD2 = "MD2";
    public static String MD5 = "MD5";
    public static String SHA1 = "SHA-1";
    public static String SHA256 = "SHA-256";
    public static String SHA384 = "SHA-384";
    public static String SHA512 = "SHA-512";
    
    String Total;
    String strCodigo;
    String accion;
    int registros;
    String id[]=new String[50];
    static int intContador;
    
    //-----------------------------------------------
    public String codigo;
    static Connection conn=null;
    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    String criterio,busqueda;
    
    public FrmEmpleado() {
        initComponents();
        //---------------------FECHA ACTUAL-------------------------------
        Date date=new Date();
        String format=new String("yyyy-MM-dd");
        SimpleDateFormat formato=new SimpleDateFormat(format);
        txtFechaIng.setDate(date);
        txtFechaNac.setDate(date);
        
        cargarComboTipoUsuario();
        tabEmpleado.setIconAt(tabEmpleado.indexOfComponent(pBuscar), new ImageIcon("src/iconos/busca_p1.png"));
        tabEmpleado.setIconAt(tabEmpleado.indexOfComponent(pNuevo), new ImageIcon("src/iconos/nuevo1.png"));
        buttonGroup1.add(rbtnMasculino);
        buttonGroup1.add(rbtnFemenino);
        buttonGroup2.add(rbtnActivo);
        buttonGroup2.add(rbtnInactivo);
        buttonGroup3.add(rbtnCodigo);
        buttonGroup3.add(rbtnNombre);
        buttonGroup3.add(rbtnDni);
        mirar();        
        actualizarTabla();
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(976, 492);
        CrearTabla();                
        CantidadTotal();
        //Ocultar Columnas
        //tblEmpleado.removeColumn(tblEmpleado.getColumnModel().getColumn(14));
        
    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------
  void CrearTabla(){
   //--------------------PRESENTACION DE JTABLE----------------------
      
        TableCellRenderer render = new DefaultTableCellRenderer() { 

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==3 || column==4 || column==6 || column==7 || column==9 || column==10 || column==11 || column==12 || column==13 || column==15){
                        l.setHorizontalAlignment(SwingConstants.CENTER); 
                    }else{
                        l.setHorizontalAlignment(SwingConstants.LEFT);
                    }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE); 
                }else{
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        //l.setBackground(new Color(232, 232, 232));
                        l.setBackground(new Color(254, 227, 152));
                    }
                }     
                return l; 
            } 
        }; 
        
        //Agregar Render
        for (int i=0;i<tblEmpleado.getColumnCount();i++){
            tblEmpleado.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblEmpleado.setAutoResizeMode(tblEmpleado.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {40,80,100,40,80,200,80,80,150,80,80,80,80,80,80,100};
        for(int i = 0; i < tblEmpleado.getColumnCount(); i++) {
            tblEmpleado.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Ocultar columnas
        setOcultarColumnasJTable(tblEmpleado,new int[]{14});
    }
   void CantidadTotal(){
        Total= String.valueOf(tblEmpleado.getRowCount());   
        lblEstado.setText("Se cargaron " + Total + " registros");      
   }
   void limpiarCampos(){
       txtCodigo.setText("");
       txtNombre.setText("");
       txtApellido.setText("");
       txtDireccion.setText("");
       txtTelefono.setText("");
       txtCelular.setText("");
       txtEmail.setText("");
       txtDni.setText("");
       txtSueldo.setText("");
       txtUsuario.setText("");
       txtContrasenia.setText("");
       
       rbtnMasculino.setSelected(true);
       rbtnFemenino.setSelected(false);
       rbtnActivo.setSelected(true);
       rbtnInactivo.setSelected(false);
       txtBusqueda.setText("");
   }
    
        
   void mirar(){
        tblEmpleado.setEnabled(true);
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
        
        txtCodigo.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        rbtnMasculino.setEnabled(false);
        rbtnFemenino.setEnabled(false);
        txtFechaNac.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtCelular.setEnabled(false);
        txtEmail.setEnabled(false);
        txtDni.setEnabled(false);
        txtFechaIng.setEnabled(false);
        txtSueldo.setEnabled(false);
        rbtnActivo.setEnabled(false);
        rbtnInactivo.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtContrasenia.setEnabled(false);
        cboTipoUsuario.setEnabled(false);
    }
   
    void modificar(){
        tblEmpleado.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
        
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        rbtnMasculino.setEnabled(true);
        rbtnFemenino.setEnabled(true);
        txtFechaNac.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtCelular.setEnabled(true);
        txtEmail.setEnabled(true);
        txtDni.setEnabled(true);
        txtFechaIng.setEnabled(true);
        txtSueldo.setEnabled(true);
        rbtnActivo.setEnabled(true);
        rbtnInactivo.setEnabled(true);
        txtUsuario.setEnabled(true);
        txtContrasenia.setEnabled(true);
        cboTipoUsuario.setEnabled(true);
        txtNombre.requestFocus();
    }
   
   
    void actualizarTabla(){
       String titulos[]={"ID","Nombre","Apellidos","Sexo","Fecha Nac.","Dirección","Teléfono","Celular","Email","DNI","Fecha Ing.","Sueldo","Estado","Usuario","Contraseña","Tipo de Usuario"};
          
       ClsEmpleado empleados=new ClsEmpleado();
       ArrayList<ClsEntidadEmpleado> empleado=empleados.listarEmpleado();
       Iterator iterator=empleado.iterator();
       DefaultTableModel defaultTableModel=new DefaultTableModel(null,titulos);
       
       String fila[]=new String[16];
       while(iterator.hasNext()){
           ClsEntidadEmpleado Empleado=new ClsEntidadEmpleado();
           Empleado=(ClsEntidadEmpleado) iterator.next();
           fila[0]=Empleado.getStrIdEmpleado();
           fila[1]=Empleado.getStrNombreEmpleado();
           fila[2]=Empleado.getStrApellidoEmpleado();
           fila[3]=Empleado.getStrSexoEmpleado();
           fila[4]=Empleado.getStrFechaNacEmpleado().toString();
           fila[5]=Empleado.getStrDireccionEmpleado();
           fila[6]=Empleado.getStrTelefonoEmpleado();
           fila[7]=Empleado.getStrCelularEmpleado();
           fila[8]=Empleado.getStrEmailEmpleado();
           fila[9]=Empleado.getStrDniEmpleado();
           fila[10]=Empleado.getStrFechaIngEmpleado().toString();
           fila[11]=Empleado.getStrSueldoEmpleado();
           fila[12]=Empleado.getStrEstadoEmpleado();
           fila[13]=Empleado.getStrUsuarioEmpleado();
           fila[14]=Empleado.getStrContraseniaEmpleado();
           fila[15]=Empleado.getStrTipoUsuario();
           defaultTableModel.addRow(fila);
                
       }
       tblEmpleado.setModel(defaultTableModel);


   }
   void BuscarEmpleado(){
        String titulos[]={"ID","Nombre","Apellidos","Sexo","Fecha Nac.","Dirección","Teléfono","Celular","Email","DNI","Fecha Ing.","Sueldo","Estado","Usuario","Contraseña","Tipo de Usuario"};
        dtm.setColumnIdentifiers(titulos);
        
        ClsEmpleado empleado=new ClsEmpleado();
        busqueda=txtBusqueda.getText();
        if(rbtnCodigo.isSelected()){
            criterio="id";
        }else if(rbtnNombre.isSelected()){
            criterio="nombre";
        }else if(rbtnDni.isSelected()){
            criterio="dni";
        }
        try{
            rs=empleado.listarEmpleadoPorParametro(criterio,busqueda);
            boolean encuentra=false;
            String Datos[]=new String[16];
            int f,i;
            f=dtm.getRowCount();
            if(f>0){
                for(i=0;i<f;i++){
                    dtm.removeRow(0);
                }
            }
            while(rs.next()){
                Datos[0]=(String) rs.getString(1);
                Datos[1]=(String) rs.getString(2);
                Datos[2]=(String) rs.getString(3);
                Datos[3]=(String) rs.getString(4);
                Datos[4]=(String) rs.getString(5);
                Datos[5]=(String) rs.getString(6);
                Datos[6]=(String) rs.getString(7);
                Datos[7]=(String) rs.getString(8);
                Datos[8]=(String) rs.getString(9);
                Datos[9]=(String) rs.getString(10);
                Datos[10]=(String) rs.getString(11);
                Datos[11]=(String) rs.getString(12);
                Datos[12]=(String) rs.getString(13);
                Datos[13]=(String) rs.getString(14);
                Datos[14]=(String) rs.getString(15);
                Datos[15]=(String) rs.getString(16);
                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblEmpleado.setModel(dtm);
    }
    void listardatos(){

        DefaultTableModel defaultTableModel=new DefaultTableModel();
        if(registros==-1){
            JOptionPane.showMessageDialog(null,"¡Se debe seleccionar un registro!");
        }else{
            defaultTableModel=(DefaultTableModel) tblEmpleado.getModel();
            strCodigo=((String) defaultTableModel.getValueAt(registros,0));
            txtCodigo.setText((String)defaultTableModel.getValueAt(registros,0));
            txtNombre.setText((String)defaultTableModel.getValueAt(registros,1));
            txtApellido.setText((String)defaultTableModel.getValueAt(registros,2));
            if ("M".equals((String) defaultTableModel.getValueAt(registros,3))){
               rbtnMasculino.setSelected(true);
            }else if ("F".equals((String) defaultTableModel.getValueAt(registros,3))){
               rbtnFemenino.setSelected(true);
            }
            txtFechaNac.setDate((Date)defaultTableModel.getValueAt(registros,4));
            txtDireccion.setText((String)defaultTableModel.getValueAt(registros,5));
            txtTelefono.setText((String)defaultTableModel.getValueAt(registros,6));
            txtCelular.setText((String)defaultTableModel.getValueAt(registros,7));
            txtEmail.setText((String)defaultTableModel.getValueAt(registros,8));
            txtDni.setText((String)defaultTableModel.getValueAt(registros,9));
            txtFechaIng.setDate((Date)defaultTableModel.getValueAt(registros,10));
            txtSueldo.setText((String)defaultTableModel.getValueAt(registros,11));
            if ("ACTIVO".equals((String) defaultTableModel.getValueAt(registros,12))){
               rbtnActivo.setSelected(true);
            }else if ("INACTIVO".equals((String) defaultTableModel.getValueAt(registros,12))){
               rbtnInactivo.setSelected(true);
            }
            txtUsuario.setText((String)defaultTableModel.getValueAt(registros,13));
            txtContrasenia.setText((String)defaultTableModel.getValueAt(registros,14));
            cboTipoUsuario.setSelectedItem((String)defaultTableModel.getValueAt(registros,15));
            tblEmpleado.setRowSelectionInterval(registros,registros);
        }
    
    }
    void cargarComboTipoUsuario(){
       ClsTipoUsuario tipousuario=new ClsTipoUsuario();
       ArrayList<ClsEntidadTipoUsuario> tipousuarios=tipousuario.listarTipoUsuario();
       Iterator iterator=tipousuarios.iterator();
       DefaultComboBoxModel DefaultComboBoxModel=new DefaultComboBoxModel();
       DefaultComboBoxModel.removeAllElements();
       
       cboTipoUsuario.removeAll();
       String fila[]=new String[2];
       intContador=0;
       
       while(iterator.hasNext()){
           ClsEntidadTipoUsuario TipoDocumento = new ClsEntidadTipoUsuario();
           TipoDocumento=(ClsEntidadTipoUsuario) iterator.next();
           id[intContador]=TipoDocumento.getStrIdTipoUsuario();
           fila[0]=TipoDocumento.getStrIdTipoUsuario();
           fila[1]=TipoDocumento.getStrDescripcionTipoUsuario();
           DefaultComboBoxModel.addElement(TipoDocumento.getStrDescripcionTipoUsuario());
           intContador++;              
       }
       cboTipoUsuario.setModel(DefaultComboBoxModel);
    }
//-----------------------ENCRIPTACION - SHA----------------------------------------
    private static String toHexadecimal(byte[] digest)
    {
        String hash = "";
        for(byte aux : digest) 
        {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }
    
    public static String getStringMessageDigest(String message, String algorithm)
    {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try 
        {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        }
        catch (NoSuchAlgorithmException ex) 
        {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    } 
    private void setOcultarColumnasJTable(JTable tbl, int columna[]){
        for(int i=0;i<columna.length;i++)
        {
             tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
             tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
             tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
             tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabEmpleado = new javax.swing.JTabbedPane();
        pBuscar = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleado = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnDni = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        btnNuevo = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        pNuevo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rbtnMasculino = new javax.swing.JRadioButton();
        rbtnFemenino = new javax.swing.JRadioButton();
        txtFechaNac = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtFechaIng = new com.toedter.calendar.JDateChooser();
        txtTelefono = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSueldo = new javax.swing.JTextField();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDireccion = new javax.swing.JTextArea();
        txtUsuario = new javax.swing.JTextField();
        cboTipoUsuario = new javax.swing.JComboBox();
        txtContrasenia = new javax.swing.JPasswordField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Empleados");
        getContentPane().setLayout(null);

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setIconTextGap(0);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar);
        btnModificar.setBounds(830, 120, 130, 90);

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
        getContentPane().add(btnCancelar);
        btnCancelar.setBounds(830, 210, 130, 100);

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
        getContentPane().add(btnSalir);
        btnSalir.setBounds(830, 360, 130, 100);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar);
        btnGuardar.setBounds(830, 30, 130, 90);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mantenimiento"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(820, 10, 140, 450);

        pBuscar.setBackground(new java.awt.Color(255, 255, 255));
        pBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblEmpleado.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEmpleado.setRowHeight(22);
        tblEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpleadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmpleado);

        pBuscar.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 790, 320));
        pBuscar.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 200, 20));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/report.png"))); // NOI18N
        jButton3.setText("Reporte");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pBuscar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 120, 50));

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        pBuscar.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 500, -1));

        rbtnCodigo.setText("ID Empleado");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        pBuscar.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 100, -1));

        rbtnDni.setText("C.I");
        rbtnDni.setOpaque(false);
        pBuscar.add(rbtnDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, -1, -1));

        rbtnNombre.setText("Nombre o Apellido");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        pBuscar.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 120, -1));

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        btnNuevo.setText("Nuevo Registro");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        pBuscar.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 30, 120, 50));

        jLabel5.setBackground(new java.awt.Color(255, 153, 0));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel5.setOpaque(true);
        pBuscar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, 80));

        tabEmpleado.addTab("Buscar", pBuscar);

        pNuevo.setBackground(new java.awt.Color(255, 255, 255));
        pNuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pNuevo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 420));

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        pNuevo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 150, -1));

        txtCodigo.setEnabled(false);
        pNuevo.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 150, -1));

        jLabel3.setText("ID Empleado:");
        pNuevo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 70, 20));

        jLabel2.setText("Nombre:");
        pNuevo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 50, 20));

        jLabel6.setText("Apellidos:");
        pNuevo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 60, 20));

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });
        pNuevo.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 150, -1));

        jLabel7.setText("Sexo:");
        pNuevo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 40, 20));

        rbtnMasculino.setText("Masculino");
        pNuevo.add(rbtnMasculino, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, -1, -1));

        rbtnFemenino.setText("Femenino");
        pNuevo.add(rbtnFemenino, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        txtFechaNac.setDateFormatString("yyyy-MM-dd");
        pNuevo.add(txtFechaNac, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 160, 30));

        jLabel8.setText("Fecha de Nacimiento:");
        pNuevo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        jLabel9.setText("Dirección:");
        pNuevo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 60, 20));

        jLabel10.setText("Teléfono:");
        pNuevo.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 60, 20));

        jLabel11.setText("Celular:");
        pNuevo.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 50, 20));

        jLabel12.setText("Email:");
        pNuevo.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 40, 20));

        jLabel13.setText("CI:");
        pNuevo.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 40, 20));

        jLabel14.setText("Fecha de Ingreso:");
        pNuevo.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 100, 20));

        txtFechaIng.setDateFormatString("yyyy-MM-dd");
        pNuevo.add(txtFechaIng, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 160, -1));
        pNuevo.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 150, -1));
        pNuevo.add(txtCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 150, -1));
        pNuevo.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 160, -1));

        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });
        pNuevo.add(txtDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 150, -1));

        jLabel15.setText("Sueldo:");
        pNuevo.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 50, -1));
        pNuevo.add(txtSueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 160, -1));

        rbtnActivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbtnActivo.setText("ACTIVO");
        pNuevo.add(rbtnActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, -1, -1));

        rbtnInactivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbtnInactivo.setText("INACTIVO");
        pNuevo.add(rbtnInactivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, -1, -1));

        jLabel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado"));
        pNuevo.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 150, 170));

        jLabel17.setText("Usuario:");
        pNuevo.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 230, 50, 20));

        jLabel18.setText("Contraseña:");
        pNuevo.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 260, 70, 20));

        jLabel19.setText("Tipo de usuario:");
        pNuevo.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 80, 30));

        txtDireccion.setColumns(20);
        txtDireccion.setRows(5);
        jScrollPane2.setViewportView(txtDireccion);

        pNuevo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 430, 40));
        pNuevo.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, 170, -1));

        cboTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pNuevo.add(cboTipoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 290, 170, 30));

        txtContrasenia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContraseniaKeyTyped(evt);
            }
        });
        pNuevo.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, 170, -1));

        jLabel21.setForeground(new java.awt.Color(0, 51, 153));
        jLabel21.setText("Los campos marcado con un asterísco (*) son obligatorios");
        pNuevo.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 280, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("*");
        pNuevo.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 290, 20, 20));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 153));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("*");
        pNuevo.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 20, 20));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 51, 153));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("*");
        pNuevo.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 20, 20));

        tabEmpleado.addTab("Nuevo / Modificar", pNuevo);

        getContentPane().add(tabEmpleado);
        tabEmpleado.setBounds(10, 10, 810, 450);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if(tblEmpleado.getSelectedRows().length > 0 ) {
            accion="Modificar";
            modificar();
            tabEmpleado.setSelectedIndex(tabEmpleado.indexOfComponent(pNuevo));
        }else{
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro!");
        }

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        tabEmpleado.setSelectedIndex(tabEmpleado.indexOfComponent(pBuscar));
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion="Nuevo";
        modificar();
        limpiarCampos();
        txtContrasenia.setEnabled(true);
        tabEmpleado.setSelectedIndex(tabEmpleado.indexOfComponent(pNuevo));
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
//----------------------VALIDACIÓN DE DATOS-------------------------------------
    public boolean validardatos(){
        if (txtNombre.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Ingrese nombre del Empleado");
            txtNombre.requestFocus();
            txtNombre.setBackground(Color.YELLOW);
            return false;
        }else if (txtApellido.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Ingrese apellido del Empleado");
            txtApellido.requestFocus();
            txtApellido.setBackground(Color.YELLOW);
            return false;
        }else if (txtDni.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Ingrese DNI del Empleado");
            txtDni.requestFocus();
            txtDni.setBackground(Color.YELLOW);
            return false;
        }else{
            return true;
        }

    }
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String algorithm="SHA-512";  
        String resultado1,resultado2;
        String contraseña=null;
    if(validardatos()==true){     
        if(accion.equals("Nuevo")){
            ClsEmpleado empleados=new ClsEmpleado();
            ClsEntidadEmpleado empleado=new ClsEntidadEmpleado();
            empleado.setStrNombreEmpleado(txtNombre.getText());
            empleado.setStrApellidoEmpleado(txtApellido.getText());
            if (rbtnMasculino.isSelected()){
                empleado.setStrSexoEmpleado("M");
            }else if (rbtnFemenino.isSelected()){
                empleado.setStrSexoEmpleado("F");
            }
            empleado.setStrFechaNacEmpleado(txtFechaNac.getDate());
            empleado.setStrDireccionEmpleado(txtDireccion.getText());
            empleado.setStrTelefonoEmpleado(txtTelefono.getText());
            empleado.setStrCelularEmpleado(txtCelular.getText());
            empleado.setStrEmailEmpleado(txtEmail.getText());
            empleado.setStrDniEmpleado(txtDni.getText());
            empleado.setStrFechaIngEmpleado(txtFechaIng.getDate());
            if(txtSueldo.getText().equals("")){
                empleado.setStrSueldoEmpleado("0");
            }else{
                empleado.setStrSueldoEmpleado(txtSueldo.getText());
            }
            if (rbtnActivo.isSelected()){
                empleado.setStrEstadoEmpleado("ACTIVO");
            }else if (rbtnInactivo.isSelected()){
                empleado.setStrEstadoEmpleado("INACTIVO");
            }
            empleado.setStrUsuarioEmpleado(txtUsuario.getText());
            if (txtContrasenia.getText().length()==0){
                empleado.setStrContraseniaEmpleado("");
            }else{
                contraseña=txtContrasenia.getText();
                resultado1=getStringMessageDigest(contraseña,algorithm);   
                empleado.setStrContraseniaEmpleado(resultado1);
            }
            
            empleado.setStrIdTipoUsuario(id[cboTipoUsuario.getSelectedIndex()]);
            empleados.agregarEmpleado(empleado);
            actualizarTabla();
            CantidadTotal();
        }
        if(accion.equals("Modificar")){
            ClsEmpleado empleados=new ClsEmpleado();
            ClsEntidadEmpleado empleado=new ClsEntidadEmpleado();
            empleado.setStrNombreEmpleado(txtNombre.getText());
            empleado.setStrApellidoEmpleado(txtApellido.getText());
            if (rbtnMasculino.isSelected()){
                empleado.setStrSexoEmpleado("M");
            }else if (rbtnFemenino.isSelected()){
                empleado.setStrSexoEmpleado("F");
            }
            empleado.setStrFechaNacEmpleado(txtFechaNac.getDate());
            empleado.setStrDireccionEmpleado(txtDireccion.getText());
            empleado.setStrTelefonoEmpleado(txtTelefono.getText());
            empleado.setStrCelularEmpleado(txtCelular.getText());
            empleado.setStrEmailEmpleado(txtEmail.getText());
            empleado.setStrDniEmpleado(txtDni.getText());
            empleado.setStrFechaIngEmpleado(txtFechaIng.getDate());
            if(txtSueldo.getText().equals("")){
                empleado.setStrSueldoEmpleado("0");
            }else{
                empleado.setStrSueldoEmpleado(txtSueldo.getText());
            }
            if (rbtnActivo.isSelected()){
                empleado.setStrEstadoEmpleado("ACTIVO");
            }else if (rbtnInactivo.isSelected()){
                empleado.setStrEstadoEmpleado("INACTIVO");
            }
            empleado.setStrUsuarioEmpleado(txtUsuario.getText());
            //empleado.setStrContraseñaEmpleado(txtContraseña.getText());
            if (txtContrasenia.getText().length()==0){
                empleado.setStrContraseniaEmpleado("");
            }else{
                contraseña=txtContrasenia.getText();
                resultado1=getStringMessageDigest(contraseña,algorithm);   
                empleado.setStrContraseniaEmpleado(resultado1);
            }
            empleado.setStrIdTipoUsuario(id[cboTipoUsuario.getSelectedIndex()]);
            empleados.modificarEmpleado(strCodigo, empleado);
            actualizarTabla();
            limpiarCampos();
            modificar();
            CantidadTotal();
        }
        CrearTabla();
        mirar();
        tabEmpleado.setSelectedIndex(tabEmpleado.indexOfComponent(pBuscar));
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblEmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadoMouseClicked
    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
    Date fechanac = null,fechaing=null;
       
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblEmpleado.getSelectedRow();

        if (fila == -1){
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
        }else{
            defaultTableModel = (DefaultTableModel)tblEmpleado.getModel();
            strCodigo =  ((String) defaultTableModel.getValueAt(fila, 0));
            txtCodigo.setText((String) defaultTableModel.getValueAt(fila, 0));
            txtNombre.setText((String) defaultTableModel.getValueAt(fila, 1));
            txtApellido.setText((String) defaultTableModel.getValueAt(fila, 2));
            if ("M".equals((String) defaultTableModel.getValueAt(fila,3))){
               rbtnMasculino.setSelected(true);
            }else if ("F".equals((String) defaultTableModel.getValueAt(fila,3))){
               rbtnFemenino.setSelected(true);
            }
 
            String fecha_nac=defaultTableModel.getValueAt(fila, 4).toString().trim();
            try{
                fechanac=formatoDelTexto.parse(fecha_nac);
                
           } catch (ParseException ex) {

            ex.printStackTrace();

            }
            txtFechaNac.setDate(fechanac);
            txtDireccion.setText((String) defaultTableModel.getValueAt(fila, 5));
            txtTelefono.setText((String) defaultTableModel.getValueAt(fila, 6));
            txtCelular.setText((String) defaultTableModel.getValueAt(fila, 7));
            txtEmail.setText((String) defaultTableModel.getValueAt(fila, 8));
            txtDni.setText((String) defaultTableModel.getValueAt(fila, 9));
            
            String fecha_ing=defaultTableModel.getValueAt(fila, 10).toString().trim();
            try{
                fechaing=formatoDelTexto.parse(fecha_ing);
                
           } catch (ParseException ex) {

            ex.printStackTrace();

            }
            txtFechaIng.setDate(fechaing);
            txtSueldo.setText((String) defaultTableModel.getValueAt(fila, 11));
            if ("ACTIVO".equals((String) defaultTableModel.getValueAt(fila,12))){
               rbtnActivo.setSelected(true);
            }else if ("INACTIVO".equals((String) defaultTableModel.getValueAt(fila,12))){
               rbtnInactivo.setSelected(true);
            }
            txtUsuario.setText((String) defaultTableModel.getValueAt(fila, 13));
            txtContrasenia.setText((String) defaultTableModel.getValueAt(fila, 14));
            cboTipoUsuario.setSelectedItem((String)defaultTableModel.getValueAt(fila,15));
        }

        mirar();
    }//GEN-LAST:event_tblEmpleadoMouseClicked

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarEmpleado();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        char car = evt.getKeyChar();
        if((car<'a' || car>'z') && (car<'A' || car>'Z')) evt.consume();
        txtNombre.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtApellido.requestFocus();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        char car = evt.getKeyChar();
        if((car<'a' || car>'z') && (car<'A' || car>'Z')) evt.consume();
        txtApellido.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtDni.getText().length();
        if(txtDni.getText().trim().length()<8){

        }else{
            i=10;
            String com=txtDni.getText().substring(0, 7);
            txtDni.setText("");
            txtDni.setText(com);
        }
        txtDni.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtDniKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map p=new HashMap();
        p.put("busqueda",txtBusqueda.getText());
        if(rbtnCodigo.isSelected()){
            p.put("criterio", "id");
        }
        else if(rbtnNombre.isSelected()){
            p.put("criterio", "nombre");
        }else if(rbtnDni.isSelected()){
            p.put("criterio", "dni");
        }else{
            p.put("criterio", "");
        }
        JasperReport report;
        JasperPrint print;
        try{

            report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptEmpleado.jrxml");
            print=JasperFillManager.fillReport(report, p,connection);
            JasperViewer view=new JasperViewer(print,false);
            view.setTitle("Reporte de Clientes");
            view.setVisible(true);
        }catch(JRException e){
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtContraseniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseniaKeyTyped
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
    }//GEN-LAST:event_txtContraseniaKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox cboTipoUsuario;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JPanel pBuscar;
    private javax.swing.JPanel pNuevo;
    private javax.swing.JRadioButton rbtnActivo;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnFemenino;
    private javax.swing.JRadioButton rbtnInactivo;
    private javax.swing.JRadioButton rbtnMasculino;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JTabbedPane tabEmpleado;
    private javax.swing.JTable tblEmpleado;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextArea txtDireccion;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEmail;
    private com.toedter.calendar.JDateChooser txtFechaIng;
    private com.toedter.calendar.JDateChooser txtFechaNac;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSueldo;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}

