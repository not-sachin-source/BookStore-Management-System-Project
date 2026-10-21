public class Horror extends Book {
    public Horror(int id, String title, String author, double price, int quantity, Vendor vendor) {
        super(id, title, author, price, quantity, vendor);
    }

    @Override
    public String getCategory() { return "Horror"; }

    @Override
    public double getDiscountPercent() { return 0; }

    @Override
    public String getNote() { return "Not for young readers (16+)"; }
}
