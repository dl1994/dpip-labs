import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class task5 {

    public static void main(String[] args) throws Exception {
        System.out.println("Input numbers: ");

        INumberSource keyboardSource = new KeyboardNumberSource();
        NumberSequence numberSequence1 = new NumberSequence(keyboardSource);

        numberSequence1.addActionListener(new SumPrinterAction());
        numberSequence1.addActionListener(new AveragePrinterAction());
        numberSequence1.addActionListener(new MediamPrinterAction());
        numberSequence1.start();

        String inputFile;
        String outputFile;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Input file: ");
            inputFile = reader.readLine().trim();
            System.out.print("Output file: ");
            outputFile = reader.readLine().trim();
        }

        try (INumberSource fileSource = new FileNumberSource(Paths.get(inputFile))) {
            NumberSequence numberSequence2 = new NumberSequence(fileSource);

            numberSequence2.addActionListener(new SumPrinterAction());
            numberSequence2.addActionListener(new TextFileWriterAction(Paths.get(outputFile)));
            numberSequence2.start();
        }
    }

    public static class NumberSequence {

        private final INumberSource source;
        private final List<Integer> numbers = new LinkedList<>();
        private final List<Consumer<List<Integer>>> actionListeners = new ArrayList<>();

        public NumberSequence(INumberSource source) {
            this.source = source;
        }

        public void start() throws InterruptedException, IOException {
            while (true) {
                Integer currentNumber = this.source.readNumber();
                Thread.sleep(1000L);

                if (currentNumber > -1) {
                    this.updateCollection(currentNumber);
                } else {
                    break;
                }
            }
        }

        public void addActionListener(Consumer<List<Integer>> listener) {
            this.actionListeners.add(listener);
        }

        private void updateCollection(Integer number) {
            this.numbers.add(number);
            this.actionListeners.forEach(l -> l.accept(this.numbers));
        }
    }

    public interface INumberSource extends AutoCloseable {
        Integer readNumber() throws IOException;
    }

    public abstract static class AbstractBufferedReaderNumberSource implements INumberSource {

        protected final BufferedReader reader;

        protected AbstractBufferedReaderNumberSource(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public Integer readNumber() throws IOException {
            return Optional.ofNullable(this.reader.readLine())
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .orElse(-1);
        }

        @Override
        public void close() throws Exception {
            this.reader.close();
        }
    }

    public static class KeyboardNumberSource extends AbstractBufferedReaderNumberSource {

        public KeyboardNumberSource() {
            super(new BufferedReader(new InputStreamReader(System.in)));
        }
    }

    public static class FileNumberSource extends AbstractBufferedReaderNumberSource {

        public FileNumberSource(Path path) throws IOException {
            super(Files.newBufferedReader(path));
        }
    }

    public static class TextFileWriterAction implements Consumer<List<Integer>> {

        private final Path path;
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        public TextFileWriterAction(Path path) {
            this.path = path;
        }

        @Override
        public void accept(List<Integer> numbers) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            String line = zonedDateTime.format(FORMATTER) + ' ' + numbers + '\n';

            try {
                Files.write(this.path, line.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class SumPrinterAction implements Consumer<List<Integer>> {

        @Override
        public void accept(List<Integer> numbers) {
            int sum = numbers.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println("Sum = " + sum);
        }
    }

    public static class AveragePrinterAction implements Consumer<List<Integer>> {

        @Override
        public void accept(List<Integer> numbers) {
            double average = numbers.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
            System.out.println("Average = " + average);
        }
    }

    public static class MediamPrinterAction implements Consumer<List<Integer>> {

        @Override
        public void accept(List<Integer> numbers) {
            int[] array = numbers.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            Arrays.sort(array);
            System.out.println("Median = " + getMedian(array));
        }

        private double getMedian(int[] array) {
            if (array.length % 2 == 0) {
                int middle = array.length / 2;
                return (array[middle - 1] + array[middle]) / 2.0;
            } else if (array.length == 0) {
                return 0.0;
            } else {
                return array[(array.length - 1) / 2];
            }
        }
    }
}
