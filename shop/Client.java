package shop;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private int id;
    private String uname;
    private String password;
    private String address;
    private static AtomicInteger nr = new AtomicInteger(0);

    public Client() {
        id = 0;
        uname=  "";
        password = "";
        address = "";
    }

    public Client( String uname, String password, String address){
        nr.getAndIncrement();
        this.id = nr.get();
        this.uname = uname;
        this.password = password;
        this.address = address;
    }

    public static void setId(int id) {
        nr.getAndSet(id);
    }
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object anotherObject){
        return (uname.equals(((Client)anotherObject).uname)) && (this.password.equals(((Client)anotherObject).password));
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String nume) {
        this.uname = nume;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNr() {
        return nr.get();
    }

    public void setNr(int nr) {}

    @Override
    public String toString() {
        return "Client id=" + id + ", uname = " + uname + ", address = " + address + "\n";
    }
}