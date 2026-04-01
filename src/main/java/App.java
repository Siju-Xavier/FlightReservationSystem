import io.javalin.Javalin;

public class App {
    public static void main(String[] args){
        // Seed the database automatically on startup (uses INSERT IGNORE)
        DatabaseSeeder.main(null);

        //Initialize Javalin and start the server
        Javalin app = Javalin.create(config ->{
            config.enableCorsForAllOrigins();
            //Allows our Front-end to talk to this API
        }).start(8080);
        // 2. Initialize your existing FlightController
        FlightController flightController = new FlightController();

        // 3. Define a real endpoint to fetch flights from your MySQL database
        app.get("/api/flights", ctx -> {
            ctx.json(flightController.retrieveFlights());
        });

        // 4. Initialize the Login Controller
        LoginController loginController = new LoginController();

        // 5. Create a Login Endpoint for the React Frontend
        app.post("/api/login", ctx -> {
            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
            User user = loginController.verifyLogin(req.username, req.password);
            if (user != null) {
                ctx.json(new LoginResponse(user.getUsername(), user.getRole()));
            } else {
                ctx.status(401).result("Invalid credentials");
            }
        });

        app.get("/api/hello", ctx -> {
            ctx.result("Bellow");
        });
        System.out.println("Flight application is running on http://localhost:8080");
    }
}