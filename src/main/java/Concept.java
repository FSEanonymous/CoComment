public class Concept {
    public String name;
    public int type;    //0 NP; 1 NS;
    public String NP;
    public String DS;
    public Concept(String name,int type,String NP,String DS){
        this.name = name;
        this.NP = NP;
        this.DS = DS;
        this.type = type;
    }
}
