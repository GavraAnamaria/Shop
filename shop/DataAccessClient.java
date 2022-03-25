package shop;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;

public class DataAccessClient extends DataAccess<Client>{

    public List<Client> findAllClient() {
        List<Client> c = this.findAll();
        if (c == null) {
            throw new NoSuchElementException("Nu exista clienti in baza de date!\n");
        }
        return c;
    }

    public String  insertClient(Client c) {
        List<Client> clients = this.findAllClient();
        for(Client cl : clients)
            if (cl.getUname() == c.getUname()) {
                return "An account with this username already exists!";
            }
        if ( this.insert(c) == null) {
            return "Error!";
        }
        return "Success!";
    }
    public void deleteClient(int id) {
        int i = this.delete(id);
        if (i == 0) {
            throw new NoSuchElementException("Nu s-a putut realiza stergerea!\n");
        }
    }

    public void updateClient(Client c) {
        Client i = this.update(c);
        if (i == null) {
            throw new NoSuchElementException("Nu s-a putut realiza actualizarea!\n");
        }
    }

    public JTable getJTable() {
        return new JTable(this.getData(), this.getCol().toArray());
    }
}

