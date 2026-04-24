package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState();
    }

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = initialState;
    }

    public String getName()     { return name; }
    public int getHp()          { return hp; }
    public int getMaxHp()       { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense()     { return defense; }
    public boolean isAlive()    { return hp > 0; }
    public HeroState getState() { return state; }

    public void setState(HeroState newState) {
        System.out.println("[State] " + name + ": " + state.getName()
                + " -> " + newState.getName());
        this.state = newState;
    }

    public int getEffectiveDamage() {
        return state.modifyOutgoingDamage(attackPower);
    }

    public boolean canAct() {
        return state.canAct();
    }

    public void onTurnStart() {
        state.onTurnStart(this);
    }

    public void onTurnEnd() {
        state.onTurnEnd(this);
    }

    public void takeDamage(int rawDamage) {
        int modified = state.modifyIncomingDamage(rawDamage);
        int actual = Math.max(1, modified - defense / 4);
        hp = Math.max(0, hp - actual);
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
}
