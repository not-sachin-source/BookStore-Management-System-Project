public class Crime extends Book {
    public Crime(int id, String title, String author, double price, int quantity, Vendor vendor) {
        super(id, title, author, price, quantity, vendor);
    }

    @Override
    public String getCategory() { return "Crime"; }

    @Override
    public double getDiscountPercent() { return 10; }

    @Override
    public String getNote() { return "Mystery and investigation"; }
}
