package org.app.query.specification;

public interface ISpecification<T> {
    boolean isSatisfiedBy(T entity);
    String toString();
}
