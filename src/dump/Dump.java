package dump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dump {
    static String file = "dump.txt";
    public static List<String> getDump() {
        try(Stream<String> strings = Files.lines(Paths.get(file))) {
            return strings.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
