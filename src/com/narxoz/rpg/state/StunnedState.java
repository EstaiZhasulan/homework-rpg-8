package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * State 3: Stunned — hero cannot act this turn.
 * Self-transitions to NormalState after exactly 1 turn.
 *
 * Modifiers:
 *   outgoing damage: N/A (canAct returns false, so attack is skipped)
 *   incoming damage × 1.25 (stunned heroes cannot dodge)
 *   canAct: FALSE — defines key State pattern control flow change
 */
public class StunnedState implements HeroState {

    @Override
    public String getName() { return "Stunned"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return basePower; }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.25);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("[Stunned] " + hero.getName() + " is stunned and cannot act!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        System.out.println("[Stunned] " + hero.getName() + " shakes off the stun.");
        hero.setState(new NormalState());  // self-transition after 1 turn
    }

    @Override
    public boolean canAct() { return false; }
}
