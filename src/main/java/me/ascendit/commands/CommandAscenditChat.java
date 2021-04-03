package me.ascendit.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import net.minecraft.util.EnumChatFormatting;

public class CommandAscenditChat extends Command
{
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	
	public CommandAscenditChat() 
	{
		super("acc", "Connects you to the Ascendit chat server", "Usage: .acc <connect/say> <message>");
		this.registerCommand();
	}

	@Override
	public void onCommand(String[] args) 
	{
		if(args.length == 2 && args[1].equalsIgnoreCase("connect"))
		{
			try
			{
				socket = new Socket("127.0.0.1", 5000);
				
				input = new DataInputStream(System.in);
				output = new DataOutputStream(socket.getOutputStream());
				
				output.writeUTF(mc.thePlayer.getName());
				output.flush();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		else if(args.length == 2 && args[1].equalsIgnoreCase("disconnect"))
		{
			try 
			{
				input.close();
				output.close();
				socket.close();
				this.sendMessage("Disconnected", EnumChatFormatting.RED);
			} 
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		else if(args.length == 3 && args[1].equalsIgnoreCase("say"))
		{
			try
			{
				output.writeUTF(args[2]);
				output.flush();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}
