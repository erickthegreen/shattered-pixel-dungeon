package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

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

/** Class-specific starting rings; original class weapons and armor stay at level zero. */
final class ClassStarterRings {
	private ClassStarterRings() {}

	static void equip(Hero hero) {
		Ring first;
		Ring second;
		switch (hero.heroClass) {
			case WARRIOR:
				first = new RingOfMight(); second = new RingOfTenacity(); break;
			case MAGE:
				first = new RingOfEnergy(); second = new RingOfArcana(); break;
			case ROGUE:
				first = new RingOfEvasion(); second = new RingOfHaste(); break;
			case HUNTRESS:
				first = new RingOfSharpshooting(); second = new RingOfHaste(); break;
			case DUELIST:
				first = new RingOfAccuracy(); second = new RingOfFuror(); break;
			case CLERIC:
				first = new RingOfEnergy(); second = new RingOfTenacity(); break;
			default: return;
		}
		first.identify();
		second.identify();
		hero.belongings.putInFreeRingSlot(first);
		hero.belongings.putInFreeRingSlot(second);
		first.activate(hero);
		second.activate(hero);
		hero.updateHT(true);
	}
}
