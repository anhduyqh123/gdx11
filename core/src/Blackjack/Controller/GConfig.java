package Blackjack.Controller;

import GDX11.GDX;

public class GConfig {
    public int level = GDX.GetPrefInteger("level",1);
    public int exp = GDX.GetPrefInteger("exp",0);
    private GDX.Runnable2<Float,Float> cbExp; //old,new percent
    private GDX.Runnable1<Integer> cbLevel;
    public GDX.Runnable1<Integer> nextLevel;

    private void NextLevel()
    {
        level++;
        GDX.SetPrefInteger("level",1);
        cbLevel.Run(level);
        nextLevel.Run(level);
        exp=0;
        AddExp(0);
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
        GDX.SetPrefInteger("exp",exp);
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
