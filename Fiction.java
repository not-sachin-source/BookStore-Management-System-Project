public class Fiction extends Book {
    public Fiction(int id, String title, String author, double price, int quantity, Vendor vendor) {
        super(id, title, author, price, quantity, vendor);
    }

    @Override
    public String getCategory() { return "Fiction"; }

    @Override
    public double getDiscountPercent() { return 5; }

    @Override
    public String getNote() { return "Imaginative stories"; }
}
