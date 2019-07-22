package ru.stonlex.api.bukkit.board.line;

import com.google.common.base.Splitter;
import ru.stonlex.api.bukkit.board.MoonSidebar;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

public final class SidebarLine {

    private final MoonSidebar board;
    private final int index;
    private String text;
    private Team team;


    /**
     * Инициализация строки
     */
    public SidebarLine(MoonSidebar board, int index, String text) {
        this.board = board;
        this.index = index;
        this.text = ChatColor.translateAlternateColorCodes('&', text.isEmpty() ? ChatColor.values()[index].toString() : text);
        this.team = board.getScoreboard().registerNewTeam(index + "");
        prepareText();
    }



    /**
     * Установка текста на строку
     */
    public void setText(String text) {
        this.text = ChatColor.translateAlternateColorCodes('&', text.isEmpty() ? ChatColor.values()[index].toString() : text);
        prepareText();
    }

    /**
     * Инициализация строки и ее преобразование
     */
    private void prepareText() {
        if (isModifiable()) {
            return;
        }
        Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
        String prefix = iterator.next();
        String result = ChatColor.values()[index].toString();

        team.setPrefix(prefix);

        team.removeEntry(result);

        team.addEntry(result);

        if (text.length() > 16) {
            String prefixColor = ChatColor.getLastColors(prefix);
            String suffix = iterator.next();

            if (prefix.endsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
                prefix = prefix.substring(0, prefix.length() - 1);
                team.setPrefix(prefix);
                prefixColor = ChatColor.getByChar(suffix.charAt(0)).toString();
                suffix = suffix.substring(1);
            }

            if (prefixColor == null)
                prefixColor = "";

            if (suffix.length() > 16) {
                suffix = suffix.substring(0, (13 - prefixColor.length()));
            }

            team.setSuffix((prefixColor.equals("") ? ChatColor.RESET : prefixColor) + suffix);

        } else team.setSuffix(ChatColor.RESET.toString());

        board.getObjective().getScore(result).setScore(index);
    }

    /**
     * Существует ли строка в Scoreboard
     */
    private boolean isModifiable() {
        return board.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null
                || board.getScoreboard().getTeam(index + "") == null;
    }

    /**
     * Скрыть строку
     */
    public void hide() {
        this.board.getScoreboard().resetScores(ChatColor.values()[index].toString());
        this.team.unregister();
    }

    /**
     * Показать строку
     */
    public void show() {
        this.team = board.getScoreboard().registerNewTeam(index + "");
        prepareText();
    }

}
