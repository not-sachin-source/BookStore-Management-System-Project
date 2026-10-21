public class Comedy extends Book {
    public Comedy(int id, String title, String author, double price, int quantity, Vendor vendor) {
        super(id, title, author, price, quantity, vendor);
    }

    @Override
    public String getCategory() { return "Comedy"; }

    @Override
    public double getDiscountPercent() { return 15; }

    @Override
    public String getNote() { return "Light and fun read"; }
}
