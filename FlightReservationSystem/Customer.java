import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String firstName;
    private String lastName;
    private List<String> bookingHistory;
    private boolean promotionNewsSubscriber;
    private boolean isGuestUser;

    public Customer(String username, String password, String email,
                    String phoneNumber, String firstName, String lastName) {
        super(username, password, email, phoneNumber, "Customer");
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookingHistory = new ArrayList<>();
        this.promotionNewsSubscriber = false;
        this.isGuestUser = false;
    }


    public void subscribe() {
        promotionNewsSubscriber = true;
        System.out.println("You have successfully subscribed to get news promotions");
    }

    public void unsubscribe() {
        promotionNewsSubscriber = false;
        System.out.println("You have successfully unsubscribed from  getting news promotions");
    }

    public void updateUserInfo(String username, String email, String phone, String password) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phone;
        this.password = password;
    }


    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public List<String> getBookingHistory()
    {
        return bookingHistory;
    }

    public boolean isPromotionNewsSubscriber()
    {
        return promotionNewsSubscriber;
    }

    public void setPromotionNewsSubscriber(boolean promotionNewsSubscriber)
    {
        this.promotionNewsSubscriber = promotionNewsSubscriber;

    }
    public boolean isGuestUser()
    {
        return isGuestUser;
    }
    public void setGuestUser(boolean guestUser)
    {
        isGuestUser = guestUser;
    }

}