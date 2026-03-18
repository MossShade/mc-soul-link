package com.mossshade.soullink.pool;

public interface IPoolManager<E> {

	void propagatePool();

	void reset();

	void syncEveryone();

	void syncEntity(E entity);

	void applyToEveryone(EveryoneOperation<E> function, boolean skipSource);

}
