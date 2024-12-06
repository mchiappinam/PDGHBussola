package me.mchiappinam.bussola;

import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class bussola
  implements Listener
{
  private Main plugin;
  private HashMap<Player, Player> compassTargets;

  public bussola(Main instance)
  {
  }
	  public Player getNearestPlayer(Player player) {
		    double distNear = 0.0D;
		    Player playerNear = null;
		    for (Player player2 : Bukkit.getOnlinePlayers()) {
		        //don't include the player that's checking
		        if (player == player2) { continue; }
		        //make sure same world (cannot measure distance between worlds)
		        if (player.getWorld() != player2.getWorld()) { continue; }
		     
		        Location location2 = player.getLocation();
		        double dist = location.distance(location2);
		        if (playerNear == null || dist < distNear) {
		            playerNear = player2;
		            distNear = dist;
		        }
		    }
		    return playerNear;
		}
		 
		//you need to define the player variable
		Player playerNear = getNearestPlayer(player);
		if (playerNear != null) {
			player.setCompassTarget(playerNear);
		}
