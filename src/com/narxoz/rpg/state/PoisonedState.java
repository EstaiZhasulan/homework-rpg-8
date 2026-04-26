package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * State 2: Poisoned — debuff that deals passive damage each turn and weakens attacks.
 * Self-transitions to NormalState after a set number of turns.
 *
 * Modifiers:
 *   outgoing damage × 0.75  (poison weakens muscles)
 *   incoming damage × 1.10  (reduced resistance)
 *   onTurnStart: deals poisonDamage HP to the hero (raw — bypasses state modifier)
 *   canAct: true
 */
public class PoisonedState implements HeroState {


    private final int poisonDamage;
    private int turnsRemaining;

    public PoisonedState(int poisonDamage, int durationTurns) {
        this.poisonDamage = poisonDamage;
        this.turnsRemaining = durationTurns;
    }

    @Override
    public String getName() { return "Poisoned(" + turnsRemaining + "t)"; }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 0.75);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.10);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("[Poison] " + hero.getName() + " suffers " + poisonDamage
                + " poison damage! (turns remaining: " + turnsRemaining + ")");
        hero.applyRawDamage(poisonDamage);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("[Poison] " + hero.getName() + " recovers from poison!");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}
