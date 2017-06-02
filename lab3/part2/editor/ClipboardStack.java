package editor;

import editor.interfaces.ClipboardObserver;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public final class ClipboardStack {

    private final Stack<String> texts = new Stack<>();
    private final Set<ClipboardObserver> observers = new HashSet<>();

    public void push(String text) {
        this.texts.push(text);
    }

    public String pop() {
        return this.texts.pop();
    }

    public String peek() {
        return this.texts.peek();
    }

    public boolean isEmpty() {
        return this.texts.isEmpty();
    }

    public void clear() {
        this.texts.clear();
    }

    public void registerObserver(ClipboardObserver observer) {
        this.observers.add(observer);
    }

    public void unregisterObserver(ClipboardObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyObservers() {
        this.observers.forEach(ClipboardObserver::updateClipboard);
    }
}
