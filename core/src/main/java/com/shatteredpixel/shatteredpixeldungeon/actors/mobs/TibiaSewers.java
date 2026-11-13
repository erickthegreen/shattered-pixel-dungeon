
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TibiaSewerSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/** Tibia-inspired original monsters for floors 1-5.  The existing global
 * Infernal difficulty multipliers are applied by Mob/Char exactly once. */
public final class TibiaSewers {
    private TibiaSewers(){}

    public abstract static class Creature extends Mob {
        protected int lowDamage, highDamage, accuracy, maximumDR;
        protected String tibiaName;

        protected void setup(String name, int hp, int minDmg, int maxDmg,
                             int evasion, int aim, int dr, int xp) {
            tibiaName = name;
            HP = HT = hp;
            lowDamage = minDmg;
            highDamage = maxDmg;
            accuracy = aim;
            maximumDR = dr;
            defenseSkill = evasion;
            EXP = xp;
            maxLvl = 29; // XP remains meaningful even with starting XP potions.
        }

        @Override public String name() { return tibiaName; }
        @Override public int damageRoll() { return Random.NormalIntRange(lowDamage, highDamage); }
        @Override public int attackSkill(Char target) { return accuracy; }
        @Override public int drRoll() { return super.drRoll() + Random.NormalIntRange(0, maximumDR); }
    }

    public static class Rat extends Creature {
        { setup("Rat", 12, 1, 2, 3, 8, 1, 1);
          spriteClass = TibiaSewerSprites.Rat.class;
          baseSpeed = 1.25f; }
    }

    public static class Rotworm extends Creature {
        { setup("Rotworm", 28, 2, 5, 4, 10, 2, 2);
          spriteClass = TibiaSewerSprites.Rotworm.class;
          baseSpeed = 0.85f; }

        @Override public int attackProc(Char victim, int damage) {
            damage = super.attackProc(victim, damage);
            if (damage > 0 && Random.Int(8) == 0) {
                Buff.prolong(victim, Cripple.class, 2f);
            }
            return damage;
        }
    }

    public static class CarrionWorm extends Creature {
        { setup("Carrion Worm", 44, 3, 6, 5, 11, 3, 3);
          spriteClass = TibiaSewerSprites.CarrionWorm.class; }

        @Override public int attackProc(Char victim, int damage) {
            damage = super.attackProc(victim, damage);
            if (damage > 0 && Random.Int(5) == 0) {
                Buff.affect(victim, Ooze.class).set(Ooze.DURATION);
            }
            return damage;
        }
    }

    public static class Troll extends Creature {
        { setup("Troll", 34, 3, 7, 4, 11, 3, 3);
          spriteClass = TibiaSewerSprites.Troll.class;
          baseSpeed = 0.85f; }

        @Override public int damageRoll() {
            return super.damageRoll() + (Random.Int(5) == 0 ? 3 : 0);
        }
    }

    public static class Minotaur extends Creature {
        { setup("Minotaur", 64, 4, 9, 6, 13, 5, 5);
          spriteClass = TibiaSewerSprites.Minotaur.class; }

        @Override public int attackProc(Char victim, int damage) {
            damage = super.attackProc(victim, damage);
            if (damage > 0 && Random.Int(5) == 0) {
                Buff.prolong(victim, Cripple.class, 2f);
            }
            return damage;
        }
    }

    public static class Slime extends Creature {
        private boolean hasSplit;
        { setup("Slime", 80, 3, 8, 5, 12, 4, 6);
          spriteClass = TibiaSewerSprites.Slime.class; }

        @Override public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            if (!hasSplit && HP > 0 && HP*2 <= HT) {
                hasSplit = true;
                // One harmless-to-balance fragment per original slime;
                // never chain split and never allow XP/loot farming.
                for (int n : PathFinder.NEIGHBOURS8) {
                    int cell = pos + n;
                    if (Dungeon.level.insideMap(cell)
                            && Dungeon.level.passable[cell]
                            && Actor.findChar(cell) == null) {
                        SlimeFragment fragment = new SlimeFragment();
                        fragment.pos = cell;
                        GameScene.add(fragment);
                        break;
                    }
                }
            }
        }

        @Override public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put("tibia_has_split", hasSplit);
        }

        @Override public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            hasSplit = bundle.getBoolean("tibia_has_split");
        }
    }

    public static class SlimeFragment extends Creature {
        { setup("Fragmento de Slime", 12, 1, 2, 2, 7, 1, 0);
          spriteClass = TibiaSewerSprites.Slime.class;
          lootChance = 0f;
          maxLvl = 0; }
    }

    /** Subclassing Goo preserves boss keys, badges, phase transitions and
     * the floor-6 exit. The visuals and poison are new. */
    public static class DeadSlime extends Goo {
        { HP = HT = 480;
          EXP = 20;
          spriteClass = TibiaSewerSprites.DeadSlime.class; }
        @Override public String name(){ return "Dead Slime"; }
        @Override public int attackProc(Char victim, int damage){
            damage = super.attackProc(victim, damage);
            if (damage > 0 && Random.Int(3) == 0) {
                Buff.affect(victim, Ooze.class).set(Ooze.DURATION);
            }
            return damage;
        }
    }
}
