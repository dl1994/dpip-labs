package task4;

import java.lang.reflect.Constructor;

public final class AnimalFactory {

    private AnimalFactory() {}

    @SuppressWarnings("unchecked")
    public static Animal newInstance(String animalKind, String name) throws Exception {
        Class<Animal> clazz = (Class<Animal>) Class.forName("task4.plugins." + animalKind);
        Constructor<Animal> constructor = clazz.getConstructor(String.class);
        return constructor.newInstance(name);
    }
}
