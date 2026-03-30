import io.javalin.Javalin;

public class App {
    public static void main(String[] args){
        //Initialize Javalin and start the server
        Javalin app = Javalin.create(config ->{
            config.enableCorsForAllOrigins();
            //Allows our Front-end to talk to this API
        }).start(8080);
        app.get("/api/hello", ctx -> {
            ctx.result("Bellow");
        });
        System.out.println("Flight application is running");
    }
}