package better.links.spigot;

import better.reload.api.ReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReloadListener implements Listener {

    private final BetterLinksSpigot plugin;

    public ReloadListener(BetterLinksSpigot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(ReloadEvent event) {
        plugin.load();
    }
}
