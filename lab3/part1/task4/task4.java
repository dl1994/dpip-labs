package task4;

public final class task4 {

    public static void main(String[] args) {
        try {
            Animal parrot = AnimalFactory.newInstance("Parrot", "Birdy");
            Animal tiger = AnimalFactory.newInstance("Tiger", "Bongo");

            parrot.animalPrintGreeting();
            parrot.animalPrintMenu();
            tiger.animalPrintGreeting();
            tiger.animalPrintMenu();
        } catch (Exception ignored) {
            System.err.println("Unable to instantiate object!");
        }
    }
}
