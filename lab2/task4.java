import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class task4 {

    public static void main(String[] args) {
        System.out.println("Using sequential with closest percentiles:\n");

        INumberGenerator sequential = new SequentialNumberGenerator(1, 50, 9);
        IPercentileFinder closest = new ClosestPercentileFinder();
        DistributionTester tester1 = new DistributionTester(sequential, closest);

        tester1.printPercentiles();

        System.out.println("\nUsing random with closest percentiles:\n");

        INumberGenerator random = new RandomNumberGenerator(50, 100, 10);
        DistributionTester tester2 = new DistributionTester(random, closest);

        tester2.printPercentiles();

        System.out.println("\nUsing fibonacci with interpolated percentiles:\n");

        INumberGenerator fibonacci = new FibonacciNumberGenerator(15);
        IPercentileFinder interpoalted = new InterpolatedPercentileFinder();
        DistributionTester tester3 = new DistributionTester(fibonacci, interpoalted);

        tester3.printPercentiles();
    }

    public static class DistributionTester {

        private final INumberGenerator numberGenerator;
        private final IPercentileFinder percentileFinder;

        public DistributionTester(INumberGenerator numberGenerator, IPercentileFinder percentileFinder) {
            this.numberGenerator = numberGenerator;
            this.percentileFinder = percentileFinder;
        }

        public void printPercentiles() {
            int[] elements = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(this.numberGenerator, Spliterator.ORDERED), false
            ).mapToInt(Integer::intValue)
                    .toArray();
                    
            Arrays.sort(elements);
            System.out.println("Numbers: " + Arrays.toString(elements));

            for (int i = 10; i <= 90; i += 10) {
                System.out.println(i + "th percentile: " + this.percentileFinder.findPercentile(elements, i));
            }
        }
    }

    public interface INumberGenerator extends Iterator<Integer> {}

    public static class SequentialNumberGenerator implements INumberGenerator {

        private int current;
        private final int step;
        private final int upperLimit;

        public SequentialNumberGenerator(int lowerLimit, int upperLimit, int step) {
            this.step = step;
            this.current = lowerLimit;
            this.upperLimit = upperLimit;
        }

        @Override
        public Integer next() {
            int next = this.current;
            this.current += this.step;
            return next;
        }

        @Override
        public boolean hasNext() {
            return this.current <= this.upperLimit;
        }
    }

    public static class RandomNumberGenerator implements INumberGenerator {

        private int index;
        private final int maxIndex;
        private final double mean;
        private final double variance;
        private final Random random = new Random();

        public RandomNumberGenerator(double mean, double variance, int numElements) {
            this.maxIndex = numElements;
            this.mean = mean;
            this.variance = variance;
        }

        @Override
        public Integer next() {
            this.index++;
            return (int) (this.variance * (this.random.nextGaussian() + this.mean));
        }

        @Override
        public boolean hasNext() {
            return this.index < this.maxIndex;
        }
    }

    public static class FibonacciNumberGenerator implements INumberGenerator {

        private int index;
        private int previousPreviousNumber;
        private int previousNumber;
        private final int maxIndex;

        public FibonacciNumberGenerator(int numElements) {
            this.maxIndex = numElements;
        }

        @Override
        public Integer next() {
            int next;

            if (this.index == 0) {
                next = 1;
            } else {
                next = this.previousPreviousNumber + this.previousNumber;

                this.previousPreviousNumber = this.previousNumber;
            }

            this.previousNumber = next;
            this.index++;
            return next;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.maxIndex;
        }
    }

    public interface IPercentileFinder {
        int findPercentile(int[] elements, int percentile);
    }

    public static class ClosestPercentileFinder implements IPercentileFinder {

        @Override
        public int findPercentile(int[] elements, int percentile) {
            int nearestPercentile = (int) (percentile * elements.length / 100.0 + 0.5) - 1;
            return elements[nearestPercentile];
        }
    }

    public static class InterpolatedPercentileFinder implements IPercentileFinder {

        @Override
        public int findPercentile(int[] elements, int percentile) {
            int lower = 0;
            int lowerPercentile = 0;
            int upper = elements.length - 1;

            for (int i = 0; i < elements.length; i++) {
                int iPercentile = (int) (100 * (i + 0.5) / elements.length) - 1;

                if (iPercentile <= percentile) {
                    lower = i;
                    lowerPercentile = iPercentile;
                }

                if (iPercentile >= percentile) {
                    upper = i;

                    break;
                }
            }

            return elements[lower] + elements.length * (percentile - lowerPercentile) *
                    (elements[upper] - elements[lower]) / 100;
        }
    }
}
