package me.ascendit.ui.notification;

import java.util.concurrent.LinkedBlockingQueue;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render2DEvent;

public class NotificationManager {
	
	private static LinkedBlockingQueue<Notification> pendingnotifications = new LinkedBlockingQueue<>();
	private static Notification currentnotification = null;
	
	public static void show(Notification notification) {
		pendingnotifications.add(notification);
	}
	
	public static void update() {
		if (currentnotification != null && !currentnotification.isShown()) {
			currentnotification = null;
		}
		
		if (currentnotification == null && !pendingnotifications.isEmpty()) {
			currentnotification = pendingnotifications.poll();
			currentnotification.show();
		}
	}
	
	@EventTarget
	public static void render(final Render2DEvent event) {
		update();
		
		if (currentnotification != null)
			currentnotification.render();
	}
}