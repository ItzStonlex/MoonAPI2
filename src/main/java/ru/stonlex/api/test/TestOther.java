package ru.stonlex.api.test;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.modules.protocol.entity.animation.FakeEntityAnimation;
import ru.stonlex.api.bukkit.modules.protocol.entity.impl.FakePlayer;
import ru.stonlex.api.bukkit.particle.ParticleEffect;
import ru.stonlex.api.java.JavaMoonAPI;
import ru.stonlex.api.java.mail.MailSender;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TestOther {

    private final MoonAPI moonAPI;

    /**
     * В данном методе мы создаем Sidebar, или же Scoreboard, или
     * даже просто Board, кому как удобнее это называть :)
     */
    public void setSidebar(Player player) {

        //Создание и инициализация нужных переменн
        ChatColor[] displayNameColors = new ChatColor[]{ChatColor.AQUA, ChatColor.BLUE};
        ThreadLocalRandom random = ThreadLocalRandom.current();

        //Непосредственно создание самого скорборда
        MoonAPI.getSidebarManager().newBuilder()
                .setDisplayName("§b§lMoonStudio")

                .setLine(2, "§aТестовая строка 1")
                .setLine(1, "§aТестовая строка 2")
                .setLine(0, "§aТестовая строка 3")

                .newUpdater(update -> {
                    ChatColor chatColor = displayNameColors[random.nextInt(displayNameColors.length)];

                    update.setDisplayName(chatColor + "MoonStudio");
                    update.setLine(2, chatColor + "Тестовая строка 1");
                }, 5)

                .showToPlayer(player);
    }

    /**
     * Отправить сообщение на Mail-почту
     */
    public void sendMailMessage() {
        //Получение отправителя
        MailSender mailSender = JavaMoonAPI.getMailManager().getMailSender("itzstonlex@bk.ru", "***");

        //Отправка сообщения на другой EMail-адрес
        mailSender.sendMessage("Тема сообщения", "Сообщение", "moonstudio@bk.ru");
    }

    /**
     * Здесь мы регистрируем лишь 1 ивент не используя лишних
     * листенеров, PluginManager, и прочего...
     *
     * Также, дженерик EventApplicable возвращает тот ивент,
     * чей класс был указан в конструкторе самого билдера.
     */
    public void registerEvent() {
        MoonAPI.getEventManager().newBuilder(BlockBreakEvent.class)
                .setPlugin(moonAPI)

                .setEventApplicable(event -> {
                    Block block = event.getBlock();

                    block.setType(Material.AIR);
                }).build();
    }

    /**
     * В этом незамысловатом методе мы очень просто и легко создаем
     * голограмму, которая может быть видима только 1 игроку, а может и всем.
     */
    public void spawnHologram(Player receiver, Location location, boolean showForAll) {
        MoonAPI.getHologramManager().createHologram("testHologram", location, hologram -> {
            hologram.addLine("123"); //Добавить линию
            hologram.addLine("§c321"); //Добавить линию
            hologram.addLine("Автор API - ItzStonlex"); //Добавить линию
            hologram.addLine("VK: vk.com/itzstonlex"); //Добавить линию

            hologram.setClickAction(player -> { //Действие при клике на голограмму
                player.sendMessage("Test"); //Отправитб сообщение игроку

                hologram.modifyLine(0, "321"); //Изменить 1 строку на другую
            });

            //Если значение = true, то показывам голограмму для всех
            if (showForAll) {
                hologram.spawn();
                return;
            }

            hologram.addReceiver(receiver); //Отправляем голграмму игроку
        });
    }

    /**
     * В данном методе мы создаем свои GUI в пару строк,
     * настраивая сразу действия при клике на предмет,
     * выставляя сами предметы, устанавливая инвентарю свои
     * Title, размер (или же количество строк, как это сделано на примере (rows)),
     * и устанавливая свое название, по которому инвентарь
     * будет кешироваться в InventoryManager
     */
    public void openInventory(Player player) {
        MoonAPI.getInventoryManager().createInventory("testInventory", "Test Inventory", 3, inventory -> {
            inventory.setItem(1, ItemUtil.getItemStack(Material.STONE), player1 -> player1.sendMessage("Ты кликнул по камню на 1 слоту"));

            inventory.openInventory(player);
        });
    }

    /**
     * Регистрация команд без вывода в plugin.yml,
     * Где Вы еще такого могли увидеть?!
     */
    public void registerCommands() {
        MoonAPI.getCommandManager()
                .registerCommand(moonAPI, new TestCommand(), "test", "testing", "moontest");
    }

    /**
     * В следующих двух методах разбирается некий
     * пинг серверов, с помощью которого можно выловить
     * любую информацию, указав лишь ip и его port.
     */
    public int getLastCraftPlayersCount() {
        return MoonAPI.getMessagingManager()
                .getServer("play.last-craft.net", 25565).getPlayersOnline();
    }

    public int getGlobalOnline() {
        return MoonAPI.getMessagingManager()
                .getServer("127.0.0.1", 25565).getPlayersOnline();
    }

    /**
     * Данный метод отправляет игроку сообщение, даже если
     * он находится на другом сервере.
     *
     * Предусмотрено и то, что он может находится и на данном сервере,
     * и если такое будет, то ему тоже отправится указанное сообщение
     */
    public void sendMessage(Player player, String text) {
        MoonAPI.getMessagingManager().sendMessage(player.getName(),
                                                     ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * MessagingManager - очень интересный и уникальный класс, которому
     * подвласно многое, и даже больше, чем кажется.
     *
     * Теперь без помощи самого BungeeCord и без его API,
     * мы можем спокойно телепортировать игроков по разным
     * серверм Proxy
     */
    public void redirectPlayer(Player player, String serverName) {
        MoonAPI.getMessagingManager().redirectPlayer(player, serverName);
    }

    /**
     * ParticleEffect - Удобный enum-класс с удобной работой
     *  с партиклами и эффектами, с их редактированием и видоизменением.
     */
    public void spawnCircle(Plugin plugin, Player player, Location location) {
        new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                t += Math.PI / 16;

                double x = 2 * Math.cos(t);
                double z = 2 * Math.sin(t);

                ParticleEffect.SPELL.display(0, 0, 0, 0, 3,
                        location.clone().add(x, 0, z), player);
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    /**
     * Создание NPC или любого другого фейкового моба
     * при помощи пакетов
     */
    public void createNPC(Player receiver, Location location) {
        FakePlayer fakePlayer = new FakePlayer("Ник скина для NPC", location);

        fakePlayer.playAnimation(FakeEntityAnimation.TAKE_DAMAGE); //Воспроиграть анимацию (Махание рукой, вставание с кровати, и т.д.)
        fakePlayer.look(receiver, receiver.getLocation()); //Повернуть голову и тело FakePlayer в сторону того, кому отправляем NPC
        fakePlayer.setGlowingColor(ChatColor.AQUA); //Создать подсветку вокруг FakePlayer определенного цвета
        fakePlayer.setSneaking(true); //Встать на SHIFT

        fakePlayer.setClickAction(player -> { //Действие при клике на FakePlayer
            player.sendMessage("Test"); //Отправить сообщение игроку, который кликнул по FakePlayer

            fakePlayer.removeReceiver(player); //Скрыть FakePlayer от игрока, который кликнул по нему
        });

        fakePlayer.addReceiver(receiver); //Отправить FakePlayer игроку
    }

}
