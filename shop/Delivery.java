package shop;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Delivery  extends Observable {

    private int idPers;
    private Map<Order, List<Product>> orders;
    private List<Client> clients;
    private List<Product> products;
    private List<Order> or;

    public Delivery(){
        this.clients = new DataAccessClient().findAllClient();
        Client.setId(clients.size());

        this.products = new DataAccessProduct().findAllProduct();
        Product.setId(products.size());

        orders = new HashMap<>();
        or = (new DataAccessOrder()).findAllOrder();
        Order.setId(or.size());
        System.out.println(or.get(0).getId());
        for(Order o : or){
           addOrder(o);
        }
        idPers = 0;
    }

    public void setIdPers ( int id){
        this.idPers = id;
    }
public int getIdPers (){
        return idPers;
    }

    public List<Product> getProducts () {
        return this.products;
    }
    public Map<Order, List<Product>> getOrders () {
        return this.orders;
    }

//________________________________________________________________________________________________________________________________
//============================================================================================================[  MANAGER  ]=======
//--------------------------------------------------------------------------------------------------------------------------------

    public void addOrder(Order o){
        int gasit = 0;
        for(Order o1 : orders.keySet()){
            if(o.getClientID() == o1.getClientID()){
                gasit = 1;
                for(Product p: products) {
                    if (p.getId() == o.getId()) {
                        Product p1 = p;
                        p1.setPrice(o.getPrice());
                        (orders.get(o1)).add(p1);
                        o1.setPrice(o1.getPrice()+p1.getPrice());
                    }
                }
            }
        }
        if(gasit == 0){
            orders.put(o, new ArrayList<>());
        }
        or.remove(o);
    }


    //---------------------------------------------------------[ MANAGE PRODUCTS ]----------------------------------------------------------

    public void addProd(String title, float rating, int price) {
        int s = products.size();
        products.add(new Product(title, rating, price));
        System.out.println("\nProdusul '" + title + "' a fost adaugat.\n");
        assert products.size() == s+1;
    }
    public void updateProd(String id, String title, String rating, String price) {
        for (Product p : products) {
            if (p.getId() == Integer.parseInt(id)) {
                if (title != null)
                    p.setName(title);
                if (rating != null)
                    p.setRating(Integer.parseInt(rating));
                if (price != null)
                    p.setPrice(Integer.parseInt(price));
                System.out.println("\nProdusul a fost modificat.\n");
                }
            }
    }

    public void delProd(String str) {
        int si = products.size();
        List<Product> b = products.stream().filter(s -> s.getName().equals(str)).collect(Collectors.toList());
        for (Product m : b) {
            products.remove(m);
            System.out.println("\nProdusul '" + m.getName() + "' a fost sters.\n");
        }
    }

    //---------------------------------------------------------[ GENERATE REPORTS ]----------------------------------------------------------
//    public List<Order> generateRapH(String h1, String h2) {           // Produsele comandate intre orele specificate
//        assert isWellFormed();
//        assert h1 != null;
//        assert h2 != null;
//        List<Order> o = new ArrayList<>();
//        for (Order or : orders.keySet()) {
//            String s = Character.toString(or.getDate().charAt(14)) + Character.toString(or.getDate().charAt(15));
//            if ((Integer.parseInt(s) >= Integer.parseInt(h1)) && (Integer.parseInt(s) <= Integer.parseInt(h2))) {
//                o.add(or);
//            }
//        }
//        return o;
//    }

//    public String generateRapVal(String nr, String val) {      // clientii care au comandat de mai mult de un numar specificat de ori produse cu o valoare mai mare decat cea specificata
//        String s = "";
//        for (ClientB c : clients) {
//            long n = (orders.keySet().stream().filter(x-> x.getPrice()>Integer.parseInt(val) && x.getClientId()==c.getId()).count());
//            if (orders.keySet().stream().filter(x-> x.getPrice()>Integer.parseInt(val) && x.getClientId()==c.getId()).count() > Integer.parseInt(nr)) {
//                s += "ID CLIENT: " + c.getId() + "\nNUMAR COMENZI: " + n + "\n\n";
//            }
//        }
//        return s;
//    }

//
//    public String generateRapNr(String nr) {           //Produse comandate de mai mult de un numar specificat de ori
//        assert nr!=null;
//        String s ="";
//        assert isWellFormed();
//        for (MenuItem mi : menu) {
//            for(List<MenuItem> mItems : orders.values()){
//                int n = (int) mItems.stream().filter(x->x.getTitle()==mi.getTitle()).count();
//                if (n > Integer.parseInt(nr)) {
//                    s+= mi.toString() + "\nNumar comenzi: " + n + "\n\n";
//                }
//            }
//        }
//        return s;
//    }
//    public List<Order> generateRapDay(String day) {        // comenzile plasate intr o anumita zi
//        List<Order> o = new ArrayList<>();
//        for (Order or : orders.keySet()) {
//            String s = Character.toString(or.getDate().charAt(8)) + Character.toString(or.getDate().charAt(9));
//            if (Integer.parseInt(s) == Integer.parseInt(day)) {
//                o.add(or);
//            }
//        }
//        return o;
//    }


//________________________________________________________________________________________________________________________________
//============================================================================================================[ CLIENT ]=========
//--------------------------------------------------------------------------------------------------------------------------------


    //---------------------------------------------------------[ REGISTER ]-----------------------------------------------------------
    public void register(String uname, String password, String address){
        Client cl = new Client(uname, password, address);
        clients.add(cl);
        new DataAccessClient().insertClient(cl);
    }

    public Client login(String uname, String password){
        for(Client c: clients)
            if(c.equals(new Client(uname, password, "")))
                return c;
        return null;
    }


    //---------------------------------------------------------[ SEARCH ]-----------------------------------------------------------
    public List<Product> searchTitle(String t) {
        List<Product> p = products.stream()
                .filter(x -> x.getName().contains(t))
                .collect(Collectors.toList());
        return p;
    }

    public List<Product> searchPrice(String pr) {
        return products.stream()
                .filter(x -> x.getPrice() == Integer.parseInt(pr))
                .collect(Collectors.toList());
    }

    public List<Product> searchRatingMin(String t) {
        List<Product> pr = products.stream()
                .filter(x -> x.getRating() >= Float.parseFloat(t))
                .collect(Collectors.toList());
        return pr;
    }

    //---------------------------------------------------------[ CREATE ORDER ]-----------------------------------------------------------
    public void createOrder(String clientId, String productId, String quantity) {
        int price = 0;
        for(Product p : products)
            if(p.getId() == Integer.parseInt(productId))
                price = p.getPrice();
        Order o = new Order(Integer.parseInt(clientId), Integer.parseInt(productId),  price);
        this.addOrder(o);
        or.add(o);
        (new DataAccessOrder()).insertOrder(o);
       // notifyUpdate(o);

//        for(Order ord:orders.keySet())
//            System.out.println(ord);
//
//        for (Order o1 : orders.keySet()) {
//            String s = or.toString();
//            for (Product m : orders.get(o1))
//                s = s + "|       -" + m.getTitle() + ";    PRET: " + m.getPrice() + "\n";
//            s += "|\n| TOTAL: " + o1.getPrice() + "\n|_______________________________________________________________________________\n\n\n\n";
//            fileWriter.writeText(s);
//        }
//        fileWriter.closeFW();
    }



    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, String> seen = new ConcurrentHashMap<>();
        return t -> seen.put(keyExtractor.apply(t), "") == null;
    }
}
