package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/** Twenty-two individually illustrated Tibia-style monsters in one compact
 * pixel-art sheet. Each frame is 32x32, anchored along the lower edge. */
public final class TibiaAllSprites {
    private TibiaAllSprites(){}
    private static abstract class TibiaMob extends MobSprite {
        protected TibiaMob(int row) {
            super();
            texture(Assets.Sprites.TIBIA_ALL_V2);
            TextureFilm film = new TextureFilm(texture,32,32);
            int i = row*4;
            idle = new Animation(3,true);
            idle.frames(film,i);
            run = new Animation(8,true);
            run.frames(film,i+1);
            attack = new Animation(10,false);
            attack.frames(film,i+2);
            zap = attack.clone();
            die = new Animation(7,false);
            die.frames(film,i+3);
            // Small monsters remain small; elite creatures and dragons look imposing.
            float[] visualSizes = {
                0.68f,0.79f,0.86f,0.96f,1.02f,0.78f,
                0.97f,0.91f,1.03f,1.10f,0.94f,1.05f,
                0.85f,0.90f,1.02f,0.93f,1.06f,1.11f,
                0.98f,0.95f,1.12f,1.13f
            };
            scale.set(visualSizes[row],visualSizes[row]);
            play(idle);
        }
    }
    public static class Rat extends TibiaMob { public Rat(){super(0);} }
    public static class Rotworm extends TibiaMob { public Rotworm(){super(1);} }
    public static class CarrionWorm extends TibiaMob { public CarrionWorm(){super(2);} }
    public static class Troll extends TibiaMob { public Troll(){super(3);} }
    public static class Minotaur extends TibiaMob { public Minotaur(){super(4);} }
    public static class Slime extends TibiaMob { public Slime(){super(5);} }
    public static class DemonSkeleton extends TibiaMob { public DemonSkeleton(){super(6);} }
    public static class Bandit extends TibiaMob { public Bandit(){super(7);} }
    public static class Dragon extends TibiaMob { public Dragon(){super(8);} }
    public static class DragonLord extends TibiaMob { public DragonLord(){super(9);} }
    public static class Necromancer extends TibiaMob { public Necromancer(){super(10);} }
    public static class BlackKnight extends TibiaMob { public BlackKnight(){super(11);} }
    public static class Dwarf extends TibiaMob { public Dwarf(){super(12);} }
    public static class DwarfSoldier extends TibiaMob { public DwarfSoldier(){super(13);} }
    public static class DwarfGuard extends TibiaMob { public DwarfGuard(){super(14);} }
    public static class DwarfGeomancer extends TibiaMob { public DwarfGeomancer(){super(15);} }
    public static class Demon extends TibiaMob { public Demon(){super(16);} }
    public static class Behemoth extends TibiaMob { public Behemoth(){super(17);} }
    public static class ElderDemonSkeleton extends TibiaMob { public ElderDemonSkeleton(){super(18);} }
    public static class Warlock extends TibiaMob { public Warlock(){super(19);} }
    public static class DemonKnight extends TibiaMob { public DemonKnight(){super(20);} }
    public static class AncientDragon extends TibiaMob { public AncientDragon(){super(21);} }
}
