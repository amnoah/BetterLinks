package better.links.paper;

import better.links.core.BetterLinks;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import sharkbyte.configuration.core.ConfigSection;
import sharkbyte.configuration.spigot.SpigotConfigurationFile;

public class BetterLinksPaper extends JavaPlugin {

    private static final int B_STATS_ID = 24771;

    private BetterLinks core;
    private Metrics metrics;

    @Override
    public void onEnable() {
        core = new BetterLinks();
        metrics = new Metrics(this, B_STATS_ID);

        if (getServer().getPluginManager().getPlugin("BetterReload") != null) {
            getServer().getPluginManager().registerEvents(new ReloadListener(this), this);
        }

        load();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    public void load() {
        SpigotConfigurationFile file = new SpigotConfigurationFile(this, "config.yml", BetterLinks.class.getResourceAsStream("/config.yml"));
        ConfigSection root = file.load();
        core.load(root);
    }
}
