package me.ascendit.command;

import java.util.ArrayList;

import me.ascendit.command.commands.*;

public class CommandManager {
	
	public static final ArrayList<Command> 		commands 			= new ArrayList<Command>();
	
    public static final CommandBind			 	cmdbind 			= new CommandBind();
    public static final CommandBinds			cmdbinds			= new CommandBinds();
    public static final CommandChatMacro		cmdchatmacro		= new CommandChatMacro();
    public static final CommandCopyIP			cmdcopyip			= new CommandCopyIP();
    public static final CommandFakeCheater 		cmdfakecheater 		= new CommandFakeCheater();
    public static final CommandFurnaceCommand 	cmdfurnacecommand 	= new CommandFurnaceCommand();
    public static final CommandGive				cmdgive				= new CommandGive();
    public static final CommandHelp 			cmdhelp 			= new CommandHelp();
    public static final CommandInvsee			cmdinvsee			= new CommandInvsee();
    public static final CommandPing				cmdping				= new CommandPing();
    public static final CommandSay				cmdsay				= new CommandSay();
    public static final CommandSetLook			cmdsetlook			= new CommandSetLook();
    public static final CommandToggle 			cmdtoggle 			= new CommandToggle();
    public static final CommandXRay				cmdxray				= new CommandXRay();
}