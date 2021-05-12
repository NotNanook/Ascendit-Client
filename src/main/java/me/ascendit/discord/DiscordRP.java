package me.ascendit.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordRP {
	
	private boolean running = true;
	private long created = 0;
	private final String applicationid = "832619832045469727";
	
	public void start() {
		created = System.currentTimeMillis();
		
		final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			@Override
			public void apply(final DiscordUser user) {
				System.out.println("Welcome " + user.username + "#" + user.discriminator);
			}
		}).build();
		
		DiscordRPC.discordInitialize(applicationid, handlers, true);
		
		new Thread("Discord RPC Callback") {
			
			@Override
			public void run() {
				while(running) {
					DiscordRPC.discordRunCallbacks();
				}
			}
		}.start();
	}
	
	public void shutdown() {
		running = false;
		DiscordRPC.discordShutdown();
	}
	
	public void update(final String firstline, final String secondline) {
		final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(secondline);
		builder.setBigImage("large", "");
		builder.setDetails(firstline);
		builder.setStartTimestamps(created);
		
		DiscordRPC.discordUpdatePresence(builder.build());
	}
}