package shop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;


@RestController
@RequestMapping("/products")
public class DataAccessProduct extends DataAccess<Product> {

//    public int setQuantity(int id, int cantitate){
//        String s = "UPDATE PRODUS SET cantitate = " + cantitate + " WHERE id = " + id;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        Connection connection = null;
//        try {
//            connection = DBconnection.getConnection();
//            statement = connection.prepareStatement(s);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            LOGGER.log(Level.WARNING, "UPDATE " + e.getMessage());
//            return 0;
//        } finally {
//            DBconnection.close(statement);
//            DBconnection.close(connection);
//        }
//        return 1;
//    }

@GetMapping(value = { "", "/" })
    public List<Product> findAllProduct() {
        List<Product> c = this.findAll();
        if (c == null) {
            throw new NoSuchElementException("Nu exista clienti in baza de date!\n");
        }
        return c;
    }
    public Product insertProd(Product  c) {
        Product  cl = this.insert(c);
        if (cl == null) {
            throw new NoSuchElementException("Nu s-a putut realiza inserarea!\n");
        }
        return cl;
    }

    public int deleteProd(int id) {
        int i = this.delete(id);
        if (i == 0) {
            throw new NoSuchElementException("Nu s-a putut realiza stergerea!\n");
        }
        return i;
    }

    public Product updateProd(Product  c) {
        Product  i = this.update(c);
        if (i == null) {
            throw new NoSuchElementException("Nu s-a putut realiza actualizarea!\n");
        }
        return i;
    }

    public JTable getJTable() {
        return new JTable(this.getData(), this.getCol().toArray());
    }
}
