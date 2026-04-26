package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    private int turnsRemaining;

    public BerserkState(int durationTurns) {
        this.turnsRemaining = durationTurns;
    }

    @Override
    public String getName() { return "Berserk(" + turnsRemaining + "t)"; }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 1.75);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.30);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("[Berserk] " + hero.getName()
                + " rages! ATK boosted ×1.75, but defense is reckless. ("
                + turnsRemaining + "t remaining)");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("[Berserk] " + hero.getName() + "'s rage subsides. Returning to Normal.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}
