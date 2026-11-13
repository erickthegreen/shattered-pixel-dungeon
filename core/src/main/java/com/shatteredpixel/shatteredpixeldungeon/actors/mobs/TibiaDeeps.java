package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TibiaAllSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TibiaBossSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/** Creatures from Tibia reinterpreted for floors 6-25, with
 * independent base stats, lore, class-specific graphics and combat abilities. */
public final class TibiaDeeps {
 private TibiaDeeps(){}
 public abstract static class Creature extends Mob {
    protected String title, lore;
    protected int low, high, aim, maxDr;
    protected void stats(String n,String desc,int hp,int d1,int d2,int eva,int accuracy,int dr,int xp){
      title=n;lore=desc;HT=HP=hp;low=d1;high=d2;defenseSkill=eva;aim=accuracy;maxDr=dr;EXP=xp;maxLvl=29;
    }
    @Override public String name(){return title;}
    @Override public String description(){return lore;}
    @Override public int damageRoll(){return Random.NormalIntRange(low,high);}
    @Override public int attackSkill(Char target){return aim;}
    @Override public int drRoll(){return super.drRoll()+Random.NormalIntRange(0,maxDr);}
 }

 public static class DemonSkeleton extends Creature {
   { stats("Demon Skeleton","Esqueleto incendiado por magia demoníaca.",36,4,9,9,13,3,6);
     spriteClass=TibiaAllSprites.DemonSkeleton.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class Dragon extends Creature {
   { stats("Dragon","Dragão vermelho das cavernas, com mordida flamejante.",58,7,13,9,15,4,8);
     spriteClass=TibiaAllSprites.Dragon.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class DragonLord extends Creature {
   { stats("Dragon Lord","Um dragão superior que incendeia aventureiros.",100,11,20,12,19,6,12);
     spriteClass=TibiaAllSprites.DragonLord.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class Necromancer extends Creature {
   { stats("Necromancer","Feiticeiro que enfraquece intrusos e invoca esqueletos.",62,7,13,12,17,3,10);
     spriteClass=TibiaAllSprites.Necromancer.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(3)==0) Buff.prolong(victim,Weakness.class,4f);
       return dmg;
   }
 }


 public static class BlackKnight extends Creature {
   { stats("Black Knight","Cavaleiro negro resistente, com poderosos golpes de espada.",120,13,22,15,22,9,17);
     spriteClass=TibiaAllSprites.BlackKnight.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(4)==0) Buff.prolong(victim,Cripple.class,2f);
       return dmg;
   }
 }


 public static class Dwarf extends Creature {
   { stats("Dwarf","Minerador armado das montanhas subterrâneas.",68,8,14,13,17,5,10);
     spriteClass=TibiaAllSprites.Dwarf.class; }
   
 }


 public static class DwarfSoldier extends Creature {
   { stats("Dwarf Soldier","Soldado anão que patrulha as passagens de minério.",95,12,20,15,20,7,12);
     spriteClass=TibiaAllSprites.DwarfSoldier.class; }
   
 }


 public static class DwarfGuard extends Creature {
   { stats("Dwarf Guard","Defensor de escudo reforçado e machado de aço.",140,16,25,17,24,10,15);
     spriteClass=TibiaAllSprites.DwarfGuard.class; }
   
 }


 public static class DwarfGeomancer extends Creature {
   { stats("Dwarf Geomancer","Mago anão que lança maldições terrestres.",115,13,22,16,22,7,16);
     spriteClass=TibiaAllSprites.DwarfGeomancer.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(3)==0) Buff.prolong(victim,Weakness.class,4f);
       return dmg;
   }
 }


 public static class ElderDemonSkeleton extends Creature {
   { stats("Elder Demon Skeleton","Mortovivo demoníaco ancestral de armadura corrompida.",155,19,30,19,26,11,21);
     spriteClass=TibiaAllSprites.ElderDemonSkeleton.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class Demon extends Creature {
   { stats("Demon","Monstro infernal que incendeia inimigos.",205,24,39,21,29,13,24);
     spriteClass=TibiaAllSprites.Demon.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class Behemoth extends Creature {
   { stats("Behemoth","Colosso brutal capaz de esmagar escudos.",265,28,43,17,31,18,28);
     spriteClass=TibiaAllSprites.Behemoth.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(4)==0) Buff.prolong(victim,Cripple.class,2f);
       return dmg;
   }
 }


 public static class Warlock extends Creature {
   { stats("Warlock","Feiticeiro demoníaco que enfraquece suas presas.",150,18,32,22,30,8,24);
     spriteClass=TibiaAllSprites.Warlock.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(3)==0) Buff.prolong(victim,Weakness.class,4f);
       return dmg;
   }
 }


 public static class DemonKnight extends Creature {
   { stats("Demon Knight","General dos salões infernais, em armadura escura.",330,31,51,24,33,17,30);
     spriteClass=TibiaAllSprites.DemonKnight.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }


 public static class AncientDragon extends Creature {
   { stats("Ancient Dragon","Dragão ancestral marcado por feitiços sombrios.",275,28,43,21,32,14,30);
     spriteClass=TibiaAllSprites.AncientDragon.class; }
   @Override public int attackProc(Char victim,int dmg){
       dmg=super.attackProc(victim,dmg);
       if(dmg>0 && Random.Int(5)==0 && !Dungeon.level.water[victim.pos])
           Buff.affect(victim,Burning.class).reignite(victim,3f);
       return dmg;
   }
 }

// Summoned creatures never reward farming and cannot summon further undead.
 public static class SummonedSkeleton extends DemonSkeleton {
   { title="Esqueleto Invocado"; lore="Invocação sem XP ou saque.";
     HP=HT=20; EXP=0; maxLvl=0; lootChance=0f; }
 }

 public static class BlackKnightBoss extends Tengu {
   { HP=HT=260;EXP=28;spriteClass=TibiaBossSprites.BlackKnight.class; }
   @Override public String name(){return "Black Knight — Comandante";}
   @Override public String description(){return "O cavaleiro negro mais temido da prisão. Mantém as fases e armadilhas da luta original.";}
 }
 public static class StoneGolemBoss extends DM300 {
   { HP=HT=420;EXP=35;spriteClass=TibiaBossSprites.StoneGolem.class; }
   @Override public String name(){return "Stone Golem Ancestral";}
   @Override public String description(){return "O antigo guardião das cavernas, carregado com energia mágica subterrânea.";}
 }
 public static class DwarfKingBoss extends DwarfKing {
   { spriteClass=TibiaBossSprites.DwarfKing.class; }
   @Override public String name(){return "Rei dos Anões";}
   @Override public String description(){return "O Rei dos Anões e suas guardas reais. Suas fases e invocações foram preservadas.";}
 }
 public static class DemonLordBoss extends YogDzewa {
   { spriteClass=TibiaBossSprites.DemonLord.class; }
   @Override public String name(){return "Demon Lord — Yog-Dzewa";}
   @Override public String description(){return "A forma demoníaca de Yog-Dzewa, com todos os seus guardiões e fases finais.";}
 }
}