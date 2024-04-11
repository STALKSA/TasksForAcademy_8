package dump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dump {
    static String file = "dump.txt";

    // Считывает строки из файла и возвращает список строк
    public static List<String> getDump() {
        try (Stream<String> strings = Files.lines(Paths.get(file))) {
            return strings.map(str -> str.replace("(", "").replace(")", "").trim()).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // Используем ConcurrentLinkedQueue для потокобезопасного управления очередью
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        // Флаг для завершения работы потоков
        boolean[] isProducerDone = {false};

        // Первый поток (Producer): читает строки из списка и добавляет их в очередь
        Thread producer = new Thread(() -> {
            List<String> dump = getDump();
            for (String str : dump) {
                queue.offer(str);
                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    System.err.println("Producer interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            isProducerDone[0] = true; // Отметим, что первый поток закончил свою работу
        });

        // Второй поток (Consumer): забирает строки из очереди и создает объекты Patient
        Thread consumer = new Thread(() -> {
            while (!isProducerDone[0] || !queue.isEmpty()) {
                String str = queue.poll();
                if (str != null) {
                    try {
                        Patient patient = new Patient(str);
                        System.out.println(patient);
                    } catch (Exception e) {
                        System.err.println("Ошибка при создании объекта Patient: " + e.getMessage());
                    }
                } else {
                    // Если очередь пуста, можно сделать небольшую задержку, чтобы снизить нагрузку на процессор
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        System.err.println("Consumer interrupted: " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        
        producer.start();
        consumer.start();

        try {
            // Дождемся завершения обоих потоков
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.err.println("Главный поток был прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
