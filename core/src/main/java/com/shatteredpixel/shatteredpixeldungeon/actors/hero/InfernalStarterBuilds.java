package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.InfernalTrinketPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.watabou.utils.Reflection;

/**
 * Infernal starting loadouts, selected by hero class (not by subclass, which
 * is unlocked later). +1000 is the stored trinket level, but trinket effects
 * are capped by Trinket.buffedLvl() to avoid invalid math and endless loops.
 */
public final class InfernalStarterBuilds {
    private InfernalStarterBuilds() {}

    private static final int WEAPON_LEVEL = 100;
    private static final int TRINKET_LEVEL = 1000;

    public static void prepare(Hero hero) {
        // Keep the existing plate armor and class artifacts. Replace the starter weapon only.
        switch (hero.heroClass) {
            case WARRIOR:
                equipWeapon(hero, new RunicBlade());
                break;
            case MAGE:
                // Preserve the existing Mage staff's charging mechanics.
                MagesStaff staff = (MagesStaff) hero.belongings.weapon;
                staff.imbueWand(new WandOfDisintegration(), hero);
                staff.level(WEAPON_LEVEL);
                staff.updateWand(false);
                staff.identify();
                break;
            case ROGUE:
                equipWeapon(hero, new AssassinsBlade());
                break;
            case HUNTRESS:
                // The Spirit Bow's infernal upgrade is handled separately.
                equipWeapon(hero, new Greatshield());
                for (SpiritBow bow : hero.belongings.getAllItems(SpiritBow.class)) {
                    bow.level(WEAPON_LEVEL);
                    bow.identify();
                }
                break;
            case DUELIST:
                equipWeapon(hero, new RunicBlade());
                // A second upgraded weapon is provided for the Champion path.
                new Greatshield().level(WEAPON_LEVEL).identify().collect();
                break;
            case CLERIC:
                equipWeapon(hero, new Greatshield());
                break;
            default:
                break;
        }

        // Auto-equip the three most useful rings for this class.
        for (Class<?> ringClass : Generator.Category.RING.classes) {
            int upgrades = ringUpgrade(hero.heroClass, ringClass);
            if (upgrades > 0) {
                Ring ring = (Ring) Reflection.newInstance(ringClass);
                ring.level(upgrades);
                ring.identify();
                hero.belongings.putInFreeRingSlot(ring);
                ring.activate(hero);
            }
        }

        // Existing Mimic Tooth is moved into this bag; all other trinkets join it.
        new InfernalTrinketPouch().collect();
        for (Class<?> trinketClass : Generator.Category.TRINKET.classes) {
            @SuppressWarnings("unchecked")
            Class<? extends Trinket> itemClass = (Class<? extends Trinket>) trinketClass;
            Trinket existing = hero.belongings.getItem(itemClass);
            if (existing != null) {
                existing.level(TRINKET_LEVEL);
            } else {
                Trinket trinket = (Trinket) Reflection.newInstance(trinketClass);
                trinket.level(TRINKET_LEVEL);
                trinket.identify();
                trinket.collect();
            }
        }
    }

    private static void equipWeapon(Hero hero, KindOfWeapon weapon) {
        weapon.level(WEAPON_LEVEL);
        weapon.identify();
        hero.belongings.weapon = weapon;
        weapon.activate(hero);
    }

    /** Only preferred rings are upgraded; all other types stay at their normal level. */
    public static int ringUpgrade(HeroClass cls, Class<?> ringClass) {
        if (cls == null) return 0;
        switch (cls) {
            case WARRIOR:
                if (ringClass == RingOfFuror.class) return 16;
                if (ringClass == RingOfAccuracy.class) return 20;
                if (ringClass == RingOfArcana.class) return 12;
                break;
            case MAGE:
                if (ringClass == RingOfEnergy.class) return 16;
                if (ringClass == RingOfArcana.class) return 12;
                if (ringClass == RingOfTenacity.class) return 15;
                break;
            case ROGUE:
                if (ringClass == RingOfAccuracy.class) return 20;
                if (ringClass == RingOfFuror.class) return 16;
                if (ringClass == RingOfEvasion.class) return 15;
                break;
            case HUNTRESS:
                if (ringClass == RingOfSharpshooting.class) return 25;
                if (ringClass == RingOfFuror.class) return 16;
                if (ringClass == RingOfAccuracy.class) return 20;
                break;
            case DUELIST:
                if (ringClass == RingOfArcana.class) return 12;
                if (ringClass == RingOfFuror.class) return 16;
                if (ringClass == RingOfAccuracy.class) return 20;
                break;
            case CLERIC:
                if (ringClass == RingOfEnergy.class) return 16;
                if (ringClass == RingOfTenacity.class) return 15;
                if (ringClass == RingOfArcana.class) return 12;
                break;
            default:
                break;
        }
        return 0;
    }
}
