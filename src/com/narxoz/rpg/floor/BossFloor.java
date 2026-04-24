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
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster(bossName, bossHp, bossAtk);
        System.out.println("[Setup] " + bossName + " rises! HP:" + bossHp + " ATK:" + bossAtk);
        System.out.println("[Setup] Party status:");
        for (Hero h : party) System.out.println("  " + h);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 0;
        boolean phase2Announced = false;

        while (boss.isAlive() && anyAlive(party)) {
            round++;
            System.out.println("\n  [Round " + round + "] " + bossName + " HP: " + boss.getHp());

            if (!phase2Announced && boss.getHp() < bossHp / 2) {
                phase2Announced = true;
                System.out.println("  *** " + bossName + " enters PHASE 2! Enraging! ***");
            }

