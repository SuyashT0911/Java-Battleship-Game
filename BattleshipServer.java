import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class BattleshipServer {
    private static final int GRID_SIZE = 4;
    private static final int NUM_SHIPS = 4;
    private static boolean[][] ships = new boolean[GRID_SIZE][GRID_SIZE];
    private static boolean[][] guessed = new boolean[GRID_SIZE][GRID_SIZE];
    private static int hits = 0;
    private static int turns = 0;

    public static void main(String[] args) throws IOException {
        String extPort = System.getenv("PORT");
        int port = (extPort != null && !extPort.isEmpty()) ? Integer.parseInt(extPort) : 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/", new StaticFileHandler("web/index.html", "text/html"));
        server.createContext("/style.css", new StaticFileHandler("web/style.css", "text/css"));
        server.createContext("/script.js", new StaticFileHandler("web/script.js", "application/javascript"));
        
        server.createContext("/api/start", new StartGameHandler());
        server.createContext("/api/guess", new GuessHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("=========================================");
        System.out.println(" Server running!");
        System.out.println(" Open http://localhost:" + port + " in your browser");
        System.out.println("=========================================");
        
        resetGame();
    }

    private static void resetGame() {
        hits = 0;
        turns = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                ships[i][j] = false;
                guessed[i][j] = false;
            }
        }
        Random rand = new Random();
        int shipsPlaced = 0;
        while (shipsPlaced < NUM_SHIPS) {
            int row = rand.nextInt(GRID_SIZE);
            int col = rand.nextInt(GRID_SIZE);
            if (!ships[row][col]) {
                ships[row][col] = true;
                shipsPlaced++;
            }
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private String path;
        private String mimeType;
        public StaticFileHandler(String path, String mimeType) {
            this.path = path;
            this.mimeType = mimeType;
        }
        @Override
        public void handle(HttpExchange t) throws IOException {
            Path file = Paths.get(path);
            if (!Files.exists(file)) {
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
            byte[] bytes = Files.readAllBytes(file);
            t.getResponseHeaders().add("Content-Type", mimeType);
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    static class StartGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                resetGame();
                String json = "{\"status\": \"started\"}";
                t.getResponseHeaders().add("Content-Type", "application/json");
                t.sendResponseHeaders(200, json.length());
                OutputStream os = t.getResponseBody();
                os.write(json.getBytes());
                os.close();
            }
        }
    }

    static class GuessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                InputStream is = t.getRequestBody();
                Scanner scanner = new Scanner(is).useDelimiter("\\A");
                String body = scanner.hasNext() ? scanner.next() : "";
                
                String[] parts = body.split(",");
                int row = Integer.parseInt(parts[0].trim());
                int col = Integer.parseInt(parts[1].trim());

                boolean hit = false;
                boolean alreadyGuessed = guessed[row][col];
                
                if (!alreadyGuessed) {
                    turns++;
                    guessed[row][col] = true;
                    if (ships[row][col]) {
                        hit = true;
                        hits++;
                    }
                }

                boolean gameOver = hits == NUM_SHIPS;

                String json = String.format("{\"hit\": %b, \"alreadyGuessed\": %b, \"turns\": %d, \"gameOver\": %b, \"totalHits\": %d}", 
                                             hit, alreadyGuessed, turns, gameOver, hits);

                t.getResponseHeaders().add("Content-Type", "application/json");
                t.sendResponseHeaders(200, json.length());
                OutputStream os = t.getResponseBody();
                os.write(json.getBytes());
                os.close();
            }
        }
    }
}
