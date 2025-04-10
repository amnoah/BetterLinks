package better.links.paper;

import better.reload.api.ReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReloadListener implements Listener {

    private final BetterLinksPaper plugin;

    public ReloadListener(BetterLinksPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(ReloadEvent event) {
        plugin.load();
    }
}
