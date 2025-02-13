package better.links.sponge;

import better.links.core.BetterLinks;
import com.google.inject.Inject;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import sharkbyte.configuration.configurate.ConfigurateConfigationFile;
import sharkbyte.configuration.core.ConfigSection;

import java.nio.file.Path;

@Plugin("betterlinks")
public class BetterLinksSponge {

    private static final int B_STATS_ID = 24772;

    private BetterLinks core;

    private final PluginContainer pluginContainer;
    private final Metrics metrics;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDirectory;

    @Inject
    public BetterLinksSponge(PluginContainer pluginContainer, Metrics.Factory metrics) {
        this.pluginContainer = pluginContainer;
        this.metrics = metrics.make(B_STATS_ID);
    }

    @Listener
    public void onServerLoad(final StartingEngineEvent<Server> event) {
        core = new BetterLinks();
    }

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        core = new BetterLinks();
        Sponge.eventManager().registerListeners(pluginContainer, new ReloadListener(this));
        load();
    }

    @Listener
    public void onServerStop(final StoppingEngineEvent<Server> event) {
        metrics.shutdown();
    }

    public void load() {
        ConfigurateConfigationFile file = new ConfigurateConfigationFile("BetterLinksConfig.yml", configDirectory, BetterLinks.class.getResourceAsStream("/config.yml"));
        ConfigSection root = file.load();
        core.load(root);
    }
}