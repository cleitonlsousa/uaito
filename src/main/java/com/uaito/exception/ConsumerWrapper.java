package com.uaito.exception;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConsumerWrapper<T, E extends Exception> {
        void accept(T t) throws E;

    static <T> Consumer<T> wrap(ConsumerWrapper<T, Exception> consumer) {

        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
