public class Seat {
    private String seatType; //Economy, Premium Economy, Business and First Class
    private String seatNumber;
    private boolean isAvailable;
    private double price;

    public Seat(String seatType, String seatNumber, boolean isAvailable, double price) {
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
        this.price = price;
    }


    public String getSeatType() {
        return seatType;
    }


    public String getSeatNumber() {
        return seatNumber;
    }


    public double getPrice() {
        return price;
    }


    public boolean isAvailable() {
        return isAvailable;
    }


    public void reserveSeat() {
        isAvailable = false;
    }


    public void cancelSeat() {
        isAvailable = true;
    }


}