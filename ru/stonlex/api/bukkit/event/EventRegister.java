package ru.stonlex.api.bukkit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import ru.stonlex.api.java.interfaces.Applicable;

@RequiredArgsConstructor
@Getter
public class EventRegister<E extends Event> implements Listener {

    private final Plugin plugin;

    private final Class<E> eventClass;

    private final Applicable<E> eventApplicable;
}
