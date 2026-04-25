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

            for (Hero h : party) {
                if (!h.isAlive()) continue;
                h.onTurnStart();

                if (!h.canAct()) {
                    h.onTurnEnd();
                    continue;
                }

                int dmg = h.getEffectiveDamage();
                boss.takeDamage(dmg);
                System.out.println("  " + h.getName() + " hits " + bossName
                        + " for " + dmg + ". (Boss HP: " + boss.getHp() + ")");
                h.onTurnEnd();
                if (!boss.isAlive()) break;
            }

            if (!boss.isAlive()) break;


            Hero target = firstAliveHero(party);
            if (target == null) break;

            int rawDmg = boss.getAttackPower();
            int hpBefore = target.getHp();
            target.takeDamage(rawDmg);
            int actual = hpBefore - target.getHp();
            totalDamage += actual;
            System.out.println("  " + bossName + " strikes " + target.getName()
                    + " for " + actual + " dmg. HP: " + target.getHp());

            if (phase2Announced && round % 2 == 0) {
                for (Hero h : party) {
                    if (h.isAlive() && !(h.getState() instanceof BerserkState)) {
                        System.out.println("  " + bossName + "'s roar fills " + h.getName() + " with rage!");
                        h.setState(new BerserkState(2));
                    }
                }
            }

            if (round % 3 == 0 && target.isAlive()) {
                System.out.println("  " + bossName + "'s shockwave stuns " + target.getName() + "!");
                target.setState(new StunnedState());
            }

            if (round % 4 == 0) {
                for (Hero h : party) {
                    if (h.isAlive() && !(h.getState() instanceof PoisonedState)) {
                        System.out.println("  " + bossName + "'s venom splashes " + h.getName() + "!");
                        h.setState(new PoisonedState(3, 2));
                    }
                }
            }
        }

        boolean cleared = !boss.isAlive();
        String summary = cleared
                ? bossName + " has been slain after " + round + " rounds!"
                : "The party fell before " + bossName + "...";
        System.out.println("\n  [Result] " + summary);
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] Boss drops legendary treasure! Full heal for all survivors!");
        for (Hero h : party) {
            if (h.isAlive()) {
                h.heal(h.getMaxHp());
                System.out.println("  " + h.getName() + " fully restored to " + h.getHp() + " HP!");
            }
        }
    }


    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] " + bossName + "'s death shockwave dispels all status effects!");
        for (Hero h : party) {
            if (h.isAlive()) {
                h.setState(new com.narxoz.rpg.state.NormalState());
            }
        }
    }

    private boolean anyAlive(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }

    private Hero firstAliveHero(List<Hero> party) {
        return party.stream().filter(Hero::isAlive).findFirst().orElse(null);
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}

