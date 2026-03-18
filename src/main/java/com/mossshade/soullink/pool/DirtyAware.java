package com.mossshade.soullink.pool;

public interface DirtyAware<T> {

	boolean isDirty();

	void markDirty(T t);

	void clean();

	T getDirt();

}
