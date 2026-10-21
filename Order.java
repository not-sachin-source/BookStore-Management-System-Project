public class Order {
    private int id;
    private Customer customer;
    private Book book;
    private int quantity;
    private double total;

    public Order(int id, Customer customer, Book book, int quantity, double total) {
        this.id = id;
        this.customer = customer;
        this.book = book;
        this.quantity = quantity;
        this.total = total;
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
}
