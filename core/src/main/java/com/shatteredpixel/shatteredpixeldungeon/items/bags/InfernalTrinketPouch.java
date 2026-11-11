package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/** Stores all seventeen unique infernal trinkets in their own inventory tab. */
public class InfernalTrinketPouch extends Bag {
    {
        image = ItemSpriteSheet.POUCH;
    }

    @Override
    public String name() {
        return "Bolsa Infernal de Amuletos";
    }

    @Override
    public String desc() {
        return "Guarda os 17 amuletos especiais no nivel +1000. " +
                "Seus efeitos usam limites de seguranca para evitar travamentos.";
    }

    @Override
    public boolean canHold(Item item) {
        return item instanceof Trinket && super.canHold(item);
    }

    @Override
    public int capacity() {
        return 19;
    }

    @Override
    public int value() {
        return 0;
    }
}
