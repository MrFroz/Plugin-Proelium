package fr.froz.proelium;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static ProtocolManager protocolManager;
    public static Inventory menu = Bukkit.createInventory(null, 9, "Menu");
    Logger log = getLogger();

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

    public Inventory getMenu() {
        return menu;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onEnable() {
        Listener l = new PluginListener();
        PluginManager pm = getServer().getPluginManager();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        pm.registerEvents(l, this);


        log.info("[ProeliumCore] Loaded");

    }

    public boolean getInfoWarn(CommandSender p) {
        //SELECT EXISTS(SELECT 1 FROM warn WHERE pseudo="MrFjz");

        return false;

    }

    public void printWarnHelp(CommandSender p) {
        p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Aide : Warn");
        p.sendMessage(ChatColor.AQUA + "/warn help : Aide ");
        p.sendMessage(ChatColor.AQUA + "/warn list [Speudo] : Afficher les Warns ");
        p.sendMessage(ChatColor.AQUA + "/warn add [Speudo] [warn] : Ajouter Warn ");
        p.sendMessage(ChatColor.AQUA + "/warn remove [Speudo] [warn] : Enlever Warn ");


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warn")) {
            if (args.length == 0) {
                printWarnHelp(sender);
            } else {
                if (args[0].equals("help")) {
                    printWarnHelp(sender);
                } else if (args[0].equals("add") && args.length > 2) {

                    if (sender.getServer().getPlayer(args[1]) != null) {
                        File userdata = new File(this.getDataFolder(), File.separator + "warn");
                        File f = new File(userdata, File.separator + args[1] + ".yml");
                        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

                        if (!f.exists()) {
                            try {

                                playerData.createSection("warn");
                                playerData.set("warn.score", args[2]);
                                playerData.save(f);


                                sender.sendMessage(ChatColor.RED + "[ProeliumWarn] Le warn a �t� cr��");

                            } catch (IOException e) {
                                log.info("[ProeliumCore] error while create data: " + e.toString());

                            }
                        } else {
                            String warnscore = (String) playerData.get("warn.score");
                            playerData.set("warn.score", Integer.toString(Integer.parseInt(warnscore) + Integer.parseInt(args[2])));
                            try {
                                playerData.save(f);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                log.info("[ProeliumCore] error while create data: " + e.toString());
                            }
                            sender.sendMessage(ChatColor.RED + "[ProeliumWarn] Le warn a �t� ajout�");

                        }
                    } else {

                        sender.sendMessage(ChatColor.RED + "[ProeliumWarn] Le joueur n'est pas en ligne");
                    }

                    //here
                } else if (args[0].equals("list") && args.length > 1) {
                    File userdata = new File(this.getDataFolder(), File.separator + "warn");
                    File f = new File(userdata, File.separator + args[1] + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
                    if ((!f.exists()) || (playerData.get("warn.score").equals("0"))) {
                        sender.sendMessage(ChatColor.GREEN + "[ProeliumWarn] Le joueur n'a aucun warn");
                    } else {
                        sender.sendMessage(ChatColor.RED + "[ProeliumWarn] Le joueur a " + ChatColor.WHITE + playerData.get("warn.score") + ChatColor.RED + " warn");

                    }


                } else if (args[0].equals("remove") && args.length > 2) {
                    File userdata = new File(this.getDataFolder(), File.separator + "warn");
                    File f = new File(userdata, File.separator + args[1] + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
                    if ((!f.exists()) || (playerData.get("warn.score").equals("0"))) {
                        sender.sendMessage(ChatColor.GREEN + "[ProeliumWarn] Le joueur n'a aucun warn");
                    } else {
                        int tr = Integer.parseInt(args[2]);
                        int result = Integer.parseInt((String) playerData.get("warn.score")) - tr;
                        if (result < 0) {
                            playerData.set("warn.score", "0");
                            try {
                                playerData.save(f);
                                sender.sendMessage(ChatColor.GREEN + "[ProeliumWarn] Vous avez retirez les warns");

                            } catch (IOException e) {
                                log.info("[ProeliumCore] error while edit data: " + e.toString());
                            }


                        } else {
                            playerData.set("warn.score", Integer.toString(result));
                            sender.sendMessage(ChatColor.GREEN + "[ProeliumWarn] Vous avez retirez les warns");

                            try {
                                playerData.save(f);
                            } catch (IOException e) {
                                log.info("[ProeliumCore] error while edit data: " + e.toString());
                            }

                        }

                    }
                } else {
                    printWarnHelp(sender);

                }
            }


        } else if (cmd.getName().equalsIgnoreCase("help") || cmd.getName().equalsIgnoreCase("?")) {
            if (sender.hasPermission("proelium.admin")) {
                if (args.length != 0) {
                    sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "+ arg");

                    Bukkit.getServer().dispatchCommand(sender, "bukkit:? " + args[0]);
                } else {
                    sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "+ arg2");

                    Bukkit.getServer().dispatchCommand(sender, "bukkit:?");

                }
            } else {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "--------Aide--------");
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "/spawn" + ChatColor.WHITE + "- Aller au spawn.");
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "/menu" + ChatColor.WHITE + "- Navigation.");
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "--------------------");

            }
        } else if (cmd.getName().equalsIgnoreCase("menu")) {
            createDisplay(Material.DIAMOND_PICKAXE, menu, 0, ChatColor.BOLD + "" + ChatColor.BLUE + "Ressources", ChatColor.GRAY + "Minage !");
            createDisplay(Material.NETHERRACK,menu,1, ChatColor.BOLD + "" + ChatColor.RED + "Nether", ChatColor.GRAY + "Nether");
            createDisplay(Material.ENDER_STONE,menu,2, ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "End", ChatColor.GRAY + "Dragon et village ...");
            createDisplay(Material.SAND, menu, 4, ChatColor.BOLD + "" + ChatColor.YELLOW + "Canyon", ChatColor.GRAY + "Il fait chaud ...");
            createDisplay(Material.SEA_LANTERN,menu,5, ChatColor.BOLD + "" + ChatColor.BLUE + "FallWiden", ChatColor.RED + "Ville moderne !");
            Player p = (Player) sender;
            p.openInventory(menu);

        }
        return true;
    }

}
//plainte
//warm andmin avertisement