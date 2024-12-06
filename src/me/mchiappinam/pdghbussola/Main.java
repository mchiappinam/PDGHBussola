package me.mchiappinam.pdghbussola;

import java.util.ArrayList;
import java.util.List;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;

public class Main extends JavaPlugin {
	public static Inventory menu = Bukkit.createInventory(null, 9, "§e» §a§lBússola PDGH §e«");
	List<String> lista = new ArrayList<String>();
	int tprincipal;
	protected SCCore core;
	protected SimpleClans core2;
	protected int version = 0;
	
	public void onEnable() {
	    getServer().getPluginCommand("bussola").setExecutor(new Comando(this));
	    getServer().getPluginManager().registerEvents(new Listeners(this), this);

		if (hookSimpleClans()) {
			getLogger().info("Hooked to SimpleClans2!");
			version = 2;
		}else if (getServer().getPluginManager().getPlugin("SimpleClans") != null) {
			getLogger().info("Hooked to SimpleClans!");
			core2 = ((SimpleClans)getServer().getPluginManager().getPlugin("SimpleClans"));
			version = 1;
		}else{
			getServer().getPluginManager().disablePlugin(this);
		}
	    task();
	    getServer().getConsoleSender().sendMessage("§3[PDGHBussola] §2ativado - Plugin by: mchiappinam");
	    getServer().getConsoleSender().sendMessage("§3[PDGHBussola] §2Acesse: http://pdgh.com.br/");
	}

	public void onDisable() {
	    getServer().getConsoleSender().sendMessage("§3[PDGHBussola] §2desativado - Plugin by: mchiappinam");
	    getServer().getConsoleSender().sendMessage("§3[PDGHBussola] §2Acesse: http://pdgh.com.br/");
	}
	
	public void getPlayerAll(Player p) {
		List<Player> players = p.getWorld().getPlayers();
		if(players.size()<=1) {
			for(String linha : lista) {
				String ent[] = linha.split(";");
				if(ent[0].contains(p.getName().toLowerCase())) {
					lista.remove(linha);
				}
			}
			p.sendMessage("§7Apenas você está nesse mundo! Sua bússola foi desligada!");
			return;
		}
	    double distNear = 0.0D;
	    Player playerNear = null;
	    for (Player player2 : Bukkit.getOnlinePlayers()) {
	        //don't include the player that's checking
	        if (p == player2) { continue; }
	        //make sure same world (cannot measure distance between worlds)
	        if (p.getWorld() != player2.getWorld()) { continue; }
	     
	        Location location2 = p.getLocation();
	        double dist = player2.getPlayer().getLocation().distance(location2);
	        if (playerNear == null || dist < distNear) {
	            playerNear = player2;
	            distNear = dist;
	        }
	    }
	    p.setCompassTarget(playerNear.getLocation());
	}
	
	public void getPlayerClan(Player p) {
		try {
			if (version == 2)
				p.sendMessage("§aSeu clan tem "+getClanPlayerManager().getAnyClanPlayer((Player)p).getClan().getMembers().size()+" membros.");
			else
				p.sendMessage("§aSeu clan tem "+core2.getClanManager().getClanByPlayerName(p.getName()).getAllMembers().size()+" membros.");
		}catch (Exception e) {
			for(String linha : lista) {
				String ent[] = linha.split(";");
				if(ent[0].contains(p.getName().toLowerCase())) {
					lista.remove(linha);
				}
			}
			p.sendMessage("§cVocê não tem clan!");
			return;
		}
		/**List<Player> players = p.getWorld().getPlayers();
		if(players.size()<=1) {
			for(String linha : lista) {
				String ent[] = linha.split(";");
				if(ent[0].contains(p.getName().toLowerCase())) {
					lista.remove(linha);
				}
			}
			p.sendMessage("§7Apenas você está nesse mundo! Sua bússola foi desligada!");
			return;
		}*/
	    double distNear = 0.0D;
	    Player playerNear = null;
		if (version == 2) {
		    /**for (ClanPlayer player2 : getClanPlayerManager().getAnyClanPlayer((Player)p).getClan().getMembers()) {
		        //don't include the player that's checking
		        if (p == player2) { continue; }
		        //make sure same world (cannot measure distance between worlds)
		        if (p.getWorld() != player2.getWorld()) { continue; }
		     
		        Location location2 = p.getLocation();
		        double dist = player2.getPlayer().getLocation().distance(location2);
		        if (playerNear == null || dist < distNear) {
		            playerNear = player2;
		            distNear = dist;
		        }
		    }*/
		}else{
			List<net.sacredlabyrinth.phaed.simpleclans.ClanPlayer> players = core2.getClanManager().getClanByPlayerName(p.getName()).getAllMembers();
			for(net.sacredlabyrinth.phaed.simpleclans.ClanPlayer lista : players) {
		        //don't include the player that's checking
				String a = (""+lista).toLowerCase();
		        if (p.getName().toLowerCase() == a) { continue; }
		        //make sure same world (cannot measure distance between worlds)
		        //if(getServer().getPlayer((""+lista).toLowerCase()).isOnline())
			if(getServer().getPlayer(a).isOnline()) {continue;}
			//getServer().broadcastMessage(getServer().getPlayer((a).toLowerCase()).getName());
		        	if (p.getWorld() == getServer().getPlayer(a).getWorld()) {
				        Location location2 = p.getLocation();
				        double dist = getServer().getPlayer(a).getPlayer().getLocation().distance(location2);
				        if (playerNear == null || dist < distNear) {
				            playerNear = getServer().getPlayer(a);
				            distNear = dist;
				        }
				}
        	}
		}
	    p.setCompassTarget(playerNear.getLocation());
	}
	
	public void remove(String p) {
		for(String linha : lista) {
			String ent[] = linha.split(";");
			if(ent[0].contains(p.toLowerCase())) {
				lista.remove(linha);
				getServer().getPlayer(p).sendMessage("§aBússola desligada.");
				return;
			}
		}
		getServer().getPlayer(p).closeInventory();
		getServer().getPlayer(p).sendMessage("§cVocê não está com a bússola ligada!");
	}

	private boolean hookSimpleClans() {
		try {
			for(Plugin plugin : getServer().getPluginManager().getPlugins()) {
				if ((plugin instanceof SCCore)) {
					core = ((SCCore)plugin);
					return true;
				}
			}
		}catch (NoClassDefFoundError e) {
			return false;
		}
		return false;
	}

	public ClanPlayerManager getClanPlayerManager() {
		return core.getClanPlayerManager();
	}
	
	public void task() {
		tprincipal = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	  		public void run() {
				for(String linha : lista) {
					String ent[] = linha.split(";");
					if(ent[1].contains("1")) {
			  	    	getPlayerAll(getServer().getPlayer(ent[0]));
					}else if(ent[1].contains("2")) {
						getServer().getPlayer(ent[0]).sendMessage("§d§lBússola desligada! Opção em criação.");
						remove(ent[0]);
						//getPlayerClan(getServer().getPlayer(ent[0]));
					}else if(ent[1].contains("3")) {
			  	    	getServer().getPlayer(ent[0]).setCompassTarget(getServer().getPlayer(ent[0]).getWorld().getSpawnLocation());
					}
				}
	  		}
		}, 0, 30);
	}
	
	public void giveBussola(Player p) {
		if(p.getInventory().contains(Material.COMPASS)) {
			p.sendMessage("§cVocê já tem uma bússola em seu inventário!");
			return;
		}
	    ItemStack bussola = new ItemStack(Material.COMPASS, 1);
	    bussola.getItemMeta().setDisplayName("§aAperte com o botão direito para alterar o destino da bússola.");
		p.getInventory().addItem(bussola);
	}
	
	public void tags() {
		ItemStack a0 = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); //1111111111111111111111
	    ItemMeta b0 = a0.getItemMeta();
		List<String> l0 = new ArrayList<String>();
	    b0.setDisplayName("§aJOGADOR");
	    l0.add(" ");
	    l0.add("§aApontar a bússola para o §lJOGADOR§a mais próximo.");
	    b0.setLore(l0);
	    a0.setItemMeta(b0);
	    menu.setItem(0, a0);

		ItemStack a1 = new ItemStack(Material.DIAMOND_SWORD, 1);//22222222222222222222
	    ItemMeta b1 = a1.getItemMeta();
		List<String> l1 = new ArrayList<String>();
	    b1.setDisplayName("§aMEMBRO DO CLAN");
	    l1.add(" ");
	    l1.add("§aApontar a bússola para o §lMEMBRO DO SEU CLAN§a mais próximo.");
	    b1.setLore(l1);
	    a1.setItemMeta(b1);
	    menu.setItem(2, a1);

		ItemStack a2 = new ItemStack(Material.WORKBENCH, 1);//3333333333333333333333333
	    ItemMeta b2 = a2.getItemMeta();
		List<String> l2 = new ArrayList<String>();
	    b2.setDisplayName("§7SPAWN");
	    l2.add(" ");
	    l2.add("§aApontar a bússola para o §7§lSPAWN§a.");
	    b2.setLore(l2);
	    a2.setItemMeta(b2);
	    menu.setItem(6, a2);

		ItemStack a3 = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);//nuuuuuuuuullllllllllll
	    ItemMeta b3 = a3.getItemMeta();
		List<String> l3 = new ArrayList<String>();
	    b3.setDisplayName("§c§lDESLIGAR");
	    l3.add(" ");
	    l3.add("§cDesliga a bússola.");
	    b3.setLore(l3);
	    a3.setItemMeta(b3);
	    menu.setItem(8, a3);

		ItemStack a4 = new ItemStack(Material.FIRE, 1);//nuuuuuuuuullllllllllll
	    ItemMeta b4 = a4.getItemMeta();
		List<String> l4 = new ArrayList<String>();
	    b4.setDisplayName("§c§lDESLIGAR E DELETAR BÚSSOLA");
	    l4.add(" ");
	    l4.add("§cDesliga e deleta a bússola.");
	    b4.setLore(l4);
	    a4.setItemMeta(b4);
	    menu.setItem(4, a4);
	}
}