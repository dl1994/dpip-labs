package task4.plugins;

import task4.Animal;

public final class Parrot extends Animal {

    private final String name;

    public Parrot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String greet() {
        return "yaaaaarrrr!";
    }

    @Override
    public String menu() {
        return "seeds";
    }
}
