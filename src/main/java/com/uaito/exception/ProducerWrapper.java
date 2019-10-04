package com.uaito.exception;

@FunctionalInterface
public interface ProducerWrapper<T> {
    T get() throws Exception;

    static <T> T wrap(ProducerWrapper<T> producer) {
        try {
            return producer.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
