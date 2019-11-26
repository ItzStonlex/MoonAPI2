package ru.stonlex.api.bukkit.tag.manager;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.tag.PacketWrapper;
import ru.stonlex.api.bukkit.tag.enums.TeamAction;

public final class TagManager {

    public void setTag(Player player, String teamName, String prefix, String suffix, TeamAction teamAction) {
        PacketWrapper wrapper = null;

        switch (teamAction) {

            case CREATE: 
            case UPDATE: {
                if (teamAction == TeamAction.UPDATE) {
                    wrapper = new PacketWrapper(
                            teamName, prefix, suffix, 1, Lists.newArrayList(player.getName()));

                    wrapper.send();
                }

                wrapper = new PacketWrapper(
                        teamName, prefix, suffix, 0, Lists.newArrayList(player.getName()));
                break;
            }

            case DESTROY: {
                wrapper = new PacketWrapper(
                        teamName, prefix, suffix, 1, Lists.newArrayList(player.getName()));
            }

        }

        wrapper.send();
    }

}

