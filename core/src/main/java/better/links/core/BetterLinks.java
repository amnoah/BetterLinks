package better.links.core;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerServerLinks;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerServerLinks;
import net.kyori.adventure.text.Component;
import sharkbyte.configuration.core.ConfigSection;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetterLinks extends SimplePacketListenerAbstract {

    private final static Pattern TRANSLATE = Pattern.compile("(?i)&[0-9A-FK-ORX]");

    private final List<WrapperCommonServerServerLinks.ServerLink> links = new ArrayList<>();
    private final Set<User> users = new HashSet<>();

    public BetterLinks() {
        PacketEvents.getAPI().getEventManager().registerListeners(this);
    }

    public void load(ConfigSection rootConfig) {
        links.clear();

        Map<Integer, List<WrapperCommonServerServerLinks.ServerLink>> linkMap = new TreeMap<>();

        for (ConfigSection section : rootConfig.getChildren()) {
            int weight = section.getObject(Integer.class, "weight", 0);
            WrapperCommonServerServerLinks.ServerLink link = new WrapperCommonServerServerLinks.ServerLink(
                    Component.text(translateColors(section.getObject(String.class, "name", "No Name!"))),
                    section.getObject(String.class, "link", "")
            );

            if (linkMap.containsKey(weight)) linkMap.get(weight).add(link);
            else {
                List<WrapperCommonServerServerLinks.ServerLink> linkList = new ArrayList<>();
                linkList.add(link);
                linkMap.put(weight, linkList);
            }
        }

        List<WrapperCommonServerServerLinks.ServerLink>[] allLinks = linkMap.values().toArray(new List[]{});
        for (int i = allLinks.length - 1; i  >= 0; i--) links.addAll(allLinks[i]);

        for (User user : users) loadUser(user);
    }

    public void loadUser(User user) {
        user.sendPacket(new WrapperPlayServerServerLinks(links));
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.JOIN_GAME) return;
        users.add(event.getUser());
        loadUser(event.getUser());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        users.remove(event.getUser());
    }

    private static String translateColors(String text) {
        if (text == null) return null;

        Matcher matcher = TRANSLATE.matcher(text);
        StringBuffer output = new StringBuffer();

        while (matcher.find()) {
            String match = matcher.group();
            String replacement = "§" + match.substring(1);
            matcher.appendReplacement(output, replacement);
        }

        matcher.appendTail(output);
        return output.toString();
    }
}