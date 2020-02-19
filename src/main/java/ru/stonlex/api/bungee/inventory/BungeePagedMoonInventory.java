package ru.stonlex.api.bungee.inventory;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;
import ru.stonlex.api.bungee.inventory.item.BungeeMaterial;
import ru.stonlex.api.bungee.utility.BungeeItemUtil;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public abstract class BungeePagedMoonInventory extends BungeeMoonInventory {

    private int page, pagesCount;

    private ProxiedPlayer viewerPlayer;

    private final Map<BungeeItemStack, Clickable<ProxiedPlayer>> pagedButtonMap;
    private final List<Integer> slotsList;

    /**
     * Инициализация инвентаря
     *
     * @param inventoryName  - Имя инвентаря
     * @param inventoryTitle - Заголовок инвентаря
     * @param inventoryRows  - Количество строк в инвентаре
     */
    public BungeePagedMoonInventory(String inventoryName, String inventoryTitle, int inventoryRows) {
        this(0, inventoryName, inventoryTitle, inventoryRows);
    }

    /**
     * Инициализация инвентаря
     *
     * @param page           - Страница инвентаря
     * @param inventoryName  - Имя инвентаря
     * @param inventoryTitle - Заголовок инвентаря
     * @param inventoryRows  - Количество строк в инвентаре
     */
    private BungeePagedMoonInventory(int page, String inventoryName, String inventoryTitle, int inventoryRows) {
        super(inventoryName, inventoryTitle.concat(" | " + (page + 1)), inventoryRows);

        this.page = page;

        this.pagedButtonMap = initializeItems();
        this.slotsList = initializeSlots();

        this.pagesCount = pagedButtonMap.size() / slotsList.size();
    }

    /**
     * Инициализация предметов в инвентарь
     */
    public abstract Map<BungeeItemStack, Clickable<ProxiedPlayer>> initializeItems();

    /**
     * Инициализация слотов, на которых будут стоять предметы
     */
    public abstract List<Integer> initializeSlots();


    /**
     * На страницу назад
     */
    private void backward(ProxiedPlayer player) {
        if (page - 1 < 0) {
            throw new RuntimeException("Page cannot be < 0");
        }

        this.page--;

        updateInventory(player);
    }

    /**
     * На страницу вперед
     */
    private void forward(ProxiedPlayer player) {
        if (page - 1 < 0) {
            throw new RuntimeException("Page cannot be > max pages count");
        }

        this.page++;

        updateInventory(player);
    }

    /**
     * Построение страничного инвентаря
     */
    private void buildPage(ProxiedPlayer player) {
        this.viewerPlayer = player;

        if (page < pagesCount) {
            setItem(inventorySize - 4, BungeeItemUtil.getItemStack(BungeeMaterial.ARROW, "§eВперед"),
                    player1 -> forward(player));
        }

        if (page > 0) {
            setItem(inventorySize - 6, BungeeItemUtil.getItemStack(BungeeMaterial.ARROW, "§eНазад"),
                    player1 -> backward(player));
        }


        for (int i = 0; i < slotsList.size(); ++i) {
            int index = page * slotsList.size() + i;

            if (pagedButtonMap.size() <= index) {
                return;
            }

            int slot = slotsList.get(i);

            Map.Entry<BungeeItemStack, Clickable<ProxiedPlayer>> itemEntry = new ArrayList<>(pagedButtonMap.entrySet()).get(index);

            BungeeItemStack itemStack = itemEntry.getKey();

            this.setItem(slot, itemStack, itemEntry.getValue());
        }
    }


    /**
     * Открытие инвентаря игроку
     */
    @Override
    public void openInventory(ProxiedPlayer player) {
        buildPage(player);

        super.openInventory(player);
    }

    /**
     * Обновление инвентаря игроку
     */
    @Override
    public void updateInventory(ProxiedPlayer player) {
        super.updateInventory(player, () -> buildPage(player));
    }

}
