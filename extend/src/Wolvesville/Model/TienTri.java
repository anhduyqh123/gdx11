package Wolvesville.Model;

public class TienTri extends HumanX{
    public TienTri() {
        super("Tiên Tri","Muốn soi");
    }
    @Override
    public boolean VoHieuBoiGiaLangDie() {
        return gialang.Valid() && gialang.Die();
    }
}
