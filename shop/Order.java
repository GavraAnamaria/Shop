package shop;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Order{
    private int id;
    private String date;
    private int clientID;
    private int productID;
    private int price;
    private static AtomicInteger nr = new AtomicInteger(0);

    public Order(){
        id = 0;
        date = "";
        clientID = 0;
        productID = 0;
        price = 0;
    }


    public Order(int clientId, int productId, int price) {
        nr.getAndIncrement();
        this.id = nr.get();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'la' HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        this.clientID = clientId;
        this.date = formatter.format(d);
        this.productID = productId;
        this.price = price;
    }



    public static void setId(int id) {
        nr.getAndSet(id);
    }

    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return price;
    }

    public int getId() {
        return productID;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    public int getClientID() {
        return this.clientID;
    }
    public int getProductID() {
        return this.productID;
    }

    public void setProductID(int prod) {
        this.productID = prod;
    }
    public void setClientID(int cl) {
        this.clientID = cl;
    }

    public int getNr() {
        return 0;
    }

    public void setNr(int nr) {
    }

    public String toString1(){
        return "ID comanda: " + id +" ID Client: " + clientID + " Data: " + date + " Valoare: "+ price;
    }
}
