package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;

import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        Hero erlan = new Hero("Erlan", 120, 25, 8, new BerserkState(2));
        Hero aisha = new Hero("Aisha", 140, 18, 12, new PoisonedState(5, 3));

        List<Hero> party = Arrays.asList(erlan, aisha);

        System.out.println("=== PARTY ASSEMBLED ===");
        for (Hero h : party) System.out.println("  " + h);

        List<TowerFloor> floors = Arrays.asList(

                new CombatFloor("Skeleton Crypt",
                        "Skeleton:30:12",
                        "Cursed Skeleton:25:10"),

                new RestFloor(30),

                new TrapFloor("Poison Needle Corridor", 8, 12, 6, 15),

                new CombatFloor("Golem Forge",
                        "Stone Golem:50:18",
                        "Stone Guardian:40:15"),

                new BossFloor("Shadow Dragon", 150, 22)
        );

        TowerRunner runner = new TowerRunner(party, floors);
        TowerRunResult result = runner.run();

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║            TOWER RUN COMPLETE            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Floors cleared : %-22d ║%n", result.getFloorsCleared());
        System.out.printf( "║  Heroes surviving: %-21d ║%n", result.getHeroesSurviving());
        System.out.printf( "║  Tower status   : %-22s ║%n",
                result.isReachedTop() ? "CONQUERED!" : "Defeated...");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.println("\nFinal party status:");
        for (Hero h : party) System.out.println("  " + h);
    }
}
