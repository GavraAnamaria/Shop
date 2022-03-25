package shop;

import javax.swing.*;

public class Employee implements Observer {
    private int id;
    private String uname;
    private String psw;

    Employee(){
        super();
    }
    Employee(int id, String uname, String psw){
        this.id = id;
        this.uname = uname;
        this.psw = psw;
    }

    public String getUname(){
        return uname;
    }

    public String getPsw(){
        return psw;
    }

    public void setUname(String uname){
        this.uname = uname;
    }
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPsw(String psw){
        this.psw = psw;
    }

        @Override
        public void update(Order or){
            new DataAccessOrder().insertOrder(or);
        }


    }