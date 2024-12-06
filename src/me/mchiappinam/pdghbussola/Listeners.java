package me.mchiappinam.pdghbussola;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
	
	private Main plugin;
	public Listeners(Main main) {
		plugin=main;
	}

	@EventHandler
    public void onInteract(PlayerInteractEvent e) {
		if(e.getPlayer().hasPermission("pdgh.admin"))
			return;
        if(e.getAction().equals(Action.LEFT_CLICK_AIR)||e.getAction().equals(Action.LEFT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
                e.setCancelled(true);
                plugin.tags();
                e.getPlayer().openInventory(Main.menu);
            }
        }
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
	    ItemStack clicked = e.getCurrentItem();
	    Inventory inventory = e.getInventory();
	    if (inventory.getName().equals(Main.menu.getName())) {
	    	e.setCancelled(true);
	    	if ((e.getCurrentItem() != null)&&(!e.getCurrentItem().getType().equals(Material.AIR)))
		    	if (clicked.getType() == Material.SKULL_ITEM) { //Jogadores proximos = 1
			    	p.closeInventory();
					for(String linha : plugin.lista) {
						String ent[] = linha.split(";");
						if(ent[0].contains(p.getName().toLowerCase())) {
							if(ent[1].equals("1")) {
								p.sendMessage("§cEssa opção já está selecionada!");
								return;
							}else{
								p.sendMessage("§cDesligue sua bússola primeiro para selecionar essa opção!");
								return;
							}
							//plugin.lista.remove(linha);
					    	//plugin.lista.add(p.getName().toLowerCase()+";"+"1");
					    	//return;
						}
					}
			    	plugin.lista.add(p.getName().toLowerCase()+";"+"1");
					p.sendMessage("§aSua bússola se atualizará a cada segundo no jogador mais próximo!");
				    return;
			    }else if (clicked.getType() == Material.DIAMOND_SWORD) { //Membro clan proximo = 2
			    	p.closeInventory();
					p.sendMessage("§d§lBússola desligada! Opção em criação.");
					/**for(String linha : plugin.lista) {
						String ent[] = linha.split(";");
						if(ent[0].contains(p.getName().toLowerCase())) {
							if(ent[1].equals("2")) {
								p.sendMessage("§cEssa opção já está selecionada!");
								return;
							}else{
								p.sendMessage("§cDesligue sua bússola primeiro para selecionar essa opção!");
								return;
							}
						}
					}
			    	plugin.lista.add(p.getName().toLowerCase()+";"+"2");
					p.sendMessage("§aSua bússola se atualizará a cada segundo no membro do seu clan mais próximo!");*/
				    return;
			    }else if (clicked.getType() == Material.WORKBENCH) { //Spawn = 3
			    	p.closeInventory();
					for(String linha : plugin.lista) {
						String ent[] = linha.split(";");
						if(ent[0].contains(p.getName().toLowerCase())) {
							if(ent[1].equals("3")) {
								p.sendMessage("§cEssa opção já está selecionada!");
								return;
							}else{
								p.sendMessage("§cDesligue sua bússola primeiro para selecionar essa opção!");
								return;
							}
						}
					}
			    	plugin.lista.add(p.getName().toLowerCase()+";"+"3");
					p.sendMessage("§aSua bússola se atualizará a cada segundo!");
				    return;
			    }else if (clicked.getType() == Material.REDSTONE_LAMP_OFF) { //Off = null
					plugin.remove(p.getName());
				    return;
			    }else if (clicked.getType() == Material.FIRE) { //Off = null
			    	p.closeInventory();
					for(String linha : plugin.lista) {
						String ent[] = linha.split(";");
						if(ent[0].contains(p.getName().toLowerCase())) {
							plugin.lista.remove(linha);
							p.sendMessage("§aVocê desligou sua bússola.");
							if(p.getInventory().contains(Material.COMPASS)) {
								p.getInventory().removeItem(new ItemStack(Material.COMPASS, 1));
								p.sendMessage("§aSua bússola foi removida com sucesso. Adquira outra bússola com o comando §l/bussola");
							}else{
								p.sendMessage("§cVocê não tem uma bússola no inventário.");
							}
							return;
						}
					}
					p.sendMessage("§cVocê não está com a bússola ligada!");
					if(p.getInventory().contains(Material.COMPASS)) {
						p.getInventory().removeItem(new ItemStack(Material.COMPASS));
						p.sendMessage("§aSua bússola foi removida com sucesso. Adquira outra bússola com o comando §l/bussola");
					}else{
						p.sendMessage("§cVocê não tem uma bússola no inventário.");
					}
				    return;
			    }
		    return;
	    }
	}

	@EventHandler
    public void onQuit(PlayerQuitEvent e) {
		plugin.remove(e.getPlayer().getName());
	}

	@EventHandler
    public void onKick(PlayerKickEvent e) {
		plugin.remove(e.getPlayer().getName());
	}
	
	
	
}