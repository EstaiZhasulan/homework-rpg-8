package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

/**
 * Floor type 3: Trap Floor
 * A gauntlet of traps dealing raw damage and applying status effects.
 * Heroes cannot fight back — survival depends on their states.
 *
 * Hook override:
 *   - shouldAwardLoot() returns true only if party survived without any deaths
 */
public class TrapFloor extends TowerFloor {

    private final String floorName;
    private final int[] trapDamages;

    public TrapFloor(String floorName, int... trapDamages) {
        this.floorName = floorName;
        this.trapDamages = trapDamages;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n⚠  TRAP GAUNTLET: " + floorName + "  ⚠");
        System.out.println("Avoid the traps — or suffer the consequences!");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] " + trapDamages.length + " traps detected ahead. Party braces.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int deathsBefore = (int) party.stream().filter(h -> !h.isAlive()).count();

        for (int i = 0; i < trapDamages.length; i++) {
            System.out.println("\n  [Trap " + (i + 1) + "] Deals " + trapDamages[i] + " raw damage!");
            for (Hero h : party) {
                if (!h.isAlive()) continue;
                h.applyRawDamage(trapDamages[i]);
                totalDamage += trapDamages[i];
                System.out.println("  " + h.getName() + " takes " + trapDamages[i] + " trap damage. HP: " + h.getHp());

                // Trap effects
                if (i % 3 == 1 && h.isAlive()) {
                    System.out.println("  Poisoned trap springs on " + h.getName() + "!");
                    h.setState(new PoisonedState(5, 2));
                }
                if (i % 4 == 3 && h.isAlive()) {
                    System.out.println("  Shocking trap stuns " + h.getName() + "!");
                    h.setState(new StunnedState());
                }
            }
        }

        int deathsAfter = (int) party.stream().filter(h -> !h.isAlive()).count();
        boolean cleared = party.stream().anyMatch(Hero::isAlive);
        String summary = "Trap gauntlet dealt " + totalDamage + " total damage. Deaths: "
                + (deathsAfter - deathsBefore);
        System.out.println("  [Result] " + summary);
        return new FloorResult(cleared, totalDamage, summary);
    }

    /**
     * Hook override: loot only if no hero died on this floor.
     */
    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        boolean allSurvived = result.getDamageTaken() < 999;
        if (!allSurvived) System.out.println("[Loot] Casualties — no loot awarded.");
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] Trap survivors find a hidden stash — each hero heals 15 HP!");
        for (Hero h : party) {
            if (h.isAlive()) {
                h.heal(15);
                System.out.println("  " + h.getName() + " -> HP: " + h.getHp());
            }
        }
    }
}
