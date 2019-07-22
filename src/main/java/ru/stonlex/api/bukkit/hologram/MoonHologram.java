package ru.stonlex.api.bukkit.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.List;

public interface MoonHologram {

    List<String> getLines();

    Location getLocation();

    Clickable<Player> getClickAction();

    int getLineCount();

    String getLine(int index);

    void addLine(String line);

    void modifyLine(int index, String line);

    void spawn();

    void addReceiver(Player player);

    void remove();

    void removeReceiver(Player player);

    void setLocation(Location location);

    void setClickAction(Clickable<Player> clickAction);

    void refreshHologram();

}
