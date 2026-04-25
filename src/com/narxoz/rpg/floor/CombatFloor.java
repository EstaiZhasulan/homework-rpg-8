package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final List<Monster> monsters = new ArrayList<>();
    private final String[] monsterSpecs;

    public CombatFloor(String floorName, String... monsterSpecs) {
        this.floorName = floorName;
        this.monsterSpecs = monsterSpecs;
    }
    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║  ⚔  COMBAT: " + floorName);
        System.out.println("╚══════════════════════════════╝");
    }