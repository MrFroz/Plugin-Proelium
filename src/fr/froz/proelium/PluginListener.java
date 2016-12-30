package fr.froz.proelium;


import java.awt.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.ChatColor;
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
                    player.sendMessage("Zbeub");


            }
        }

    }
}
//Location l0 = new Location(Bukkit.getWorld("ressource"), -431.5, 74, 544.5);
//player.teleport(l0);
//Location l1 = new Location(Bukkit.getWorld("world"), -900.5, 74, 7220.5);
//player.teleport(l1);
