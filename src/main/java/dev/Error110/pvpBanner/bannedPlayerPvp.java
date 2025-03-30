package dev.Error110.pvpBanner;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import com.palmergames.bukkit.towny.object.TownyWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class bannedPlayerPvp implements Listener {
    private final PvpBanner plugin;

    public bannedPlayerPvp(PvpBanner plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPvp(TownyPlayerDamagePlayerEvent event)
    {
        Player attacker = event.getAttackingPlayer();
        Player victim = event.getVictimPlayer();
        if (event.isCancelled()) {
            attacker.sendMessage("event was canceled before thing");
            victim.sendMessage("event was cancled before thing");
            return;
        }
        TownyWorld world = TownyAPI.getInstance().getTownyWorld(event.getVictimPlayer().getWorld().getName());
        TownyUniverse universe = TownyUniverse.getInstance();

        for (UUID bannedUUID : plugin.getBannedList())
        {
            plugin.getLogger().severe("banned uuid " + bannedUUID);
            if(victim.getUniqueId().equals(bannedUUID)) {
                event.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "this person is pvp banned as hes too fucking good at pvp for this server but fishy doesnt want to disable pvp in wild but is willing to make a custom plugin to ban someone from pvp");
            } else {
                plugin.getLogger().severe("banned uuid " + bannedUUID + "\n" + "towny uuid " + victim.getUniqueId());
            }
            if (attacker.getUniqueId().equals(bannedUUID)) {
                event.setCancelled(true);
            } else {
                plugin.getLogger().severe("banned uuid " + bannedUUID + "\n" + "towny uuid " + attacker.getUniqueId());
            }
        }
    }
}
