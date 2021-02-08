package me.ascendit.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event {

	private Packet<?> packet;

	public PacketEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public static class Outgoing extends PacketEvent {

		public Outgoing(Packet<?> packetIn) {
			super(packetIn);
		}

	}

	public static class Incoming extends PacketEvent {

		public Incoming(Packet<?> packetIn) {
			super(packetIn);
		}

	}
}
