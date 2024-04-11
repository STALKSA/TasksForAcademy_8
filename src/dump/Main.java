package dump;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Main {
        public static void main(String[] args) {
            Queue<String> queue = new LinkedList<>();
            Thread producer = new Thread(() -> {
                List<String> dump = Dump.getDump();
                for (String str : dump) {
                    queue.offer(str);
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread consumer = new Thread(() -> {
                while (true) {
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            String str = queue.poll();
                            Patient patient = new Patient(str);
                            System.out.println(patient);
                        }
                    }
                }
            });

            producer.start();
            consumer.start();
        }
    }
