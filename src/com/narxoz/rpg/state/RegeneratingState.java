package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * State 5: Regenerating — heals a small amount each turn start.
 * Self-transitions to NormalState after N turns.
 */
public class RegeneratingState implements HeroState {


    private final int regenAmount;
    private int turnsRemaining;

    public RegeneratingState(int regenAmount, int durationTurns) {
        this.regenAmount = regenAmount;
        this.turnsRemaining = durationTurns;
    }

    @Override
    public String getName() { return "Regenerating(" + turnsRemaining + "t)"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { return basePower; }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 0.90);
    }

    @Override
    public void onTurnStart(Hero hero) {
        hero.heal(regenAmount);
        System.out.println("[Regen] " + hero.getName() + " regenerates " + regenAmount
                + " HP. (HP: " + hero.getHp() + "/" + hero.getMaxHp()
                + ", turns left: " + turnsRemaining + ")");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("[Regen] " + hero.getName() + "'s regeneration fades.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}
