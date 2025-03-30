package dev.Error110.pvpBanner;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class PvpBanner extends JavaPlugin {

    public List<UUID> bannedList = new ArrayList<>() {};
    Map<Integer, List<String>> tabCompletions = new HashMap<>();

    @Override
    public void onEnable() {
        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File file = new File(getDataFolder()+ File.separator+"UUIDs.yml");
        if (file.exists())
        {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                String uuidString = config.getString(key);
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    bannedList.add(uuid);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("something went wrong getting uuid from file");
                }
            }
        }

        getServer().getPluginManager().registerEvents(new bannedPlayerPvp(this), this);
        addtomap();
        AddonCommand townycommand = new AddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "pvpban", new townyAdminBanCommand(this));
        townycommand.setTabCompletions(tabCompletions);
        TownyCommandAddonAPI.addSubCommand(townycommand);
    }
    @Override
    public void onDisable() {
        int i = 0;
        File file = new File(getDataFolder()+ File.separator+"UUIDs.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) { file.delete();}
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (UUID banneduuid : bannedList)
        {
            config.set("uuid" + i, banneduuid.toString());
            i++;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addtomap() {
        tabCompletions.put(0, getAddOrRemoveOptions());
        tabCompletions.put(1, residentTab());
    }

    private List<String> getAddOrRemoveOptions() {
        List<String> options = new ArrayList<>();
        options.add("add");
        options.add("remove");
        return options;
    }

    private List<String> residentTab() {
        List<String> completions = new ArrayList<>();
        TownyUniverse universe = TownyUniverse.getInstance();
        Collection<Resident> residents = universe.getResidents();
        for (Resident resident : residents) {
            completions.add(resident.getName());
        }

        return completions;
    }

    public List<UUID> getBannedList() {
        return bannedList;
    }

    public boolean addBannedList(UUID value) {
        if(bannedList.add(value)) return true;
        return false;
    }

    public boolean removeBannedList(UUID value) {
        if(bannedList.remove(value)) return true;
        return false;
    }
}
