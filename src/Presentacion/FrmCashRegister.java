/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;


import Entidad.dtos.CashRegisterItem;
import Negocio.ClsCashRegister;
import interfaces.FrameState;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.Message;
import statics.Paths;
import statics.ScreenUses;
import statics.TableConfigurator;
import tools.ObjectDeserializer;
import tools.ObjectSerializer;

/**
 *
 * @author Edgar
 */
public class FrmCashRegister extends javax.swing.JInternalFrame implements FrameState {
    
    static ResultSet rs=null;
    DefaultTableModel dtmVentas, dtmCreditos;
    private Date fecha_ini;
    private final String titulos[] = {"CANTIDAD", "PRODUCTO", "PRECIO", "IMPORTE", "GANACIA", "FECHA"};
    private final int[] anchos = {40, 200, 40, 40, 40, 60};
    private ArrayList<CashRegisterItem> salesList, creditsList;
    
    public FrmCashRegister() {
        initComponents();
        readFrameRectanble();
        Date date=new Date();
        String format=new String("dd/MM/yyyy");
        SimpleDateFormat formato=new SimpleDateFormat(format);
        dcFecha.setDate(date);
        dtmVentas = new DefaultTableModel(null, titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dtmCreditos = new DefaultTableModel(null, titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        BuscarIngresosVentasYCreditos();
        CrearTablas(); 
        loadForm();
        
        design();
    }
    
    
    private void design(){
        jPanelBackground.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanelBackground.setBackground(Design.COLOR_PRIMARY_DARK);
    }
    
    private void CrearTablas(){
        Hashtable<Integer, Integer> hashtable = new Hashtable<>();
        hashtable.put(0, SwingConstants.CENTER);
        hashtable.put(2, SwingConstants.CENTER);
        hashtable.put(3, SwingConstants.CENTER);
        hashtable.put(4, SwingConstants.CENTER);
        for (int i=0;i<tblVenta.getColumnCount();i++){
            TableCellRenderer render = TableConfigurator.configureTableItem(hashtable);
            tblVenta.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        for (int i = 0; i < tblCredito.getColumnCount(); i++) {
            TableCellRenderer render = TableConfigurator.configureTableItem(hashtable);
            tblCredito.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        //Activar ScrollBar
//        tblVenta.setAutoResizeMode(tblVenta.AUTO_RESIZE_OFF);
        for(int i = 0; i < tblVenta.getColumnCount(); i++)
            tblVenta.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        for (int i = 0; i < tblCredito.getColumnCount(); i++)
            tblCredito.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
    }
    
    private void BuscarIngresosVentasYCreditos(){
        ClsCashRegister cashRegisterService = new ClsCashRegister();
        fecha_ini = dcFecha.getDate();
        salesList = cashRegisterService.listCashRegisterItem("venta", fecha_ini);
        creditsList = cashRegisterService.listCashRegisterItem("credito", fecha_ini);
        loadTableData(tblVenta, dtmVentas, salesList);
        loadTableData(tblCredito, dtmCreditos, creditsList);
    }
    
    private void loadTableData(JTable table, DefaultTableModel defaultTableModel, ArrayList<CashRegisterItem> list) {
        try {
            while(defaultTableModel.getRowCount() > 0)
                defaultTableModel.removeRow(0);
            String Datos[] = new String[6];
            //        "CANTIDAD 0", "PRODUCTO 1", "PRECIO 2", "IMPORTE 3", "GANACIA 4", "FECHA 5"
            for(CashRegisterItem item: list) {
                Datos[0] = String.valueOf(item.getAmount());
                Datos[1] = item.getName();
                Datos[2] = String.valueOf(item.getPrice());
                Datos[3] = String.valueOf(item.getTotal());
                Datos[4] = String.valueOf(item.getUtility());
                Datos[5] = String.valueOf(item.getDate());
                defaultTableModel.addRow(Datos);
            }
        } catch (Exception e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        table.setModel(defaultTableModel);
    }
    
    private void loadForm(){
        loadFormItems(txtIngresoVenta, txtCantidadProductoVenta, txtGananciaVenta, salesList);
        loadFormItems(txtIngresoCredito, txtCantidadProductoCredito, txtGananciaCredito, creditsList);
        try{
            txtTotal.setText(String.valueOf(
                    Double.parseDouble(txtIngresoVenta.getText()) + Double.parseDouble(txtIngresoCredito.getText())
                )
            );
        }catch(Exception e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
    
    private void loadFormItems(JTextField fieldTotal, JTextField fieldAmount, JTextField fieldUtility, ArrayList<CashRegisterItem> list){
        double total = 0;
        double amount = 0;
        double utility = 0;
        for(CashRegisterItem item: list){
            total += item.getTotal();
            amount += item.getAmount();
            utility += item.getUtility();
        }
        fieldTotal.setText(String.valueOf(total));
        fieldAmount.setText(String.valueOf(amount));
        fieldUtility.setText(String.valueOf(utility));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBackground = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtIngresoVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCantidadProductoVenta = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGananciaVenta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtIngresoCredito = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCantidadProductoCredito = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtGananciaCredito = new javax.swing.JTextField();
        jPanelCenter = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanelTables = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblVenta = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblCredito = new javax.swing.JTable();
        jPanelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dcFecha = new com.toedter.calendar.JDateChooser();
        jButtonCalculate = new javax.swing.JButton();
        jPanelFooter = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Resumen de caja");
        setMinimumSize(new java.awt.Dimension(225, 590));
        setPreferredSize(new java.awt.Dimension(225, 590));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanelBackground.setLayout(new java.awt.BorderLayout(5, 5));

        jPanelLeft.setOpaque(false);
        jPanelLeft.setPreferredSize(new java.awt.Dimension(200, 380));
        jPanelLeft.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle ventas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(160, 190));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(160, 190));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ingreso por ventas:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 180, 20));

        txtIngresoVenta.setEditable(false);
        txtIngresoVenta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtIngresoVenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIngresoVenta.setText("0.0");
        jPanel1.add(txtIngresoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 180, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cant. de Productos:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 180, -1));

        txtCantidadProductoVenta.setEditable(false);
        txtCantidadProductoVenta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCantidadProductoVenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProductoVenta.setText("0");
        jPanel1.add(txtCantidadProductoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 180, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Utilidad bruta:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 150, 20));

        txtGananciaVenta.setEditable(false);
        txtGananciaVenta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtGananciaVenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGananciaVenta.setText("0.0");
        jPanel1.add(txtGananciaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 180, 30));

        jPanelLeft.add(jPanel1);
        jPanel1.getAccessibleContext().setAccessibleName("Estadísticas de ventas del día");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle créditos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel2.setMinimumSize(new java.awt.Dimension(160, 190));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(160, 190));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Ingreso por ventas a crédito:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 180, 20));

        txtIngresoCredito.setEditable(false);
        txtIngresoCredito.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtIngresoCredito.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIngresoCredito.setText("0.0");
        jPanel2.add(txtIngresoCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 180, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cant. de Productos:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 180, -1));

        txtCantidadProductoCredito.setEditable(false);
        txtCantidadProductoCredito.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCantidadProductoCredito.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProductoCredito.setText("0");
        jPanel2.add(txtCantidadProductoCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 180, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Utilidad bruta:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 180, 20));

        txtGananciaCredito.setEditable(false);
        txtGananciaCredito.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtGananciaCredito.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGananciaCredito.setText("0.0");
        jPanel2.add(txtGananciaCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 180, 30));

        jPanelLeft.add(jPanel2);

        jPanelBackground.add(jPanelLeft, java.awt.BorderLayout.WEST);

        jPanelCenter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle de ingresos por productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanelCenter.setForeground(new java.awt.Color(255, 255, 255));
        jPanelCenter.setOpaque(false);
        jPanelCenter.setLayout(new java.awt.BorderLayout());

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Lista de ventas");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel9.setOpaque(true);
        jPanel4.add(jLabel9);

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Lista de créditos");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel10.setOpaque(true);
        jPanel4.add(jLabel10);

        jPanelCenter.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanelTables.setOpaque(false);
        jPanelTables.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        tblVenta.setRowHeight(22);
        tblVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVentaMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblVenta);

        jPanelTables.add(jScrollPane5);

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        tblCredito.setRowHeight(22);
        tblCredito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCreditoMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblCredito);

        jPanelTables.add(jScrollPane6);

        jPanelCenter.add(jPanelTables, java.awt.BorderLayout.CENTER);

        jPanelBackground.add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelHeader.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Fecha:");
        jPanelHeader.add(jLabel1);

        dcFecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dcFecha.setMinimumSize(new java.awt.Dimension(100, 30));
        dcFecha.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanelHeader.add(dcFecha);

        jButtonCalculate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/calcular_32.png"))); // NOI18N
        jButtonCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalculateActionPerformed(evt);
            }
        });
        jPanelHeader.add(jButtonCalculate);

        jPanelBackground.add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jPanelFooter.setOpaque(false);

        jPanel3.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("TOTAL EN CAJA");
        jLabel5.setPreferredSize(new java.awt.Dimension(98, 30));
        jPanel3.add(jLabel5);

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 237));
        txtTotal.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(0, 102, 204));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal.setText("0.0");
        txtTotal.setMinimumSize(new java.awt.Dimension(120, 40));
        txtTotal.setPreferredSize(new java.awt.Dimension(150, 40));
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        jPanel3.add(txtTotal);

        jPanelFooter.add(jPanel3);

        jPanelBackground.add(jPanelFooter, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelBackground);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalculateActionPerformed
        BuscarIngresosVentasYCreditos();
        CrearTablas();
        loadForm();
    }//GEN-LAST:event_jButtonCalculateActionPerformed

    private void tblVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVentaMouseClicked

    }//GEN-LAST:event_tblVentaMouseClicked

    private void tblCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCreditoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCreditoMouseClicked

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JButton jButtonCalculate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBackground;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelFooter;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelTables;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tblCredito;
    private javax.swing.JTable tblVenta;
    private javax.swing.JTextField txtCantidadProductoCredito;
    private javax.swing.JTextField txtCantidadProductoVenta;
    private javax.swing.JTextField txtGananciaCredito;
    private javax.swing.JTextField txtGananciaVenta;
    private javax.swing.JTextField txtIngresoCredito;
    private javax.swing.JTextField txtIngresoVenta;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    
    
    @Override
    public void readFrameRectanble() {
        ObjectDeserializer deserializer = new ObjectDeserializer(Paths.SERIAL_DIRECTORY_DATA, Paths.CASH_REGISTER);
        Rectangle rectangle = deserializer.deserialicer();
        if(rectangle != null){
            setBounds(rectangle);
        }else{
            setLocation(
                    (ScreenUses.getHorizontal() / 2) - (getWidth() / 2),
                    (ScreenUses.getVertical() / 2) - (getHeight() / 2)
            );
        }
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer serializer = new ObjectSerializer(Paths.SERIAL_DIRECTORY_DATA, Paths.CASH_REGISTER);
        serializer.serilizer(getBounds());
    }

    @Override
    public void dispose() {
        writeFrameRectangle();
        super.dispose();
    }
    
    
}
