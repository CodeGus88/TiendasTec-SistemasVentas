
package Entidad.dtos.accountsreceivable;

import java.util.Date;


/**
 *
 * @author Gustavo Abasto Argote
 */
public class AccountsReceivableItem {
    
    private int id; // idCredito
    private String clientName;
    private String clientDni;
    private String clientRuc;
    private String employeeName;
    private float igv;
    private float subtotal;
    private float totalCredit;
    private float discount;
    private float total;
    private Date date;
    
    public AccountsReceivableItem(){
    
    }

    public AccountsReceivableItem(int id, String clientName, String clientDni, String clientRuc, String employeeName, float igv, float subtotal, float totalCredit, float discount, float total, Date date) {
        this.id = id;
        this.clientName = clientName;
        this.clientDni = clientDni;
        this.clientRuc = clientRuc;
        this.employeeName = employeeName;
        this.igv = igv;
        this.subtotal = subtotal;
        this.totalCredit = totalCredit;
        this.discount = discount;
        this.total = total;
        this.date = date;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int creditSaleId) {
        this.id = creditSaleId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientDni() {
        return clientDni;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public String getClientRuc() {
        return clientRuc;
    }

    public void setClientRuc(String clientRuc) {
        this.clientRuc = clientRuc;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public float getIgv() {
        return igv;
    }

    public void setIgv(float igv) {
        this.igv = igv;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(float totalCredit) {
        this.totalCredit = totalCredit;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String plaintContent(){
        return  id + " " +// idCredito
    clientName + " " +
    clientDni + " " +
    clientRuc + " " +
    employeeName + " " +
    igv + " " +
    subtotal + " " +
    totalCredit + " " +
    discount + " " +
    total + " " +
    date; 
    }
    
}
