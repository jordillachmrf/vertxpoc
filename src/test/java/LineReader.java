import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LineReader {

    public static void main(String[] args) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("/Users/jordillach/Documents/test.txt"))) {
            stream.map(String::trim).forEach(System.out::println);
        }
    }
}
