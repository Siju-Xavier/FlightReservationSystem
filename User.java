import java.util.LinkedHashMap;
import java.util.Map;

public abstract class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected String role;

    public User(String username, String password, String email, String phoneNumber, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println(username + " logged out successfully.");
    }

    public Map<String, Object> getUserInfo() {
        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("username", username);
        userInfo.put("email", email);
        userInfo.put("phoneNumber", phoneNumber);
        userInfo.put("role", role);
        return userInfo;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getRole() {
        return role;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

}