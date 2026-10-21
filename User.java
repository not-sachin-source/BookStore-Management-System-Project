public abstract class User {
    private String name;
    private String loginId;
    private String password;

    public User(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public String getName() { return name; }
    public String getLoginId() { return loginId; }

    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    public abstract String getRole();
}
