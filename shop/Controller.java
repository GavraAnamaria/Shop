package shop;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


//@SpringBootApplication
//@RestController
//public class DemoApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(DemoApplication.class, args);
//        // DeliveryService d = new DeliveryService();
//        //Employee observer = new Employee(d);
//        //Client c = new Client(d);
//        //Administrator a = new Administrator(d);
//    }
//    @GetMapping("/hello")
//    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
//        return String.format("Hello %s!", name);
//    }



@RestController
public class Controller {
 private Delivery d = new Delivery();
 private List<Product> cart = new ArrayList<>();
//    @GetMapping("/setName")
//    public String setName(@RequestParam(value = "myName", defaultValue = "Ana") String name) {
//            this.m.setMessage(name);
//        return String.format("new name: %s", m.getMessage());
//    }

    @RequestMapping("/signUp")
    public void signUp(@RequestParam(value = "country" , defaultValue = "Romania") String country, @RequestParam(value = "city", defaultValue = "Babeni") String city, @RequestParam(value = "address", defaultValue = "nr. 217") String adr, @RequestParam(value = "uname2", defaultValue = "ana") String uname, @RequestParam( value = "psw2", defaultValue = "123") String psw) {
         String add = country + ", " + city + ", " + adr;
         d.register(uname, psw, add);
    }


    @RequestMapping("/addEmp")
    public String addEmp(@RequestParam(value = "uname" , defaultValue = "ana1") String uname, @RequestParam(value = "psw", defaultValue = "123") String psw){
        if((new DataAccessEmployee()).insertEmp(new Employee(12, uname, psw)) == null)
            return "Acest angajat exista deja in baza de date!";
        return "Angajat adaugat cu succes!";
    }

    @RequestMapping("/delEmp")
    public String delEmp(@RequestParam(value = "idd" , defaultValue = "1") int id){
        if((new DataAccessEmployee()).deleteEmp(id) == 0)
            return "Nu exista niciun produs cu acest id in baza de date";
        return "Produs sters cu success!";
    }

    @RequestMapping("/addProd")
    public String addProd(@RequestParam(value = "name" , defaultValue = "Bluza") String name, @RequestParam(value = "rating", defaultValue = "0.0") float rating, @RequestParam(value = "price", defaultValue = "50") int price){
        if((new DataAccessProduct()).insertProd(new Product(name, rating, price)) == null)
            throw new NoSuchElementException("Acest produs exista deja in baza de date!\n");
        return "Produs adaugat cu success!";
    }

    @RequestMapping("/delProd")
    public String delProd(@RequestParam(value = "idd1" , defaultValue = "1") int id){
        if((new DataAccessProduct()).deleteProd(id) == 0)
            return "Acest produs nu exista!";
        return "Produsul a fost eliminat din baza de date!";
    }

    @RequestMapping ("/prod")
    public List<Product> produse(Model model) {
        List<Product> products = d.getProducts();
        model.addAttribute("products", products);
        return products;
    }
    @RequestMapping ("/cart")
    public List<Product> getCart(Model model) {
        model.addAttribute("cart", cart);
        return cart;
    }


    @RequestMapping("/addToCart")
    public void addToCart(@RequestParam(value = "name" , defaultValue = "Bluza") String name, @RequestParam(value = "rating", defaultValue = "0.0") float rating, @RequestParam(value = "price", defaultValue = "50") int price){
        cart.add(new Product(name, rating, price));
    }

    @GetMapping("/newOrder")
    public void newOrder(){
        for(Product p : cart){
            Order o = new Order(d.getIdPers(), p.getId(), p.getPrice());
            (new DataAccessOrder()).plasareComanda(o);
            (new Employee()).update(o);
        }
    }
    @GetMapping("/delOrder")
    public void delOrder(@RequestParam(value = "id" , defaultValue = "1") int id){
        d.getOrders().remove(id);
        }

}