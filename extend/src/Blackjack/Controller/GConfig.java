package Blackjack.Controller;

import Extend.XItem;
import GDX11.GDX;
import GDX11.Util;

public class GConfig {
    private static final int[] unlockTheme = {1,10,20,30,50,80};
    public static int GetUnlockLevel(int id)
    {
        return unlockTheme[id];
    }
    public static void CheckUnlock(int level)
    {
        Util.For(0,5,i->{
            if (unlockTheme[i]==level)
                GDX.i.SetPrefBoolean("theme"+i,true);
        });
    }
    public static boolean IsUnlockTheme(int id)
    {
        return GDX.i.GetPrefBoolean("theme"+id,false);
    }
    private static final int[] minX = {10,500,20000,1000000,5000000,2000000000};
    public static int GetMinCoin(int id)
    {
        return minX[id];
    }
    public static int GetCoin(int index)
    {
        int min = minX[XItem.Get("theme").value];
        if (index==0) return min;
        int preCoin = GetCoin(index-1);
        String st = preCoin+"";
        if (st.charAt(0)!='2') return preCoin*2;
        return preCoin*2+preCoin/2;
    }

    public int level = GDX.i.GetPrefInteger("level",1);
    public int exp = GDX.i.GetPrefInteger("exp",0);
    private GDX.Runnable2<Float,Float> cbExp; //old,new percent
    private GDX.Runnable1<Integer> cbLevel;
    public GDX.Runnable1<Integer> nextLevel;

    private void NextLevel()
    {
        level++;
        GDX.i.SetPrefInteger("level",level);
        cbLevel.Run(level);
        nextLevel.Run(level);
        exp=0;
        AddExp(0);
        CheckUnlock(level);
    }
    public void SetLevelEvent(GDX.Runnable1<Integer> cbLevel)
    {
        this.cbLevel = cbLevel;
        cbLevel.Run(level);
    }

    public void SetExpEvent(GDX.Runnable2<Float,Float> cbExp)
    {
        this.cbExp = cbExp;
        float p = exp*1f/GetMaxExp(level);
        cbExp.Run(p,p);
    }
    private void AddExp(int value)
    {
        float maxExp = GetMaxExp(level);
        cbExp.Run(exp/maxExp,(exp+value)/maxExp);
        exp+=value;
        GDX.i.SetPrefInteger("exp",exp);
    }
    private int GetMaxExp(int level)
    {
        return level*5+10;
    }
    public int GetChipReward(int level)
    {
        return 50+50*level;
    }
    public void CheckNextLevel()
    {
        if (exp>=GetMaxExp(level)) NextLevel();
    }
    public void Win(){
        AddExp(5);
    }
    public void Lose()
    {
        AddExp(2);
    }
    public void Push()
    {
        AddExp(4);
    }
}
