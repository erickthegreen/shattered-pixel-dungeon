/*
 * Shattered Pixel Dungeon - Infernal Edition
 * Diagnostic test scene; accessible ONLY by explicit debug Android intent.
 * Runs in a real Android emulator, using the game's own startup/load logic.
 */
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TibiaSewers;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;

import java.util.ArrayList;

public class InfernalSmokeScene extends PixelScene {

    private static final String TAG = "INFERNAL_SMOKE";

    @Override
    public void create() {
        super.create();
        try {
            int[] checkDepths = {1, 2, 3, 4, 5, 6, 8, 9, 11, 12, 14, 16, 18, 19, 21, 22, 24};

            for (HeroClass heroClass : HeroClass.values()) {
                Dungeon.seed = 87897L + heroClass.ordinal();
                GamesInProgress.selectedClass = heroClass;
                GamesInProgress.curSlot = 1;
                Dungeon.init();

                ArrayList<Trinket> trinkets = Dungeon.hero.belongings.getAllItems(Trinket.class);
                if (trinkets.size() != 8) {
                    throw new AssertionError(heroClass + ": expected 8 trinkets, got " + trinkets.size());
                }
                for (Trinket trinket : trinkets) {
                    if (trinket.level() != 1000 || trinket.buffedLvl() > 3) {
                        throw new AssertionError(heroClass + ": unsafe trinket " + trinket.name());
                    }
                }
                if (Dungeon.hero.belongings.armor == null || Dungeon.hero.belongings.armor.level() != 100) {
                    throw new AssertionError(heroClass + ": missing starting +100 plate");
                }
                int rings = (Dungeon.hero.belongings.ring == null ? 0 : 1)
                        + (Dungeon.hero.belongings.extraRing == null ? 0 : 1);
                for (int i=0; i<Dungeon.hero.belongings.ringSlots.length; i++) {
                    if (Dungeon.hero.belongings.ringSlots[i] != null) rings++;
                }
                if (rings < 5) {
                    throw new AssertionError(heroClass + ": expected five equipped upgraded rings, got " + rings);
                }

                // Floor 1 creation directly exercises the user's crashing
                // starter loadout, all trinkets, and special-only population.
                Level firstFloor = Dungeon.newLevel();
                validateFloor(firstFloor, 1);
                Gdx.app.log(TAG, "PASS_CLASS " + heroClass + " trinkets=8 rings=" + rings + " enemies=" + firstFloor.mobs.size());
            }

            // Spawn tables and representative level generation from all regions.
            Dungeon.seed = 180923L;
            GamesInProgress.selectedClass = HeroClass.WARRIOR;
            Dungeon.init();
            for (int depth : checkDepths) {
                Dungeon.depth = depth;
                Dungeon.branch = 0;
                for (Class<? extends Mob> mobClass : MobSpawner.getMobRotation(depth)) {
                    Mob mob = com.watabou.utils.Reflection.newInstance(mobClass);
                    if (!MobSpawner.isInfernalSpecial(mob)) {
                        throw new AssertionError("ordinary mob at depth " + depth + ": " + mobClass.getSimpleName());
                    }
                }
                Level level = Dungeon.newLevel();
                validateFloor(level, depth);
                if (depth == 5) {
                    boolean boss = false;
                    for (Mob m : level.mobs) {
                        if (m instanceof TibiaSewers.DeadSlime) boss = true;
                    }
                    if (!boss) throw new AssertionError("Missing Dead Slime boss on floor 5");
                    if (!((com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel)level)
                            .isInfernalSafeZone(level.entrance())) {
                        throw new AssertionError("Safe zone missing from floor 5");
                    }
                }
                Gdx.app.log(TAG, "PASS_FLOOR depth=" + depth + " mobs=" + level.mobs.size());
            }

            // Enter a real rendered game scene. The emulator keeps the process
            // alive and checks for subsequent renderer/texture crashes.
            int depthToRender = ShatteredPixelDungeon.infernalSmokeDepth;
            Dungeon.seed = 300193L;
            GamesInProgress.selectedClass = HeroClass.WARRIOR;
            GamesInProgress.curSlot = 1;
            Dungeon.init();
            Dungeon.depth = depthToRender;
            Level level = Dungeon.newLevel();
            validateFloor(level, depthToRender);
            Dungeon.switchLevel(level, level.entrance());
            // GameScene normally follows InterlevelScene, which supplies the
            // transition mode. Set it explicitly in this debug-only test.
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Gdx.app.log(TAG, "PASS READY_TO_RENDER depth=" + depthToRender);
            ShatteredPixelDungeon.switchNoFade(GameScene.class);
        } catch (Throwable error) {
            Gdx.app.error(TAG, "FAIL " + error.getClass().getSimpleName() + ": " + error.getMessage(), error);
            // Do not proceed to publish the APK: CI greps for this failure.
        }
    }

    private static void validateFloor(Level level, int depth) {
        if (level == null || level.entrance() < 0 || level.entrance() >= level.length()) {
            throw new AssertionError("invalid floor at depth " + depth);
        }
        int count = 0;
        for (Mob mob : level.mobs) {
            if (mob.alignment != Char.Alignment.ENEMY) continue;
            count++;
            // Story-scripted monsters, mimics and quest enemies may use their
            // original classes; all procedural rotations are checked above.
            // Bee and some puzzle summons intentionally grant 0 XP to prevent
            // farming. Only special normal-spawn replacements must grant XP.
            boolean xpRequired = MobSpawner.isInfernalSpecial(mob)
                    || mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomPiranha
                    || mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
            if (xpRequired && mob.EXP <= 0) {
                throw new AssertionError("special without XP: " + mob.getClass().getSimpleName()
                        + " at depth " + depth);
            }
        }
        if (depth == 1 && count > 15) {
            throw new AssertionError("first floor overcrowded: " + count);
        }
        if (depth > 1 && count > 24) {
            throw new AssertionError("overcrowded floor " + depth + ": " + count);
        }
    }
}
