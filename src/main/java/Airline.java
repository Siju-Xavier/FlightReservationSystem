import java.util.Map;

public class Airline {
    private String name;
    private String airlineID;
    private Map<String, String> loyaltyPointRules;
    private Map<String, String> baggageRules;
    private Map<String, String> policies;

    public Airline(String name, String airlineID, Map<String, String> loyaltyPointRules, Map<String, String> baggageRules, Map<String, String> policies) {
        this.name = name;
        this.airlineID = airlineID;
        this.loyaltyPointRules = loyaltyPointRules;
        this.baggageRules = baggageRules;
        this.policies = policies;
    }

    public String getName() {
        return name;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public Map<String, String> getLoyaltyPointRules() {
        return loyaltyPointRules;
    }

    public Map<String, String> getBaggageRules() {
        return baggageRules;
    }

    public Map<String, String> getPolicies() {
        return policies;
    }

    public void setLoyaltyPointRules(Map<String, String> loyaltyPointRules) {
        this.loyaltyPointRules = loyaltyPointRules;
    }

    public void setBaggageRules(Map<String, String> baggageRules) {
        this.baggageRules = baggageRules;
    }

    public void setPolicies(Map<String, String> policies) {
        this.policies = policies;
    }
}