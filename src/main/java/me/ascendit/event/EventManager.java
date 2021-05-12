package me.ascendit.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import me.ascendit.command.ChatMacro;
import me.ascendit.module.ModuleManager;
import me.ascendit.ui.notification.NotificationManager;
import me.ascendit.util.MovementUtils;

public class EventManager {

	private static final Map<Class<? extends Event>, CopyOnWriteArrayList<EventData>> REGISTRY_MAP = new HashMap<Class<? extends Event>, CopyOnWriteArrayList<EventData>>();

	public static void registerEventClasses() {
		EventManager.register(new ChatMacro());
		EventManager.register(new ModuleManager());
		EventManager.register(new NotificationManager());
        EventManager.register(new MovementUtils());
	}
	
	private static void sortListValue(final Class<? extends Event> clazz) {
		final CopyOnWriteArrayList<EventData> flexableArray = new CopyOnWriteArrayList<EventData>();

		for (final byte b : EventPriority.VALUE_ARRAY) {
			for (EventData methodData : EventManager.REGISTRY_MAP.get(clazz)) {
				if (methodData.priority == b) {
					flexableArray.add(methodData);
				}
			}
		}
		EventManager.REGISTRY_MAP.put(clazz, flexableArray);
	}

	private static boolean isMethodBad(final Method method) {
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}

	private static boolean isMethodBad(final Method method, final Class<? extends Event> clazz) {
		return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
	}

	public static CopyOnWriteArrayList<EventData> get(final Class<? extends Event> clazz) {
		return REGISTRY_MAP.get(clazz);
	}

	public static void cleanMap(final boolean removeOnlyEmptyValues) {
		final Iterator<Map.Entry<Class<? extends Event>, CopyOnWriteArrayList<EventData>>> iterator = EventManager.REGISTRY_MAP.entrySet().iterator();

		while (iterator.hasNext()) {
			if (!removeOnlyEmptyValues || iterator.next().getValue().isEmpty()) {
				iterator.remove();
			}
		}
	}

	public static void unregister(final Object o, final Class<? extends Event> clazz) {
		if (REGISTRY_MAP.containsKey(clazz)) {
			for (final EventData methodData : REGISTRY_MAP.get(clazz)) {
				if (methodData.source.equals(o)) {
					REGISTRY_MAP.get(clazz).remove(methodData);
				}
			}
		}
		cleanMap(true);
	}

	public static void unregister(final Object o) {
		for (CopyOnWriteArrayList<EventData> flexableArray : REGISTRY_MAP.values()) {
			for (int i = flexableArray.size() - 1; i >= 0; i--) {

				if (flexableArray.get(i).source.equals(o)) {
					flexableArray.remove(i);
				}

			}
		}
		cleanMap(true);
	}

	@SuppressWarnings("serial")
	public static void register(final Method method, final Object o) {
		final Class<?> clazz = method.getParameterTypes()[0];
		final EventData methodData = new EventData(o, method, method.getAnnotation(EventTarget.class).priority());
		if (!methodData.target.isAccessible()) {
			methodData.target.setAccessible(true);
		}
		if (REGISTRY_MAP.containsKey(clazz)) {
			if (!REGISTRY_MAP.get(clazz).contains(methodData)) {
				REGISTRY_MAP.get(clazz).add(methodData);
				sortListValue((Class<? extends Event>) clazz);
			}
		} else {
			REGISTRY_MAP.put((Class<? extends Event>) clazz, new CopyOnWriteArrayList<EventData>() {
				{
					this.add(methodData);
				}
			});
		}
	}

	public static void register(final Object o, final Class<? extends Event> clazz) {
		for (final Method method : o.getClass().getMethods()) {
			if (!isMethodBad(method, clazz)) {
				register(method, o);
			}
		}
	}

	public static void register(Object o) {
		for (final Method method : o.getClass().getMethods()) {
			if (!isMethodBad(method)) {
				register(method, o);
			}
		}
	}
}