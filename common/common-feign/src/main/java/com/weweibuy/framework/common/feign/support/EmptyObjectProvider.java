package com.weweibuy.framework.common.feign.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;

import java.util.function.Consumer;

public class EmptyObjectProvider<T> implements ObjectProvider<T> {

	@Override
	public T getObject(Object... args) throws BeansException {
		return null;
	}

	@Override
	public T getIfAvailable() throws BeansException {
		return null;
	}

	@Override
	public T getIfUnique() throws BeansException {
		return null;
	}

	@Override
	public T getObject() throws BeansException {
		return null;
	}

	@Override
	public void forEach(Consumer action) {
		// do nothing
	}

}