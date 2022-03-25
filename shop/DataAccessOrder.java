package shop;
import java.util.List;
import java.util.NoSuchElementException;

public class DataAccessOrder  extends DataAccess<Order>{

    public List<Order> findAllOrder() {
        List<Order> c = this.findAll();
        if (c == null) {
            throw new NoSuchElementException("Nu exista comsnzi in baza de date!\n");
        }
        return c;
    }
    public Order insertOrder(Order o) {
        Order or = this.insert(o);
        if (or == null) {
            throw new NoSuchElementException("Nu s-a putut realiza inserarea!\n");
        }
        return or;
    }
    public int deleteOrder(int id) {
        if (this.delete(id) == 0) {
            throw new NoSuchElementException("Nu s-a putut realiza stergerea!\n");
        }
        return 1;
    }

    public void plasareComanda( Order o){
        Order cl = this.insertOrder(o);
        if (cl == null) {
            throw new NoSuchElementException("Nu s-a putut realiza comanda!\n");
        }
    }
}
