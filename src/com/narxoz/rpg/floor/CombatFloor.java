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

    @Override
    protected void setup(List<Hero> party) {
        monsters.clear();
        for (String spec : monsterSpecs) {
            String[] parts = spec.split(":");
            monsters.add(new Monster(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }
        System.out.println("[Setup] Monsters appear: " + monsterNames());
        System.out.println("[Setup] Party: " + partyStatus(party));
    }

    Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 0;

        while (monstersAlive() && anyAlive(party)) {
            round++;
            System.out.println("\n  [Round " + round + "]");
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();

                if (!hero.canAct()) {
                    hero.onTurnEnd();
                    continue;
                }

                Monster target = firstAlive();
                if (target == null) break;
                int dmg = hero.getEffectiveDamage();
                target.takeDamage(dmg);
                System.out.println("  " + hero.getName() + " attacks " + target.getName()
                        + " for " + dmg + " dmg. (Monster HP: " + target.getHp() + ")");
                if (!target.isAlive()) {
                    System.out.println("  " + target.getName() + " is defeated!");
                }
                hero.onTurnEnd();
            }

            for (Monster m : monsters) {
                if (!m.isAlive()) continue;
                Hero target = firstAliveHero(party);
                if (target == null) break;

                int rawDmg = m.getAttackPower();
                int hpBefore = target.getHp();
                target.takeDamage(rawDmg);
                int actual = hpBefore - target.getHp();
                totalDamage += actual;
                System.out.println("  " + m.getName() + " attacks " + target.getName()
                        + " for " + actual + " dmg. (Hero HP: " + target.getHp() + ")");


