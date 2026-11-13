
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/** Native 32x32 texture cells cropped from newly authored fantasy sprites.
 * 8 columns by 7 rows, normal actors take eight frames per row. */
public final class TibiaSewerSprites {
    private TibiaSewerSprites(){}

    private abstract static class Normal extends MobSprite {
        Normal(int row){
            super();
            texture(Assets.Sprites.TIBIA_SEWERS);
            TextureFilm frames = new TextureFilm(texture, 32, 32);
            int i = 8*row;
            idle = new Animation(3, true);
            idle.frames(frames, i, i+1);
            run = new Animation(9, true);
            run.frames(frames, i+2, i+3);
            attack = new Animation(11, false);
            attack.frames(frames, i+4, i+5);
            die = new Animation(8, false);
            die.frames(frames, i+6, i+7);
            play(idle);
        }
    }
    public static class Rat extends Normal { public Rat(){ super(0); } }
    public static class Rotworm extends Normal { public Rotworm(){ super(1); } }
    public static class CarrionWorm extends Normal { public CarrionWorm(){ super(2); } }
    public static class Troll extends Normal { public Troll(){ super(3); } }
    public static class Minotaur extends Normal { public Minotaur(){ super(4); } }
    public static class Slime extends Normal { public Slime(){ super(5); } }

    /** Goo's animation/particle infrastructure still handles charge warnings. */
    public static class DeadSlime extends GooSprite {
        public DeadSlime(){
            super();
            texture(Assets.Sprites.TIBIA_SEWERS);
            TextureFilm frames = new TextureFilm(texture, 32, 32);
            int i = 48;
            idle = new Animation(5, true);
            idle.frames(frames, i, i+1);
            run = new Animation(8, true);
            run.frames(frames, i+2, i+3);
            attack = new Animation(10, false);
            attack.frames(frames, i+4, i+5);
            pump = new Animation(12, true);
            pump.frames(frames, i+2, i+4);
            pumpAttack = new Animation(15, false);
            pumpAttack.frames(frames, i+4, i+5);
            die = new Animation(8, false);
            die.frames(frames, i+6, i+7);
            play(idle);
        }
    }
}
