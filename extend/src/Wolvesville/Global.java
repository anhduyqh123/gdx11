package Wolvesville;

import Extend.IDropDown;
import GDX11.Config;

import java.util.*;

public interface Global {
    String cupid = "CUPID",tientri="TIÊN TRI",baove = "BẢO VỆ",thosan = "THỢ SĂN",
            phuthuy = "PHÙ THỦY",gialang = "GIÀ LÀNG",thoisao = "THỔI SÁO",bansoi = "BÁN SÓI",hiepsi = "HIỆP SĨ",
            chandoi = "CHÁN ĐỜI",nguyetnu="NGUYỆT NỮ",silence = "SILENCE";
    List<String> allFunc = Arrays.asList(cupid,tientri,baove,thosan,phuthuy,gialang,thoisao,bansoi,hiepsi,chandoi,nguyetnu,silence);
    List<String> func = new ArrayList<>();
    List<String> allName = new ArrayList<>();//name
    List<String> leftName = new ArrayList<>();
    List<String> human = new ArrayList<>();
    List<String> wolves = new ArrayList<>();
    List<String> alive = new ArrayList<>();
    List<String> biThoisao = new ArrayList<>();
    List<String> pair = new ArrayList<>();
    Map<String,String> map = new HashMap<>();
    Map<String,String> targetMap = new HashMap<>();

    static void Reset(){

    }

    default String GetFuncString(String name){
        if (name.equals(nguyetnu)) return "Muốn vô hiệu";
        if (name.equals(silence)) return "Muốn câm lặng";
        if (name.equals(tientri)) return "Muốn soi";
        if (name.equals(thosan)) return "Chọn mục tiêu";
        return "Muốn bảo vệ";
    }
    default void SetMainDropdown(String name, IDropDown iDropDown){
        iDropDown.onSelect = vl->map.put(name,vl);
        iDropDown.SetItems(leftName);
        iDropDown.SetSelected(leftName.get(0));
    }
    default void SetSoiCount(int count){
        Config.Set("soiCount",count);
    }
    default int GetSoiCount(){
        return Config.Get("soiCount");
    }
    default String GetSoiTarget(){
        return targetMap.get("soi");
    }
    default void SetSoiTarget(String name){
        targetMap.put("soi",name);
    }
    default List<String> GetDeathHuman(){
        List<String> list = new ArrayList<>();
        String dead = GetSoiTarget();//bị sói cắn
        return list;
    }
    default boolean WillDead(String name){
        if (targetMap.get(baove).equals(name))return false;//dc bao ve
        if (targetMap.get(baove).equals(name))return false;//dc bao ve
        return true;
    }
}
