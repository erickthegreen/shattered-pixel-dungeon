package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.watabou.noosa.TextureFilm;

/** 48px original pixel-art boss graphics. Keep each vanilla boss sprite
 * superclass so its puzzle, attack, zap and ending callbacks still work. */
public final class TibiaBossSprites {
    private TibiaBossSprites(){}

    private static void dummy(){}

    public static class DeadSlime extends GooSprite {
        public DeadSlime(){
            super();
            texture(Assets.Sprites.TIBIA_BOSSES_V2);
            TextureFilm f = new TextureFilm(texture,48,48);
            int i = 0;
            idle = new Animation(5,true); idle.frames(f,i,i);
            run = new Animation(8,true); run.frames(f,i+1,i+1);
            attack = new Animation(10,false);attack.frames(f,i+2,i+2);
            pump = new Animation(12,true);pump.frames(f,i+1,i+2);
            pumpAttack = new Animation(15,false);pumpAttack.frames(f,i+2,i+2);
            die = new Animation(8,false);die.frames(f,i+3,i+3);
            play(idle);
        }
    }

    public static class BlackKnight extends TenguSprite {
        public BlackKnight(){
            super();
            texture(Assets.Sprites.TIBIA_BOSSES_V2);
            TextureFilm f = new TextureFilm(texture,48,48);
            int i = 4;
            idle = new Animation(4,true);idle.frames(f,i,i);
            run = new Animation(12,false);run.frames(f,i+1,i+1,i);
            attack = new Animation(11,false);attack.frames(f,i+2,i+2);
            zap = attack.clone();
            die = new Animation(8,false);die.frames(f,i+3,i+3);
            play(idle);
        }
    }

    public static class StoneGolem extends DM300Sprite {
        private boolean tibiaReady;
        private Animation golemCharge;
        private Animation golemSlam;

        public StoneGolem(){
            super();
            tibiaReady = true;
            updateChargeState(false);
        }
        @Override
        public void updateChargeState(boolean enraged){
            // The DM300Sprite superclass calls this during its own constructor.
            if(!tibiaReady){super.updateChargeState(enraged);return;}
            texture(Assets.Sprites.TIBIA_BOSSES_V2);
            TextureFilm f = new TextureFilm(texture,48,48);
            int i = 8;
            idle = new Animation(enraged?12:7,true);idle.frames(f,i,i);
            run = new Animation(9,true);run.frames(f,i+1,i+1);
            attack = new Animation(12,false);attack.frames(f,i+2,i+2);
            zap = attack.clone();
            die = new Animation(8,false);die.frames(f,i+3,i+3);
            golemCharge = new Animation(4,true);golemCharge.frames(f,i+1,i+4);
            golemSlam = attack.clone();
            play(idle);
        }
        @Override public void charge(){play(golemCharge);}
        @Override public void slam(int cell){turnTo(ch.pos,cell);play(golemSlam);}
        @Override public void onComplete(Animation anim){
            if (anim==golemSlam && ch instanceof DM300)
                ((DM300)ch).onSlamComplete();
            super.onComplete(anim);
        }
    }

    public static class DwarfKing extends KingSprite {
        public DwarfKing(){
            super();
            texture(Assets.Sprites.TIBIA_BOSSES_V2);
            TextureFilm f = new TextureFilm(texture,48,48);
            int i = 12;
            idle = new Animation(5,true);idle.frames(f,i,i);
            run = new Animation(10,true);run.frames(f,i+1,i+1);
            attack = new Animation(12,false);attack.frames(f,i+2,i+2);
            die = new Animation(8,false);die.frames(f,i+3,i+3);
            play(idle);
        }
    }

    public static class DemonLord extends YogSprite {
        public DemonLord(){
            super();
            texture(Assets.Sprites.TIBIA_BOSSES_V2);
            TextureFilm f = new TextureFilm(texture,48,48);
            int i = 16;
            idle = new Animation(4,true);idle.frames(f,i,i);
            run = new Animation(8,true);run.frames(f,i+1,i+1);
            attack = new Animation(10,false);attack.frames(f,i+2,i+2);
            zap = attack.clone();
            die = new Animation(8,false);die.frames(f,i+3,i+3);
            play(idle);
        }
    }
}
