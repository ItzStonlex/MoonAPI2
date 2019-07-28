package ru.stonlex.api.test;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.particle.ParticleEffect;
import ru.stonlex.api.java.JavaMoonAPI;
import ru.stonlex.api.java.mail.MailSender;
import ru.stonlex.api.java.schedulers.MoonTask;
import ru.stonlex.api.bukkit.utility.ItemUtil;
import ru.stonlex.api.java.utility.DateUtil;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TestOther {

    private final MoonAPI moonAPI;

    /**
     * В данном методе мы создаем Sidebar, или же Scoreboard, или
     * даже просто Board, кому как удобнее это называть :)
     *
     * Все происходит максимально просто и удобно:
     *  При помощи основного класса MoonSidebar мы вызываем новый
     *  Builder, через который строим весь скорборд, устанавливая ему
     *  строки, DisplayName, и даже добавляя Updater (можно несколько),
     *  который будет обновлять то, и так, как мы того сами хотим.
     */
    public void setSidebar(Player player) {

        //Создание и инициализация нужных переменн
        ChatColor[] displayNameColors = new ChatColor[]{ChatColor.AQUA, ChatColor.BLUE};
        ThreadLocalRandom random = ThreadLocalRandom.current();

        //Непосредтсвенно создание самого скорборда
        MoonAPI.getSidebarManager().newBuilder()
                .setShowPlayer(player)

                .setDisplayName("§6§lMOONSTUDIO")

                .addLine(4, "&7".concat(DateUtil.getDate("Дата: dd/MM/yy, Время: HH:mm")) )
                .addLine(3, "")
                .addLine(2, "&fАвтор MoonAPI: &cItzStonlex")
                .addLine(1, "")
                .addLine(0, "&ewww.moonstudio.space")

                .addUpdater(20, update -> {
                    update.setDisplayName(displayNameColors[random.nextInt(displayNameColors.length)] + "§lMOONSTUDIO");

                    update.addLine(4, "&7".concat(DateUtil.getDate("Дата: dd/MM/yy, Время: HH:mm")) );
                })

                .build().show();
    }

    /**
     * Отправить сообщение на Mail-почту
     */
    public void sendMailMessage() {
        //Получение отправителя
        MailSender mailSender = JavaMoonAPI.getMailManager().getMailSender("itzstonlex@bk.ru",
                "itzstonlex@bk.ru", "*****",
                "smtp.mail.ru");

        //Отправка сообщения на другой EMail-адрес
        mailSender.sendMessage("Тема сообщения", "Сообщение",
                "moonstudio@bk.ru");
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
    public void spawnHologram(Player player, Location location, boolean showForAll) {
        MoonAPI.getHologramManager().createHologram("testHologram", location, hologram -> {
            hologram.addLine("123");
            hologram.addLine("§c321");
            hologram.addLine("Автор API - ItzStonlex");
            hologram.addLine("VK: vk.com/itzstonlex");

            if (showForAll) {
                hologram.spawn();
                return;
            }

            hologram.addReceiver(player);
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
     * В данном методе предусмотрены сразу две фишки MoonAPI 2.0:
     *
     * 1. MoonTask - Разработчик сделал свои собственные таски
     *  (тот же BukkitRunnable), которые в разы удобнее баккитовских.
     *
     * 2. ParticleEffect - Удобный enum-класс с удобной работой
     *  с партиклами и эффектами, с их редактированием и видоизменением.
     */
    public void spawnCircle(Player player, Location location) {
        new MoonTask() {
            double t = 0;

            @Override
            public void run() {
                t += Math.PI / 16;

                double x = 2 * Math.cos(t);
                double z = 2 * Math.sin(t);

                ParticleEffect.SPELL.display(0, 0, 0, 0, 3,
                        location.clone().add(x, 0, z), player);
            }
        }.scheduleTimer(0, 1, TimeUnit.SECONDS);
    }

}
