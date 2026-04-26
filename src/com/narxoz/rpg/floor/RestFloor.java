package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.RegeneratingState;

import java.util.List;

/**
 * Floor type 2: Rest Floor
 * Heroes find a safe room and recover. No combat — no loot.
 *
 * Hook overrides:
 *   - shouldAwardLoot() returns FALSE (rest needs no loot step)
 *   - cleanup() resets any negative states to Regenerating
 */
public class RestFloor extends TowerFloor {

    private final int healAmount;

    public RestFloor(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    protected String getFloorName() { return "Sanctuary Chamber"; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] A peaceful chamber. The air smells of herbs and magic.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Rest] The party rests and tends to their wounds...");
        int totalHealed = 0;
        for (Hero h : party) {
            if (!h.isAlive()) continue;
            int before = h.getHp();
            h.heal(healAmount);
            int healed = h.getHp() - before;
            totalHealed += healed;
            System.out.println("  " + h.getName() + " healed " + healed + " HP -> " + h.getHp() + "/" + h.getMaxHp());
        }
        return new FloorResult(true, 0, "Party rested and recovered " + totalHealed + " total HP.");
    }

    /**
     * Hook override: no loot on a rest floor.
     */
    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("[Loot] No loot here — this is a rest chamber.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        // Never called because shouldAwardLoot returns false
    }

    /**
     * Hook override: after resting, grant Regenerating state to all living heroes.
     */
    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] The chamber's magic grants regeneration for 2 turns.");
        for (Hero h : party) {
            if (h.isAlive()) {
                h.setState(new RegeneratingState(6, 2));
            }
        }
    }
}
