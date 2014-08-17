package cz.kadledav.am.client;

import java.util.Set;

import org.reflections.Reflections;

public class StateInformatorFactory {

	private static StateInformator instance;

	static {
		Reflections reflections = new Reflections("cz.kadledav.am");
		Set<Class<? extends StateInformator>> informatorClazzSet = reflections.getSubTypesOf(StateInformator.class);
		if (informatorClazzSet.isEmpty()) {
			throw new IllegalStateException("There must be at least one implementation of StateInformator interface on the class path");
		}
		Class<? extends StateInformator> informatorClazz = informatorClazzSet.iterator().next();
		try {
			instance = informatorClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Implementations of StateInformator are allowed only with primitive public constructor");
		}
	}

	public static StateInformator getInformator() {
		return instance;
	}
}
