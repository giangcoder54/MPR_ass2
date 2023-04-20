package hanu.a2_2001040056.models;

public class Product {
    private long id ;

    private String thumbnail;
    private String name;
    private String category;
    private long unitPrice;

    private int quantity ;

    public Product(long id, String thumbnail, String name, String category, long unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }
    public Product(long id,String thumbnail, String name , long unitPrice, int quantity){
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name ;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public long getSumPrice(){
        return unitPrice*quantity;
    }

    public long getId() {
        return id;
    }
}
