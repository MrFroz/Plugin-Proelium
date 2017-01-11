package fr.froz.proelium;



import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.milkbowl.vault.economy.Economy;

public class PluginListener implements Listener {

    public static Economy economy = null;
    Location l = new Location(Bukkit.getWorld("world"), 10.5, 66, -0.5);

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        //setupEconomy();
        PacketContainer pc = Main.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        pc.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.RED + "" + ChatColor.ITALIC + "Proelium")).write(1, WrappedChatComponent.fromText(ChatColor.RED + "" + ChatColor.ITALIC + "www.proelium.fr"));
        try {
            Main.protocolManager.sendServerPacket(e.getPlayer(), pc);
        } catch (Exception ex) {
            System.out.println("[ProeliumCore] error while create data: " + ex.toString());

        }

        Player p = e.getPlayer();
        l.setYaw(-90);
        p.teleport(l);
        if (p.hasPermission("proelium.modo")) {
            p.setPlayerListName(ChatColor.GOLD + "[Modo] " + p.getName());
        } else if (p.hasPermission("proelium.admin")) {
            p.setPlayerListName(ChatColor.RED + "[Admin] " + p.getName());
        } else if (p.hasPermission("proelium.dev")) {
            p.setPlayerListName(ChatColor.DARK_BLUE + "[Dev] " + p.getName());
        } else if (p.hasPermission("proelium.chefadmin")) {
            p.setPlayerListName(ChatColor.DARK_RED + "[Chef-Admin] " + p.getName());
        } else if (p.hasPermission("proelium.fonda")) {
            p.setPlayerListName(ChatColor.DARK_RED + "[Fondateur] " + p.getName());
        } else {
            p.setPlayerListName(ChatColor.GREEN + "[Citoyen] " + p.getName());

        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Proelium", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Proelium");
        Score score0 = objective.getScore(ChatColor.BOLD + "Joueur: " + ChatColor.GREEN + p.getName());
        setupEconomy();
        Score score1 = objective.getScore(ChatColor.BOLD + "Argent: " + ChatColor.GRAY + economy.getBalance(p));
        p.setScoreboard(manager.getNewScoreboard());
        score0.setScore(2);
        score1.setScore(1);


        p.setScoreboard(board);


    }
    @EventHandler
   public void onInventoryCloseEvent(InventoryCloseEvent event){
    	if(event.getInventory().getName().equals("Pub")){
    		Main.canPub = true;
    	}
	   
   }
    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {               
        Player p = event.getPlayer();
        if (p.hasPermission("firework.level")){
           
            //Spawn the Firework, get the FireworkMeta.
            Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
           
            //Our random generator
            Random r = new Random();   
 
            //Get the type
            int rt = r.nextInt(4) + 1;
            Type type = Type.BALL;       
            if (rt == 1) type = Type.BALL;
            if (rt == 2) type = Type.BALL_LARGE;
            if (rt == 3) type = Type.BURST;
            if (rt == 4) type = Type.CREEPER;
            if (rt == 5) type = Type.STAR;
           
            //Get our random colours   
            int r1i = r.nextInt(17) + 1;
            int r2i = r.nextInt(17) + 1;
           Color c1 = Color.BLUE;
           Color c2 = Color.PURPLE;
            //Create our effect with this
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
           
            //Then apply the effect to the meta
            fwm.addEffect(effect);
           
            //Generate some random power and set it
            int rp = r.nextInt(2) + 1;
            fwm.setPower(rp);
           
            //Then apply this to our rocket
            fw.setFireworkMeta(fwm);           
        }           
    }  

	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Location l0 = new Location(Bukkit.getWorld("ressource"), -431.5, 74, 544.5);
        Location l1 = new Location(Bukkit.getWorld("world"), -900.5, 74, 7220.5);
        Location l2 = new Location(Bukkit.getWorld("FallWiden"), 17.5, 26, -5.5);
        Location l3 = new Location(Bukkit.getWorld("nether"), 8, 54, 8.5);
        Location l4 = new Location(Bukkit.getWorld("world_the_end"), 71.5, 57, 64.5);
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (inventory.getName().equals("Menu")) {
            event.setCancelled(true);
            player.closeInventory();
            switch (event.getCurrentItem().getType()) {
                case DIAMOND_PICKAXE:
                    player.teleport(l0);
                    break;
                case SAND:
                    player.teleport(l1);
                    break;
                case SEA_LANTERN:
                    player.teleport(l2);
                    break;
                case NETHERRACK:
                    player.teleport(l3);
                    break;
                case ENDER_STONE:
                    player.teleport(l4);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "[CoreProelium] Veuillez cliquez dans le menu.");


            }
        } else if(inventory.getName().equals("Pub")){
        	  event.setCancelled(true);
              player.closeInventory();
              setupEconomy();
        	switch(event.getSlot()){
        	case 0:
        		if(Main.msgNbr[0]==0){
        			Main.msg[0] = Main.msgPub;
        			Main.msgNbr[0] = 4;
        			player.sendMessage(ChatColor.GREEN + "[ProeliumPub] La pub a été créé");
        			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "[Pub] " + ChatColor.AQUA + Main.msgPub);
        			economy.withdrawPlayer(player, 150);

        		}
        		break;
        		
        	case 1:
        		if(Main.msgNbr[1]==0){
        			Main.msg[1] = Main.msgPub;
        			Main.msgNbr[1] = 4;
        			player.sendMessage(ChatColor.GREEN + "[ProeliumPub] La pub a été créé");
        			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "[Pub] " + ChatColor.AQUA + Main.msgPub);
        			economy.withdrawPlayer(player, 150);
        		}
        		break;
        	case 2:
        		if(Main.msgNbr[2]==0){
        			Main.msg[2] = Main.msgPub;
        			Main.msgNbr[2] = 4;
        			player.sendMessage(ChatColor.GREEN + "[ProeliumPub] La pub a été créé");
        			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "[Pub] " + ChatColor.AQUA + Main.msgPub);
        		}
        		break;	
        	case 3:
        		if(Main.msgNbr[3]==0){
        			Main.msg[3] = Main.msgPub;
        			Main.msgNbr[3] = 4;
        			player.sendMessage(ChatColor.GREEN + "[ProeliumPub] La pub a été créé");
        			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "[Pub] " + ChatColor.AQUA + Main.msgPub);
        			economy.withdrawPlayer(player, 150);

        		}
        		break;	
        	case 4:
        		if(Main.msgNbr[4]==0){
        			Main.msg[4] = Main.msgPub;
        			Main.msgNbr[4] = 4;
        			player.sendMessage(ChatColor.GREEN + "[ProeliumPub] La pub a été créé");
        			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "[Pub] " + ChatColor.AQUA + Main.msgPub);
        			economy.withdrawPlayer(player, 150);

        		}
        		break;		
        	default:
                player.sendMessage(ChatColor.RED + "[CoreProelium] Veuillez cliquez dans le menu.");
        	}
        	
        }

    }
}
//Location l0 = new Location(Bukkit.getWorld("ressource"), -431.5, 74, 544.5);
//player.teleport(l0);
//Location l1 = new Location(Bukkit.getWorld("world"), -900.5, 74, 7220.5);
//player.teleport(l1);
