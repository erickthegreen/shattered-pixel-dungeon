/*
 * Shattered Pixel Dungeon — Edição Infernal
 * Original eight-frame pixel animation; CC0 artwork authored for this fork.
 */
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class AbyssalDevourerSprite extends MobSprite {

    public AbyssalDevourerSprite() {
        super();
        texture(Assets.Sprites.ABYSSAL_DEVOURER);
        // Eight consistently aligned 32x32 cells, from the original PNG.
        TextureFilm frames = new TextureFilm(texture, 32, 32);

        idle = new Animation(4, true);
        idle.frames(frames, 0, 1, 7);

        run = new Animation(8, true);
        run.frames(frames, 0, 1, 2, 3, 2, 1);

        attack = new Animation(10, false);
        attack.frames(frames, 4, 5, 6, 7);

        zap = new Animation(9, false);
        zap.frames(frames, 4, 5, 6, 7);

        die = new Animation(6, false);
        die.frames(frames, 6, 7, 7);

        play(idle);
    }
}
