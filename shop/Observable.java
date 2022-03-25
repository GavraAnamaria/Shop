package shop;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void delObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyUpdate(Order or) {
        for(Observer o: observers) {
            o.update(or);
        }
    }
}
