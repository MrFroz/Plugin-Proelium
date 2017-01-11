package fr.froz.proelium;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class staffFeat implements Listener{
	List<Player> p;
	List<Player> wp = new LinkedList<Player>();
	public static ItemStack setSkin(ItemStack item, String nick) {
		
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(nick);
        item.setItemMeta(meta);
        return item;
    }
	 public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
	        ItemStack item = new ItemStack(material);
	        ItemMeta meta = item.getItemMeta();
	        meta.setDisplayName(name);
	        ArrayList<String> Lore = new ArrayList<String>();
	        Lore.add(lore);
	        meta.setLore(Lore);
	        item.setItemMeta(meta);

	        inv.setItem(Slot, item);

	    }
	public Inventory mainMenu(){
		Inventory inv = Bukkit.createInventory(null, 9 , "Staff");
		createDisplay(Material.GOLD_PICKAXE ,inv , 0 ,ChatColor.AQUA + "Mineurs", ChatColor.RED + "voir les joueurs qui mines");
		
		return inv;
	}
	public Inventory makeMine(){
	Inventory inv = Bukkit.createInventory(null, 27 , "Mineurs");
	 p = Bukkit.getWorld("ressource").getPlayers();
	int i = 0;
	int y = 0;
	while(i < p.size()){
		if(p.get(i).getLocation().getBlockY() < 40){
		ItemStack skull = setSkin(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3),p.get(i).getName());
		 ItemMeta meta = skull.getItemMeta();
	        meta.setDisplayName(p.get(i).getName());
	        skull.setItemMeta(meta);
	        inv.setItem(y, skull);
	        wp.add(p.get(i));
	        //wp.add(y, p.get(i));
	        y++;
		}
        i++;

	}
	
	return inv;
		
	}
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
	    Player player = (Player) event.getWhoClicked();
		 if (inv.getName().equals("Mineurs")){
			 event.setCancelled(true);
	         player.closeInventory();
	         int slot = event.getSlot();
	         if(slot < wp.size()){
	         player.teleport(wp.get(slot));
	         player.sendMessage(ChatColor.RED + "[ProeliumCore] Téléportation !");
	         Main.disableListener(this);
	         }
		 }
	}
}
