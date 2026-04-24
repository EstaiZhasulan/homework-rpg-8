package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class BossFloor extends TowerFloor {

    private final String bossName;
    private final int bossHp;
    private final int bossAtk;
    private Monster boss;

    public BossFloor(String bossName, int bossHp, int bossAtk) {
        this.bossName = bossName;
        this.bossHp = bossHp;
        this.bossAtk = bossAtk;
    }

    @Override
    protected String getFloorName() { return bossName + "'s Lair"; }

    @Override
    protected void announce() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         *** BOSS ENCOUNTER ***           ║");
        System.out.println("║  " + padRight(bossName + " awakens!", 40) + " ║");
        System.out.println("╚══════════════════════════════════════════╝");
