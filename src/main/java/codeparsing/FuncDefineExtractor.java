package codeparsing;

import com.alibaba.fastjson.JSON;
import config.iSuladConfig;
import config.mindSporeConfig;
import config.openGaussConfig;
import javafx.util.Pair;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.parser.ParserLanguage;
import util.ReadWriteUtil;

import java.io.File;
import java.util.*;

public class FuncDefineExtractor {
    public static int fileNum = 0;
    public static List<FuncDefineInfo> funcDefineInfos = new ArrayList<>();
    public static void ParseCode(String sourceFile){
        ASTTranslationUnitCore astTranslationUnitCore = new ASTTranslationUnitCore();
        IASTTranslationUnit astTranslationUnit = astTranslationUnitCore.parse(sourceFile, ParserLanguage.CPP, false, false);
        FuncDefineVisitor visitor = new FuncDefineVisitor();
        astTranslationUnit.accept(visitor);
        List<IASTFunctionDefinition> functions = visitor.getFunctions();
        List<IASTComment> comments = visitor.getComments();
        List<Pair<Integer,String>>varNames = visitor.getVarNames();

        for(IASTFunctionDefinition f:functions){
            int loc = f.getFileLocation().getStartingLineNumber();
            String comment = getComment(f,comments);
            List<String>varName = getVarNames(f,varNames);
            funcDefineInfos.add(new FuncDefineInfo(sourceFile,loc,f.getDeclarator().getName().toString(),comment,varName));
        }
    }

    public static String getComment(IASTFunctionDefinition function,List<IASTComment> comments){
        String result = "";
        int fstart = function.getFileLocation().getStartingLineNumber();
        for(IASTComment comment:comments){
            int cend = comment.getFileLocation().getEndingLineNumber();
            if(fstart == cend+1){
                return comment.getRawSignature();
            }
        }
        return result;
    }

    public static List<String>getVarNames(IASTFunctionDefinition f,List<Pair<Integer,String>>varNames){
        List<String>result = new ArrayList<>();
        int start = f.getFileLocation().getStartingLineNumber();
        int end = f.getFileLocation().getEndingLineNumber();
        for(Pair<Integer,String>var:varNames){
            if(var.getKey()>=start && var.getKey()<=end && !var.getValue().equals(f.getDeclarator().getName().toString())){
                result.add(var.getValue());
            }
        }
        return result;
    }

    public static void Parse(String path){
        File f = new File(path);
        if(f.isDirectory()){
            for(File fs:f.listFiles()){
                Parse(fs.getAbsolutePath());
            }
        }else{
            if(f.getAbsolutePath().endsWith(".cpp")|| f.getName().endsWith("c")){
                fileNum++;
                try{
                    ParseCode(f.getAbsolutePath());
                }catch (Exception e){
                    System.out.println("parse file fail!");
                    System.out.println(f.getAbsolutePath());
                }

            }
        }
    }

    public static void mindSporeFuncDefineExtract(){
        Parse(mindSporeConfig.codePath1);
        Parse(mindSporeConfig.codePath2);
        HashMap<String, FuncDefineInfo> map = new HashMap<>();
        for(FuncDefineInfo info: funcDefineInfos){
            map.put(info.name,info);
            System.out.println(info.file);
            System.out.println(info.location);
            System.out.println(info.name);
            System.out.println(info.comment);
            System.out.println(info.varNames);
        }
        ReadWriteUtil.writeFile(mindSporeConfig.funcDefineInfoPath, JSON.toJSONString(map, true) );
    }

    public static void openGaussFuncDefineExtract(){
        Parse(openGaussConfig.codePath);
    }

    public static void iSuladFuncDefineExtract(){
        Parse(iSuladConfig.codePath1);
        Parse(iSuladConfig.codePath2);
        HashMap<String, FuncDefineInfo> map = new HashMap<>();
        for(FuncDefineInfo info: funcDefineInfos){
            map.put(info.name,info);
            System.out.println(info.file);
            System.out.println(info.location);
            System.out.println(info.name);
            System.out.println(info.comment);
            System.out.println(info.varNames);
        }
        ReadWriteUtil.writeFile(iSuladConfig.funcDefineInfoPath, JSON.toJSONString(map, true) );
    }

    public static void main(String args[]){

    }
}
