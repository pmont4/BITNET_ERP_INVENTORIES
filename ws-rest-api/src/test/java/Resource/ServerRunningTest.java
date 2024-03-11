package Resource;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import RestAPI.Main;

public class ServerRunningTest {

    private HttpServer server;

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void isUp() throws Exception {
        server = Main.startServer();
        if (server.isStarted()) {
            System.out.println("Server is up!");
        }
    }

}
