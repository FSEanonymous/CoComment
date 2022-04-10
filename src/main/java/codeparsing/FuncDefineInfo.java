package codeparsing;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数定义处的保存信息
 */
public class FuncDefineInfo {
    public String file; //文件名
    public int location;//行号
    public String name; //函数名
    public String comment; //代码中已有的注释
    public List<String> varNames = new ArrayList<>(); //函数体中的其他变量名，用于扩展函数名中的缩略词

    public FuncDefineInfo(){}
    public FuncDefineInfo(String file, int location, String name, String comment, List<String>varNames){
        this.file = file;
        this.location = location;
        this.name = name;
        this.comment = comment;
        this.varNames = varNames;
    }
}
