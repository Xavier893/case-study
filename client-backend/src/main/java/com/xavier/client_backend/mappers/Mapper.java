package com.xavier.client_backend.mappers;

/**
 * The Mapper interface defines two methods: mapTo and mapFrom.
 *
 * The mapTo method is used to convert an object of type A to an object of type B.
 * This is useful when you want to convert an Entity object to a DTO object.
 *
 * The mapFrom method is used to convert an object of type B to an object of type A.
 * This is useful when you want to convert a DTO object to an Entity object.
 *
 * @param <A> The type of the original object.
 * @param <B> The type of the converted object.
 */
public interface Mapper<A,B> {
    /**
     * Convert an object of type A to an object of type B.
     *
     * @param a The object to be converted.
     * @return The converted object.
     */
    B mapTo(A a);

    /**
     * Convert an object of type B to an object of type A.
     *
     * @param b The object to be converted.
     * @return The converted object.
     */
    A mapFrom(B b);
}
