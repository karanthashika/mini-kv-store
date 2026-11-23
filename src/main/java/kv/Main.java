package kv;
public class Main {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 6380;
        new KVServer(port).start();
    }
}
