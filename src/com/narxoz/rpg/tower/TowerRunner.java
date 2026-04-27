package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;

import java.util.List;

/**
 * Executes a sequence of floors in order.
 * Tracks floors cleared, hero survival, and returns a TowerRunResult.
 */
public class TowerRunner {

    private final List<Hero> party;
    private final List<TowerFloor> floors;

    public TowerRunner(List<Hero> party, List<TowerFloor> floors) {
        this.party = party;
        this.floors = floors;
    }

    public TowerRunResult run() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     THE HAUNTED TOWER — ASCENT BEGINS    ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("Party:");
        for (Hero h : party) System.out.println("  " + h);
        System.out.println("Floors: " + floors.size());

        int floorsCleared = 0;

        for (int i = 0; i < floors.size(); i++) {
            TowerFloor floor = floors.get(i);

            System.out.println("\n══ Floor " + (i + 1) + " of " + floors.size() + " ══");

            if (!anyAlive()) {
                System.out.println("All heroes have fallen. Tower climb ends.");
                break;
            }

            FloorResult result = floor.explore(party);

            if (result.isCleared()) {
                floorsCleared++;
                System.out.println("\n[Tower] Floor " + (i + 1) + " cleared! (" + floorsCleared + "/" + floors.size() + ")");
            } else {
                System.out.println("\n[Tower] Floor " + (i + 1) + " failed. The party retreats.");
                break;
            }

            // Print party status after each floor
            System.out.println("[Party Status]");
            for (Hero h : party) System.out.println("  " + h);
        }

        boolean reachedTop = floorsCleared == floors.size() && anyAlive();
        int survivors = (int) party.stream().filter(Hero::isAlive).count();

        return new TowerRunResult(floorsCleared, survivors, reachedTop);
    }

    private boolean anyAlive() {
        return party.stream().anyMatch(Hero::isAlive);
    }
}
