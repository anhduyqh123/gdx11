package Wolvesville.Model;

public class BaoVe extends HumanX{
    public BaoVe() {
        super("Bảo Vệ","Muốn bảo vệ");
    }
    public void Run(){
        getCard.Run(target).DuocBaoVe();
    }
}
