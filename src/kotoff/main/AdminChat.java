package kotoff.main;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implement simple admin chat function
 */
public class AdminChat extends JavaPlugin {
    List<UUID> inAdminChat = new ArrayList<UUID>();

    public void onEnable() {
        getCommand("a").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if(commandSender.hasPermission("ritcraft.adminchat")) {
                    if(strings.length == 0) {
                        if (commandSender instanceof Player) {
                            Player p = ((Player) commandSender);
                            if(!inAdminChat.contains(p.getUniqueId())) {
                                inAdminChat.add(p.getUniqueId());
                                p.sendMessage(ChatColor.GREEN + "You are in admin chat mode");
                            }else{
                                inAdminChat.remove(p.getUniqueId());
                                p.sendMessage(ChatColor.RED + "You are no longer admin chat mode");
                            }
                        }else{
                            commandSender.sendMessage(ChatColor.RED + "Only players can go into admin chat mode");
                        }
                    }else{
                        String message = combineArgs(strings);
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            if(player.hasPermission("CreepCrafter.grantAdministration")) {
                                player.sendMessage(ChatColor.RED + "[ADMIN] " + ChatColor.GOLD + commandSender.getName() + ": " + ChatColor.GRAY + message);
                            }
                        }
                    }
                }else{
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission for this command");
                }
                return true;
            }
        });

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onTalk(AsyncPlayerChatEvent e) {
                if(inAdminChat.contains(e.getPlayer().getUniqueId())) {
                    e.setCancelled(true);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.hasPermission("CreepCrafter.grantAdministration")) {
                            player.sendMessage(ChatColor.YELLOW + "[Admin] " + ChatColor.GOLD + e.getPlayer().getName() + ": " + ChatColor.GRAY + e.getMessage());
                        }
                    }
                }
            }
        },this);
    }

    public String combineArgs(String[] args) {
        String ret = "";
        for(String arg : args) {
            ret+=(arg+" ");
        }
        return ret.trim();
    }
}
