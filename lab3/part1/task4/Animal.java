package task4;

public abstract class Animal {

    public final void animalPrintGreeting() {
        System.out.println(this.name() + " greets: " + this.greet());
    }

    public final void animalPrintMenu() {
        System.out.println(this.name() + " likes " + this.menu());
    }

    public abstract String name();

    public abstract String greet();

    public abstract String menu();
}
