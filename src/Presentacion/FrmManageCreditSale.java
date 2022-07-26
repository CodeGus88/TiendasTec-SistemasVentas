
package Presentacion;

import Entidad.ClsEntidadCliente;
import Entidad.ClsEntidadCredito;
import Entidad.ClsEntidadEmpleado;
import Entidad.dtos.accountsreceivable.ProductDetailItem;
import Negocio.ClsCliente;
import Negocio.ClsCredito;
import Negocio.ClsDetalleCredito;
import Negocio.ClsEmpleado;
import interfaces.TableUpdatable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.ScreenUses;
import statics.TableConfigurator;
import tools.toast.Toast;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class FrmManageCreditSale extends javax.swing.JInternalFrame {

    /**
     * Creates new form FrmManageCreditSale
     */
    
    private final TableUpdatable tableUpdatable;
    private final String titles[] = {"ID", "CÓDIGO", "PRODUCTO", "DESCRIPCIÓN", "CANTIDAD", "PRECIO", "TOTAL"};
    private final int[] widths = {20, 40, 200, 200, 40, 40, 40};
    private  DefaultTableModel defaultTableModel;
    private int creditId;
    
    public FrmManageCreditSale(int creditId, TableUpdatable tableUpdatable) {
        initComponents();
        this.tableUpdatable = tableUpdatable;
        this.creditId = creditId;
        design();
        setLocation(
                (ScreenUses.getHorizontal() / 2) - (getWidth() / 2),
                (ScreenUses.getVertical() / 2) - (getHeight() / 2)
        );
        tableConfigurator();
        loadData(creditId);
    }
    
    private void loadData(int creditId){
        ClsCredito creditoService = new ClsCredito();
        ClsEntidadCredito credito = creditoService.findById(creditId);
        if(credito != null){
            fillCreditData(credito);
            ClsCliente cliente = new ClsCliente();
            fillClientData(cliente.findById(Integer.parseInt(credito.getStrIdCliente())));
            ClsDetalleCredito detalleCreditoService = new ClsDetalleCredito();
            loadDetailList(detalleCreditoService.findByCreditId(
                        Integer.parseInt(credito.getStrIdCredito())
                    )
            );
            ClsEmpleado employee = new ClsEmpleado();
            fillEmployeeData(
                    employee.findById(Integer.parseInt(credito.getStrIdEmpleado()))
            );
        }
    }
    
    private void fillCreditData(ClsEntidadCredito credito){
        jLabelCreditId.setText(credito.getStrIdCredito());
        jLabelSerie.setText(credito.getStrSerieCredito());
        jLabelNumber.setText(credito.getStrNumeroCredito());
        jLabelState.setText(credito.getStrEstadoCredito());
        // 
        jLabelIgv.setText(credito.getStrIgvCredito());
        jLabelSubtotal.setText(credito.getStrSubTotalCredito());
        jLabelTotalCredit.setText(credito.getStrTotalCredito());
        jLabelDescount.setText(credito.getStrDescuentoCredito());
        jLabelTotal.setText(credito.getStrTotalPagarCredito());
        jLabelDate.setText(credito.getStrFechaCredito().toString());
    }
    
    private void fillClientData(ClsEntidadCliente client) {
        jLabelClientId.setText(client.getStrIdCliente());
        jLabelName.setText(client.getStrNombreCliente());
        jLabelPhone.setText(client.getStrTelefonoCliente());
        jLabelNit.setText(client.getStrDniCliente().isEmpty()?client.getStrRucCliente() + " (NIT)":client.getStrDniCliente()+" (DNI)");
        jLabelAddress.setText(client.getStrDireccionCliente());
        jTextAreaObservation.setText(client.getStrObsvCliente());
    }
    
    private void fillEmployeeData(ClsEntidadEmpleado employee){
        jLabelEmployee.setText("Empleado: " + employee.getStrNombreEmpleado() + " " + employee.getStrApellidoEmpleado());
    }
    
    private void tableConfigurator() {
        defaultTableModel = new DefaultTableModel(null, titles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableDetails.setModel(defaultTableModel);
        for (int i = 0; i < jTableDetails.getColumnCount(); i++) {
            jTableDetails.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
//        TableConfigurator.getTableHeaderConfigurator(jTableDetails);
    }
    
    private void design(){
        jPanelBackground.setBackground(Design.COLOR_PRIMARY_DARK);
        btnPay.setBorder(Design.BORDER_BUTTON);
        btnPay.setBackground(Design.COLOR_ACCENT);
        jPanelBackground.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private void loadDetailList(ArrayList<ProductDetailItem> list) {
        while (defaultTableModel.getRowCount() > 0) {
            defaultTableModel.removeRow(0);
        }
        String fila[] = new String[9];
        for (ProductDetailItem item : list) {
            fila[0] = String.valueOf(item.getProductId());
            fila[1] = item.getProductCode();
            fila[2] = item.getProductName();
            fila[3] = item.getProductDescription();
            fila[4] = String.valueOf(item.getAmount());
            fila[5] = String.valueOf(item.getUnitPrice());
            fila[6] = String.valueOf(item.getTotal());
            defaultTableModel.addRow(fila);
        }
        for (int i = 0; i < jTableDetails.getColumnCount(); i++) {
            Hashtable<Integer, Integer> map = new Hashtable<Integer, Integer>();
            map.put(0, SwingConstants.CENTER);
            map.put(1, SwingConstants.RIGHT);
            map.put(4, SwingConstants.CENTER);
            map.put(5, SwingConstants.CENTER);
            map.put(6, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(map);
            jTableDetails.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBackground = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabelEmployee = new javax.swing.JLabel();
        jPanelHead = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObservation = new javax.swing.JTextArea();
        jLabelClientId = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        jLabelNit = new javax.swing.JLabel();
        jLabelPhone = new javax.swing.JLabel();
        jLabelAddress = new javax.swing.JLabel();
        jPanelBody = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetails = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnPay = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabelCreditId = new javax.swing.JLabel();
        jLabelSerie = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabelNumber = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabelState = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabelIgv = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelSubtotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelDescount = new javax.swing.JLabel();
        jLabelTotalCredit = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Crédito");
        setMinimumSize(new java.awt.Dimension(125, 150));
        setPreferredSize(new java.awt.Dimension(720, 478));
        setVisible(true);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanelBackground.setLayout(new java.awt.BorderLayout());

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jLabelEmployee.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jLabelEmployee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEmployee.setText("undefined");
        jPanel3.add(jLabelEmployee);

        jPanelBackground.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanelHead.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanelHead.setForeground(new java.awt.Color(255, 255, 255));
        jPanelHead.setMaximumSize(new java.awt.Dimension(32767, 170));
        jPanelHead.setMinimumSize(new java.awt.Dimension(50, 155));
        jPanelHead.setOpaque(false);
        jPanelHead.setPreferredSize(new java.awt.Dimension(989, 150));
        jPanelHead.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID:");
        jLabel1.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabel1);
        jLabel1.setBounds(17, 17, 204, 17);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("NOMBRE O RAZÓN SOCIAL:");
        jLabel4.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabel4.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabel4);
        jLabel4.setBounds(17, 40, 204, 17);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("DNI / NIT:");
        jPanelHead.add(jLabel5);
        jLabel5.setBounds(326, 17, 83, 17);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("TEFÉFONO:");
        jLabel6.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabel6.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabel6);
        jLabel6.setBounds(17, 63, 204, 17);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("DIRECCIÓN:");
        jPanelHead.add(jLabel7);
        jLabel7.setBounds(316, 63, 95, 17);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("OBSERVACIONES:");
        jLabel8.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabel8.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabel8);
        jLabel8.setBounds(17, 86, 204, 17);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(0, 0));

        jTextAreaObservation.setEditable(false);
        jTextAreaObservation.setColumns(20);
        jTextAreaObservation.setRows(5);
        jTextAreaObservation.setText("Description");
        jTextAreaObservation.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane2.setViewportView(jTextAreaObservation);

        jPanelHead.add(jScrollPane2);
        jScrollPane2.setBounds(233, 86, 384, 57);

        jLabelClientId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelClientId.setForeground(new java.awt.Color(255, 255, 255));
        jLabelClientId.setText("0");
        jLabelClientId.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabelClientId.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabelClientId);
        jLabelClientId.setBounds(233, 17, 87, 17);

        jLabelName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelName.setText("name");
        jLabelName.setMaximumSize(new java.awt.Dimension(1000000, 1000000));
        jLabelName.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabelName);
        jLabelName.setBounds(233, 40, 370, 17);

        jLabelNit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelNit.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNit.setText("CI-NIT");
        jLabelNit.setMinimumSize(new java.awt.Dimension(39, 0));
        jPanelHead.add(jLabelNit);
        jLabelNit.setBounds(413, 17, 210, 17);

        jLabelPhone.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelPhone.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPhone.setText("79382914");
        jLabelPhone.setMaximumSize(new java.awt.Dimension(500, 1000000));
        jLabelPhone.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelHead.add(jLabelPhone);
        jLabelPhone.setBounds(233, 63, 90, 17);

        jLabelAddress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelAddress.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAddress.setText("Address");
        jLabelAddress.setMaximumSize(new java.awt.Dimension(20000, 17));
        jLabelAddress.setMinimumSize(new java.awt.Dimension(20000, 17));
        jLabelAddress.setPreferredSize(new java.awt.Dimension(20000, 17));
        jPanelHead.add(jLabelAddress);
        jLabelAddress.setBounds(417, 63, 700, 17);

        jPanelBackground.add(jPanelHead, java.awt.BorderLayout.PAGE_START);

        jPanelBody.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del crédito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanelBody.setOpaque(false);
        jPanelBody.setLayout(new java.awt.BorderLayout(5, 5));

        jTableDetails.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableDetails);

        jPanelBody.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setMinimumSize(new java.awt.Dimension(39, 110));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(755, 110));
        jPanel2.setLayout(new java.awt.BorderLayout());

        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("PAGAR AHORA");
        btnPay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnPay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPay.setSelected(true);
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        jPanel2.add(btnPay, java.awt.BorderLayout.EAST);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 90));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(716, 90));
        jPanel1.setLayout(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel4.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("ID: ");

        jLabelCreditId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelCreditId.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCreditId.setText("0");

        jLabelSerie.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelSerie.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerie.setText("0");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("NÚMERO:");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("SERIE: ");

        jLabelNumber.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelNumber.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNumber.setText("0");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("ESTADO: ");

        jLabelState.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelState.setForeground(new java.awt.Color(255, 255, 255));
        jLabelState.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCreditId, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSerie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelCreditId))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabelSerie))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabelNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabelState))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(10, 0, 170, 110);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel5.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("IGV:");

        jLabelIgv.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelIgv.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIgv.setText("0");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("SUB TOTAL:");

        jLabelSubtotal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelSubtotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSubtotal.setText("0");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("TOTAL:");

        jLabelTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal.setText("0");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("DESCUENTO:");

        jLabelDescount.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelDescount.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDescount.setText("0");

        jLabelTotalCredit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelTotalCredit.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotalCredit.setText("0");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("TOTAL CRÉDITO:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("FECHA DE EMICIÓN:");

        jLabelDate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDate.setText("00-00-0000");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabelIgv, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(jLabelDescount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 19, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabelIgv)
                    .addComponent(jLabel11)
                    .addComponent(jLabelSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelTotalCredit)
                    .addComponent(jLabel12)
                    .addComponent(jLabelDescount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabelTotal))
                .addGap(3, 3, 3)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabelDate))
                .addGap(6, 6, 6))
        );

        jPanel1.add(jPanel5);
        jPanel5.setBounds(190, 0, 350, 110);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanelBody.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanelBackground.add(jPanelBody, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelBackground);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        int confirm = JOptionPane.showConfirmDialog(null, 
                "\n¿Realmente desea pagar este crédito?\n\nID CRÉDITO: " 
                        + jLabelCreditId.getText() 
                         + "\nCLIENTE: "+ jLabelName.getText() 
                        + "\nMONTO TOTAL: " + jLabelTotal.getText(), 
                "Confirmar pago", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
        if(confirm == JOptionPane.YES_NO_OPTION){
            ClsCredito creditService = new ClsCredito();
            if (creditService.payCredit(creditId, false, Date.valueOf(LocalDate.now()))) {
                tableUpdatable.updateTable();
                Toast.makeText(Toast.SUCCESS, "Se pagó con éxito", Toast.LENGTH_SHORT).show();
                this.dispose();
            } else {
                Toast.makeText(Toast.DANGER, "Ocurrió un error al intentar pagar", Toast.LENGTH_SHORT).show();
            }
        }
    }//GEN-LAST:event_btnPayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAddress;
    private javax.swing.JLabel jLabelClientId;
    private javax.swing.JLabel jLabelCreditId;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDescount;
    private javax.swing.JLabel jLabelEmployee;
    private javax.swing.JLabel jLabelIgv;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelNit;
    private javax.swing.JLabel jLabelNumber;
    private javax.swing.JLabel jLabelPhone;
    private javax.swing.JLabel jLabelSerie;
    private javax.swing.JLabel jLabelState;
    private javax.swing.JLabel jLabelSubtotal;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotalCredit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelBackground;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JPanel jPanelHead;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDetails;
    private javax.swing.JTextArea jTextAreaObservation;
    // End of variables declaration//GEN-END:variables
}
