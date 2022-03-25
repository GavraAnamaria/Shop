package shop;

import java.util.concurrent.atomic.AtomicInteger;

public class Product {
    private int id;
    private String name;
    private float rating;
    private int price;
    private static AtomicInteger nr = new AtomicInteger(0);

    public Product() {
        id = 0;
        name = "";
        rating = 0.0F;
        price = 0;
    }

    public Product(String name, float rating, int price) {
        nr.getAndIncrement();
        this.id = nr.get();
        this.name = name;
        this.rating = rating;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public static void setId(int id) {
        nr.getAndSet(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNr() {
        return 0;
    }

    public void setNr(int nr) {
    }

    @Override
    public String toString() {
        return "id=" + id + ", denumire = " + name + ", rating = " + rating + ", pret = " + price +"\n";
    }
}