package DrinkGame.Game.Tiktaktoe;

import GDX11.GDX;

import java.util.*;

public class Board {
    private final static List<List<String>> matches = new ArrayList<>();//slots
    private final static HashSet<String> allSlot = new HashSet<>();
    private final static Map<String,List<String>> connect = new HashMap<>();

    public static void Clear(){
        matches.clear();
        allSlot.clear();
        connect.clear();
    }
    public static void SetMatch(List<String> list) {
        for (String stMatch : list) {//1,2,3
            List<String> match = Arrays.asList(stMatch.split(","));
            matches.add(Arrays.asList(stMatch.split(",")));
            allSlot.addAll(match);
        }
    }
    public static void SetConnect(String slot,List<String> list) {
        allSlot.add(slot);
        connect.put(slot,list);
    }

    private final Map<String,Integer> map = new HashMap<>();//key=slot,value=1,2;
    private final List<String> list1 = new ArrayList<>();//slots
    private final List<String> list2 = new ArrayList<>();//slots
    private int matchSize = 3;

    public Board(){
        this(3);
    }
    public Board(int matchSize){
        this.matchSize = matchSize;
        for (String slot : allSlot) map.put(slot,0);
    }
    public Board(Board board) {
        matchSize = board.matchSize;
        list1.addAll(board.list1);
        list2.addAll(board.list2);
        map.putAll(board.map);
    }
    public void Reset(){
        for (String slot : allSlot) map.put(slot,0);
        list1.clear();
        list2.clear();
    }
    public void Set(int playerID,String fromSlot,String toSlot) {
        if (fromSlot!=null) map.put(fromSlot,0);
        map.put(toSlot,playerID);
        GetList(playerID).remove(fromSlot);
        GetList(playerID).add(toSlot);
    }
    private List<String> GetList(int playerID) {
        return playerID==1?list1:list2;
    }
    private int GetOpID(int playerID) {
        return playerID==1?2:1;
    }
    //Step
    public String NewStep(int playerID) {
        List<Node> nodes = new ArrayList<>();
        for (String slot : GetEmptySlots()) {
            Node node = new Node(playerID,null,slot,new Board(this));
            node.Run();
            nodes.add(node);
        }
        return GetNode(nodes).toSlot;
    }
    public List<String> GetEmptySlots() {
        List<String> list = new ArrayList<>();
        for (String slot : map.keySet())
            if (map.get(slot)==0) list.add(slot);
        return list;
    }
    //result
    private Node GetNode(List<Node> nodes)
    {
        Collections.sort(nodes,(o1,o2)->{
            if (o1.score<o2.score) return 1;
            if (o1.score>o2.score) return -1;
            if (o1.score1<o2.score1) return -1;
            if (o1.score1>o2.score1) return 1;
            return 0;
        });
        List<Node> best = new ArrayList<>();
        for (Node n : nodes)
            if (n.Equal(nodes.get(0))) best.add(n);
        Collections.shuffle(best);
        //ShowNodes(best);
        return best.get(0);
    }
    //GetScore
    private float GetScore(int playerID,float ex,String toSlot) {
        float score = ex;
        for (List<String> match : matches)
            score+=GetScore(playerID,match,ex,toSlot);
        return score;
    }
    private int GetScore(int playerID,List<String> match,float ex,String toSlot){//match3
        int count = 0;
        for (String slot : match) {
            if (map.get(slot)==0) continue;
            if (map.get(slot)!=playerID) return 0;
            count+=1;
        }
        if (count>=3 && !match.contains(toSlot)) return 0;
        return count==0?0:(int)Math.pow(10,count-1+ex);
    }
    //Check Win
    public List<String> GetMatch(int playerID){
        for (List<String> match : matches)
            if (GetList(playerID).containsAll(match)) return match;
        return null;
    }
    public static class Node{
        public String fromSlot,toSlot;
        public int playerID;
        public float score,score1;
        private Board board;
        public Node(int playerID, String fromSlot, String toSlot, Board board){
            this.playerID = playerID;
            this.fromSlot = fromSlot;
            this.toSlot = toSlot;
            this.board = board;
        }
        public void Run()
        {
            board.Set(playerID,fromSlot,toSlot);
            score = board.GetScore(playerID,0,toSlot);
            score1 = board.GetScore(board.GetOpID(playerID),0,toSlot);
            if (score<100 && score1>=10) score=0;
        }
        public String ToString()
        {
            return playerID +":"+toSlot+"_"+(int)score+"_"+(int)score1;
        }
        public boolean Equal(Node node)
        {
            return score== node.score && score1==node.score1;
        }
    }
}
