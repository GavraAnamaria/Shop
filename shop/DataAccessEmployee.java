package shop;

import java.util.List;
import java.util.NoSuchElementException;

public class DataAccessEmployee extends DataAccess<Employee>{

    public List<Employee> findAllEmp() {
        List<Employee> e = this.findAll();
        return e;
    }

    public Employee insertEmp(Employee e) {
        Employee or = this.insert(e);
        if (or == null) {
            throw new NoSuchElementException("Nu s-a putut realiza inserarea!\n");
        }
        return or;
    }
    public int deleteEmp(int id) {
        int i = this.delete(id);
        if (i == 0) {
            throw new NoSuchElementException("Error!\n");
        }
        return i;
    }
}
