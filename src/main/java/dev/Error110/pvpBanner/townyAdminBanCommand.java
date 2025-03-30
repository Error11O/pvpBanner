package dev.Error110.pvpBanner;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.permissions.PermissionNodes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class townyAdminBanCommand implements CommandExecutor{
    private final PvpBanner plugin;

    public townyAdminBanCommand(PvpBanner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;

        if (player.hasPermission(PermissionNodes.TOWNY_ADMIN.getNode()) | player.isOp()) {
            switch (args[0])
            {
                case "add":
                    if (args.length < 1) {
                        return true;
                    }
                    TownyUniverse universe = TownyUniverse.getInstance();
                    String residentName = args[1];
                    Resident resident = universe.getResident(residentName);
                    UUID uuid = resident.getUUID();
                    plugin.addBannedList(uuid);
                    sender.sendMessage(ChatColor.GREEN + "Player " + residentName + " is now banned from pvp.");
                    break;
                case "remove":
                    TownyUniverse removeUniverse = TownyUniverse.getInstance();
                    String removeResidentName = args[1];
                    Resident removeResident = removeUniverse.getResident(removeResidentName);
                    UUID removeUuid = removeResident.getUUID();
                    plugin.removeBannedList(removeUuid);
                    sender.sendMessage(ChatColor.GREEN + "Player " + removeResidentName + " is now unbanned from pvp.");
                    break;
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "no perms");
        }
        return true;
    }
}
