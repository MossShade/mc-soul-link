package com.mossshade.soullink.pool;

@FunctionalInterface
public interface EveryoneOperation<T> {
	void apply(T singleEntity);
}
