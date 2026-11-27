package kv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.function.Consumer;

public class WriteAheadLog {
    private final Path path;
    private final FileOutputStream out;

    public WriteAheadLog(String file) throws IOException {
        this.path = Paths.get(file);
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        this.out = new FileOutputStream(path.toFile(), true); // true = append
    }

    // Write one line to the log and force it onto the disk
    public synchronized void append(String line) throws IOException {
        out.write((line + "\n").getBytes(StandardCharsets.UTF_8));
        out.getFD().sync(); // fsync: really write it to disk
    }

    // Read the log from top to bottom, giving each line to the handler
    public void replay(Consumer<String> handler) throws IOException {
        if (!Files.exists(path)) return;
        try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.isBlank()) handler.accept(line);
            }
        }
    }
}