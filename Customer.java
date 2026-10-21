public class Customer extends User {
    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public String getEmail() { return getLoginId(); }

    @Override
    public String getRole() { return "Customer"; }
}
