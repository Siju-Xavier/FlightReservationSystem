import io.javalin.Javalin;

public class App {
    public static void main(String[] args){
        //Initialize Javalin and start the server
        Javalin app = Javalin.create(config ->{
            config.enableCorsForAllOrigins();
            //Allows our Front-end to talk to this API
        }).start(8080);
        // 2. Initialize your existing FlightController
        FlightController flightController = new FlightController();

        // 3. Define a real endpoint to fetch flights from your MySQL database
        app.get("/api/flights", ctx -> {
            ctx.json(flightController.getFlights());
        });

        app.get("/api/hello", ctx -> {
            ctx.result("Bellow");
        });
        System.out.println("Flight application is running on http://localhost:8080");
    }
}