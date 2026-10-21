public abstract class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private int quantity;
    private Vendor vendor;

    public Book(int id, String title, String author, double price, int quantity, Vendor vendor) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.vendor = vendor;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Vendor getVendor() { return vendor; }

    public void reduceStock(int qty) { quantity = quantity - qty; }
    public void addStock(int qty) { quantity = quantity + qty; }

    public abstract String getCategory();
    public abstract double getDiscountPercent();
    public abstract String getNote();

    public double getFinalPrice() {
        return price - (price * getDiscountPercent() / 100);
    }
}
