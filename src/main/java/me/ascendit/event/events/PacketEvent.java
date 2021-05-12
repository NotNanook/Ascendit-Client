package me.ascendit.event.events;

import me.ascendit.event.Event;

import net.minecraft.network.Packet;

public class PacketEvent extends Event {
	private Packet<?> packet;

    public PacketEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }
}