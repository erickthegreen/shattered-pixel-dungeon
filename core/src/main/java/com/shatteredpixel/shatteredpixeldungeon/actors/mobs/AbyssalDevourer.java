/*
 * Shattered Pixel Dungeon — Edição Infernal
 * Original elite enemy for this fork, preserving the normal Mob AI.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AbyssalDevourerSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AbyssalDevourer extends Mob {

    private static final String SPECIAL_CD = "infernal_special_cd";
    private int specialCooldown = 6;

    {
        spriteClass = AbyssalDevourerSprite.class;
        HP = HT = 200;
        defenseSkill = 23;
        EXP = 24;
        maxLvl = 26;
        viewDistance = 8;
        properties.add(Property.DEMONIC);
        properties.add(Property.LARGE);
        loot = Generator.Category.RING;
        lootChance = 0.35f;
    }

    @Override
    public String name() {
        return "Devorador Abissal";
    }

    @Override
    public String description() {
        return "Uma abominacao blindada nascida de um bau faminto. Seus olhos violetas "
                + "antecipam a investida, suas garras roubam vigor e seu rugido obscurece os sentidos.";
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 24);
    }

    @Override
    public int attackSkill(Char target) {
        return HP * 3 <= HT ? 38 : 26;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(6, 16);
    }

    @Override
    public float speed() {
        return super.speed() * (HP * 3 <= HT ? 1.5f : 1f);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        // Garras destruidoras: fraqueza no alvo; devorar energia: cura controlada.
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Weakness.class, 4f);
        }
        if (damage > 0 && Random.Int(3) == 0) {
            HP = Math.min(HT, HP + Math.max(1, damage/10));
        }
        return damage;
    }

    @Override
    protected boolean act() {
        // Rugido do Vazio: possui recarga e exige proximidade; sem invocar
        // novos atores, evitando loops de colocacao ou pilhas de monstros.
        if (state == HUNTING && Dungeon.hero != null && Dungeon.hero.isAlive()
                && Dungeon.level.distance(pos, Dungeon.hero.pos) <= 4
                && --specialCooldown <= 0) {
            Buff.affect(Dungeon.hero, Blindness.class, 3f);
            specialCooldown = 6;
        }
        return super.act();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPECIAL_CD, specialCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(SPECIAL_CD)) specialCooldown = bundle.getInt(SPECIAL_CD);
    }
}
