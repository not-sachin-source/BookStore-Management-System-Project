public class Vendor extends User {
    private String phone;
    private String city;

    public Vendor(String name, String loginId, String password, String phone, String city) {
        super(name, loginId, password);
        this.phone = phone;
        this.city = city;
    }

    public String getPhone() { return phone; }
    public String getCity() { return city; }

    @Override
    public String getRole() { return "Vendor"; }
}
