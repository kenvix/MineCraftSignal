package net.moecraft.mcsignal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import sun.misc.SignalHandler;
import sun.misc.Signal;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import java.io.File;
import java.util.List;

public class MCSignal extends JavaPlugin implements Listener,SignalHandler {

	public String g(String n) {
		return getConfig().getString(n);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mcsignal")){
			sender.sendMessage("--------MineCraft Signal By Kenvix @ MoeCraft-------");
			return true;
		 }
		return false;
	}

	@Override
	public void handle(Signal sign) {
		String signal = sign.toString();
		try {
			List commands = getConfig().getList(signal);
			if(commands == null) {
				getLogger().warning("Undefined Signal command. Ignore" + signal);
				return;
			}
			if(!commands.isEmpty()) {
				getLogger().info("Received Signal " + signal + " . Running ...");
				for (Object c:commands) {
					String command = c.toString();
					if(!command.isEmpty()) {
						getServer().dispatchCommand(getServer().getConsoleSender(), command);
					}
				}
			} else {
				getLogger().warning("Signal command is empty. Ignore" + signal);
			}
		} catch (Exception ex) {
			getLogger().warning("Process Signal Failed. Ignore " + signal);
			ex.printStackTrace();
		}
	}

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getLogger().info("Data Folder not exists, create it");
		}
		File file = new File(getDataFolder(), "config.yml");
		if(!file.exists()) {
			saveDefaultConfig();
			getLogger().info("Config not exists, create it");
		}
		reloadConfig();
		try {
			List handlers = getConfig().getList("handle");
			for (Object h:handlers) {
				String handler = h.toString();
				getLogger().info("Registering Signal Handler: " + handler);
				Signal.handle(new Signal(handler), this);
			}

		} catch (Exception ex) {
			getLogger().warning("Register Signal Handler Failed. Stop");
			ex.printStackTrace();
		}
		getLogger().info("Enabled - By: Kenvix @ MoeCraft");
	}

	public void onDisable() {
		getLogger().info("Disabled");
	}
}