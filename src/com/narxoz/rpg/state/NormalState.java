package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * State 1: Normal — baseline state, no modifiers.
 * Heroes begin in this state. canAct() returns true.
 */
public class NormalState implements HeroState {

    @Override
    public String getName() { return "Normal"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return basePower; }

    @Override
    public int modifyIncomingDamage(int rawDamage) { return rawDamage; }

    @Override
    public void onTurnStart(Hero hero) {
        // No side effects in Normal state
    }

    @Override
    public void onTurnEnd(Hero hero) {
        // No countdown — state persists until externally changed
    }

    @Override
    public boolean canAct() { return true; }
}
