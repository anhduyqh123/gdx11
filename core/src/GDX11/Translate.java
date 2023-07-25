package GDX11;

import com.badlogic.gdx.utils.JsonValue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Translate {
    public static Translate i = Init();
    private final JsonValue json = new JsonValue(JsonValue.ValueType.object);
    private final Map<String, Runnable> cbChange = new LinkedHashMap<>();
    public String code = "en";//get only
    public List<String> codes;//get only

    public Translate(){
    }
    public Translate(String data)
    {
        String[][] board = Util.ReadCSV(data);
        codes = Arrays.asList(board[0]);
        Util.For(1,board.length-1,i->{
            String[] arr = board[i];
            Util.For(1,arr.length-1,j->Add(arr[0],board[0][j],arr[j]));
        });
    }
    private String Format(String st)
    {
        if (st.equals("")) return st;
        char ch0 = st.charAt(0),ch1 = st.charAt(st.length()-1);
        if (st.contains(",") && ch0=='\"' && ch1=='\"') return st.substring(1,st.length()-2);
        return st;
    }
    private void Add(String key,String code,String value)
    {
        if (!json.has(key)) json.addChild(key,new JsonValue(JsonValue.ValueType.object));
        json.get(key).addChild(code,new JsonValue(Format(value)));
    }
    public void SetCode(int index)
    {
        SetCode(codes.get(index));
    }
    public void SetCode(String code)
    {
        if (!codes.contains(code)) this.code = codes.get(1);
        else this.code = code;
        for(Runnable i : cbChange.values()) i.run();
    }
    public String Get(String key)
    {
        return json.get(key).getString(code);
    }
    public boolean HasKey(String key)
    {
        return json.has(key);
    }
    public void AddChangeCallback(String key,Runnable cb)
    {
        cbChange.put(key,cb);
        cb.run();
    }
    public JsonValue GetData()
    {
        return json;
    }
    public static Translate Init()
    {
        return  GDX.Try(()->new Translate(Asset.i.GetString("translate")),()->new Translate());
    }
}
