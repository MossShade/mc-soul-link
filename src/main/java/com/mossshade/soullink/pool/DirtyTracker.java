package com.mossshade.soullink.pool;

public class DirtyTracker<T> implements DirtyAware<T> {

	private boolean dirty = false;

	private T dirtyObject;

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public void markDirty(T t) {
		this.dirty = true;
		this.dirtyObject = t;
	}

	@Override
	public void clean() {
		this.dirty = false;
		this.dirtyObject = null;
	}

	@Override
	public T getDirt() {
		return this.dirtyObject;
	}

}
