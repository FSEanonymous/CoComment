package codeparsing;

/**
 * 函数调用语句保存的信息
 */
public class FuncCallInfo {
    public String file; //文件名
    public int location;//行号
    public String statement; //具体调用语句内容
    public String name; //函数名
    public FuncCallInfo(){}

    public FuncCallInfo(String file,int location,String statement,String name){
        this.file = file;
        this.location = location;
        this.statement = statement;
        this.name = name;
    }
}
