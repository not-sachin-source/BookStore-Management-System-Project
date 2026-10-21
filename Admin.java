public class Admin extends User {
    public Admin(String name, String loginId, String password) {
        super(name, loginId, password);
    }

    @Override
    public String getRole() { return "Admin"; }
}
