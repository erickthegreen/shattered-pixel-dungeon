package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.InfernalTrinketPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dirk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HandAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.watabou.utils.Reflection;

/**
 * Balanced Tibia edition: two modest rings, class weapon +2, armor +2.
 * The former tier-5 +100 weapons and five +15 rings removed all challenge.
 * Every adventurer still retains their original class-specific equipment.
 */
public final class InfernalStarterBuilds {
    private InfernalStarterBuilds() {}
    private static final int WEAPON_LEVEL = 2;
    private static final int TRINKET_LEVEL = 2;

    public static void prepare(Hero hero) {
        switch (hero.heroClass) {
            case WARRIOR:
                equipWeapon(hero, new HandAxe());
                break;
            case ROGUE:
                equipWeapon(hero, new Dirk());
                break;
            case DUELIST:
                equipWeapon(hero, new Rapier());
                break;
            case HUNTRESS:
                // Keep starting melee equipment and original ranged bow mechanics.
                if (hero.belongings.weapon != null) {
                    hero.belongings.weapon.level(1);
                    hero.belongings.weapon.identify();
                }
                for (SpiritBow bow : hero.belongings.getAllItems(SpiritBow.class)) {
                    bow.level(1);
                    bow.identify();
                }
                break;
            case MAGE:
            case CLERIC:
            default:
                // Never replace the Mage's special charging staff or Cleric's relic.
                if (hero.belongings.weapon != null) {
                    hero.belongings.weapon.level(WEAPON_LEVEL);
                    hero.belongings.weapon.identify();
                }
                break;
        }

        int equipped = 0;
        for (Class<?> ringClass : Generator.Category.RING.classes) {
            int upgrades = ringUpgrade(hero.heroClass, ringClass);
            if (upgrades <= 0 || equipped >= 2) continue;
            Ring ring = (Ring) Reflection.newInstance(ringClass);
            ring.level(upgrades);
            ring.identify();
            hero.belongings.putInFreeRingSlot(ring);
            ring.activate(hero);
            equipped++;
        }
        hero.updateHT(true);

        // Eight curated, small-effect trinkets; no permanent 1000-level buffs.
        new InfernalTrinketPouch().collect();
        for (Class<?> c : Generator.Category.TRINKET.classes) {
            @SuppressWarnings("unchecked")
            Class<? extends Trinket> type = (Class<? extends Trinket>)c;
            Trinket t = hero.belongings.getItem(type);
            if (t == null) {
                t = (Trinket)Reflection.newInstance(type);
                t.level(TRINKET_LEVEL);
                t.identify();
                t.collect();
            } else {
                t.level(TRINKET_LEVEL);
            }
        }
    }

    private static void equipWeapon(Hero hero, KindOfWeapon weapon) {
        weapon.level(WEAPON_LEVEL);
        weapon.identify();
        hero.belongings.weapon = weapon;
        weapon.activate(hero);
    }

    public static int ringUpgrade(HeroClass cls, Class<?> c) {
        if (cls == null) return 0;
        switch (cls) {
            case WARRIOR:
                return c == RingOfMight.class || c == RingOfTenacity.class ? 1 : 0;
            case MAGE:
                return c == RingOfEnergy.class || c == RingOfArcana.class ? 1 : 0;
            case ROGUE:
                return c == RingOfEvasion.class || c == RingOfHaste.class ? 1 : 0;
            case HUNTRESS:
                return c == RingOfSharpshooting.class || c == RingOfHaste.class ? 1 : 0;
            case DUELIST:
                return c == RingOfAccuracy.class || c == RingOfFuror.class ? 1 : 0;
            case CLERIC:
                return c == RingOfEnergy.class || c == RingOfTenacity.class ? 1 : 0;
            default: return 0;
        }
    }
}
