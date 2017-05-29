package task4.plugins;

import task4.Animal;

public final class Tiger extends Animal {

    private final String name;

    public Tiger(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String greet() {
        return "growl!";
    }

    @Override
    public String menu() {
        return "warm milk";
    }
}
