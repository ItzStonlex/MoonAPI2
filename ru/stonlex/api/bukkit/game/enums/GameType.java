package ru.stonlex.api.bukkit.game.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameType {

    SOLO("Solo", 1),
    DOUBLES("Doubles", 2),
    TEAM("Team", 4);

    private final String type;
    private final int playersInTeamCount;

}
