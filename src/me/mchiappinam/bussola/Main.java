package me.mchiappinam.bussola;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	  public void onEnable()
	  {
	    getLogger().info("Enabled!");

	    new bussola(this);
	  }

	  public void onDisable() {
	    getLogger().info("Disabled!");
	  }
}