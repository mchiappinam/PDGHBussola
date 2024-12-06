package me.mchiappinam.pdghbussola;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comando implements CommandExecutor {
	private Main plugin;

	public Comando(Main main) {
		plugin = main;
	}
	
  	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("bussola")) {
        	if(args.length>0) {
        		sender.sendMessage("§cUse /bussola");
        		return true;
        	}
        	plugin.giveBussola((Player)sender);
        	sender.sendMessage("§aVocê recebeu uma bússola.");
        	return true;
        }
		return false;
    }
  	
}