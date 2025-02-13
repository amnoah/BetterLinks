package better.links.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;

public class ReloadListener {

    private final BetterLinksSponge plugin;

    public ReloadListener(BetterLinksSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onReload(final RefreshGameEvent event) {
        plugin.load();
    }
}
