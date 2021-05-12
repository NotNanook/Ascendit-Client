package me.ascendit.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import me.ascendit.command.ChatMacro;
import me.ascendit.event.EventManager;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.setting.Setting;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class SaveLoad {

	private final File dir;
	private final File dataFile;

	public SaveLoad() {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "ascendit");
		if (!dir.exists())
			dir.mkdir();
		dataFile = new File(dir, "data.txt");
		if (!dataFile.exists())
			try {
				dataFile.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
	}

	public boolean FileExists() {
		return dir.exists();
	}

	public void load() {
		final ArrayList<String> lines = new ArrayList<String>();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		for (final String string : lines) {
			final String[] args = string.split(":");
			if (string.toLowerCase().startsWith("module:")) {
				final Module module = Module.getModulebyName(args[1]);
				if (module != null) {
					module.setToggled(Boolean.parseBoolean(args[2]));
					module.setKeybind(Integer.parseInt(args[3]));
					if (module.isToggled())
						EventManager.register(module);
				}
			} else if (string.toLowerCase().startsWith("setting:")) {
				final Module module = Module.getModulebyName(args[2]);
				if (module != null) {
					final Setting setting = Setting.getSettingByName(module, args[1]);
					if (setting != null)
						if (setting instanceof BoolSetting)
							((BoolSetting) setting).set(Boolean.parseBoolean(args[3]));
						else if (setting instanceof DoubleSetting)
							((DoubleSetting) setting).set(Double.parseDouble(args[3]));
						else if (setting instanceof FloatSetting)
							((FloatSetting) setting).set(Float.parseFloat(args[3]));
						else if (setting instanceof IntSetting)
							((IntSetting) setting).set(Integer.parseInt(args[3]));
						else if (setting instanceof ModeSetting)
							((ModeSetting) setting).setMode(args[3]);
				}
			} else if (string.toLowerCase().startsWith("chatmacro:")) {
				final ChatMacro chatmacro = new ChatMacro(args[1], Integer.parseInt(args[2]));
				if (!ChatMacro.chatmacros.contains(chatmacro))
					ChatMacro.chatmacros.add(chatmacro);

			} else if (string.toLowerCase().startsWith("xrayblock:"))
				if (Block.getBlockFromName(args[2]) != null
						&& !ModuleManager.xray.xrayblocks.contains(Block.getBlockFromName(args[2])))
					ModuleManager.xray.xrayblocks.add(Block.getBlockFromName(args[2]));
		}
	}

	public void save() {
		final ArrayList<String> toSave = new ArrayList<String>();
		for (final Module module : ModuleManager.modules) {
			toSave.add("MODULE:" + module.getName() + ":" + module.isToggled() + ":" + module.getKeybind());
			for (final Setting setting : module.getSettings())
				if (setting instanceof BoolSetting)
					toSave.add("SETTING:" + setting.getName() + ":" + module.getName() + ":"
							+ ((BoolSetting) setting).get());
				else if (setting instanceof DoubleSetting)
					toSave.add("SETTING:" + setting.getName() + ":" + module.getName() + ":"
							+ ((DoubleSetting) setting).get());
				else if (setting instanceof FloatSetting)
					toSave.add("SETTING:" + setting.getName() + ":" + module.getName() + ":"
							+ ((FloatSetting) setting).get());
				else if (setting instanceof IntSetting)
					toSave.add("SETTING:" + setting.getName() + ":" + module.getName() + ":"
							+ ((IntSetting) setting).get());
				else if (setting instanceof ModeSetting)
					toSave.add("SETTING:" + setting.getName() + ":" + module.getName() + ":"
							+ ((ModeSetting) setting).getMode());
		}

		for (final ChatMacro chatmacro : ChatMacro.chatmacros)
			if (!ChatMacro.chatmacros.isEmpty())
				toSave.add("CHATMACRO:" + chatmacro.getMessage() + ":" + chatmacro.getKey());

		for (final Block block : ModuleManager.xray.xrayblocks)
			if (!ModuleManager.xray.xrayblocks.isEmpty() && block != null)
				toSave.add("XRAYBLOCK:" + block.getRegistryName());

		try {
			final PrintWriter pw = new PrintWriter(dataFile);
			for (final String str : toSave)
				pw.println(str);
			pw.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}