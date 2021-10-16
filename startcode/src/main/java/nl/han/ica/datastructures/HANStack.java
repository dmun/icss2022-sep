package nl.han.ica.datastructures;

import java.util.ArrayList;
import java.util.List;

public class HANStack<T> implements IHANStack<T> {

    private List<T> stack;

    public HANStack() {
        this.stack = new ArrayList<>();
    }

    @Override
    public void push(T value) {
        stack.add(value);
    }

    @Override
    public T pop() {
        T value = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return value;
    }

    @Override
    public T peek() {
        return stack.get(stack.size() - 1);
    }
}
