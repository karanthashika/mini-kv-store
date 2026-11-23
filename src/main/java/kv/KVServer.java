package kv;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
public class KVServer {
    private final int port;
    private final Store store = new Store();
    private final ExecutorService pool = Executors.newFixedThreadPool(16);
    public KVServer(int port) { this.port = port; }
    public void start() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("KV server listening on port " + port);
            while (true) {
                Socket client = server.accept();
                pool.submit(() -> handle(client));
            }
        }
    }
    private void handle(Socket client) {
        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            String line;
            while ((line = in.readLine()) != null) {
                out.println(execute(line.trim()));
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
    private String execute(String line) {
        if (line.isEmpty()) return "ERR empty command";
        String[] parts = line.split("\\s+", 3);
        String cmd = parts[0].toUpperCase();
        switch (cmd) {
            case "SET":
                if (parts.length < 3) return "ERR usage: SET key value";
                store.set(parts[1], parts[2]);
                return "OK";
            case "GET":
                if (parts.length < 2) return "ERR usage: GET key";
                String v = store.get(parts[1]);
                return v == null ? "(nil)" : v;
            case "DELETE":
                if (parts.length < 2) return "ERR usage: DELETE key";
                return store.delete(parts[1]) ? "1" : "0";
            default:
                return "ERR unknown command";
        }
    }
}
