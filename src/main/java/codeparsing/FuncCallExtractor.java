package codeparsing;

import com.alibaba.fastjson.JSON;
import config.iSuladConfig;
import config.mindSporeConfig;
import config.openGaussConfig;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.parser.ParserLanguage;
import util.ReadWriteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FuncCallExtractor {
    public static List<FuncCallInfo> funcCalls = new ArrayList<>();

    public static void ParseCode(String sourceFile){
        ASTTranslationUnitCore astTranslationUnitCore = new ASTTranslationUnitCore();
        IASTTranslationUnit astTranslationUnit = astTranslationUnitCore.parse(sourceFile, ParserLanguage.CPP, false, false);
        FuncCallVisitor visitor = new FuncCallVisitor();
        astTranslationUnit.accept(visitor);

        List<IASTExpression> calls = visitor.getFuncCalls();

        for(IASTExpression call:calls){
            int loc = call.getFileLocation().getStartingLineNumber();
            String name = call.getRawSignature().split("\\(")[0];
            String statement = call.getRawSignature();
            if(name.trim().equals(""))continue;
            funcCalls.add(new FuncCallInfo(sourceFile,loc,statement,name));
        }
    }

    public static void Parse(String path){
        File f = new File(path);
        if(f.isDirectory()){
            for(File fs:f.listFiles()){
                Parse(fs.getAbsolutePath());
            }
        }else{
            if(f.getAbsolutePath().endsWith(".cpp")||f.getAbsolutePath().endsWith("c")){
                if(f.getAbsolutePath().endsWith("ascend_control_parser.cc")||
                f.getAbsolutePath().endsWith("ascend_session.cc")||
                f.getAbsolutePath().endsWith("kernel_graph.cc")||
                f.getAbsolutePath().endsWith("session_basic.cc")){
                    return;
                }
                try{
                    ParseCode(f.getAbsolutePath());
                }catch (Exception e){
                    System.out.println("parse code fail!");
                    System.out.println(f.getAbsolutePath());
                }

            }
        }
    }

    public static void mindSporeFuncCallExtract(){
        Parse(mindSporeConfig.codePath1);
        Parse(mindSporeConfig.codePath2);
        List<String>calls = new ArrayList<>();
        for(FuncCallInfo callInfo:funcCalls){
            calls.add(callInfo.file+"+"+callInfo.location+"+"+callInfo.name);
        }
        ReadWriteUtil.writeFile(mindSporeConfig.funcCallInfoPath, JSON.toJSONString(calls, true));
    }

    public static void openGaussFuncCallExtract(){
        Parse(openGaussConfig.codePath);
        List<String>calls = new ArrayList<>();
        for(FuncCallInfo callInfo:funcCalls){
            calls.add(callInfo.file+"+"+callInfo.location+"+"+callInfo.name);
        }
        ReadWriteUtil.writeFile(openGaussConfig.funcCallInfoPath, JSON.toJSONString(calls, true));
    }

    public static void iSuladFuncCallExtract(){
        Parse(iSuladConfig.codePath1);
        Parse(iSuladConfig.codePath2);
        List<String>calls = new ArrayList<>();
        for(FuncCallInfo callInfo:funcCalls){
            calls.add(callInfo.file+"+"+callInfo.location+"+"+callInfo.name);
        }
        ReadWriteUtil.writeFile(iSuladConfig.funcCallInfoPath, JSON.toJSONString(calls, true));

    }

    public static void main(String args[]){
        funcCalls = new ArrayList<>();
        mindSporeFuncCallExtract();
        funcCalls = new ArrayList<>();
        openGaussFuncCallExtract();
        funcCalls = new ArrayList<>();
        iSuladFuncCallExtract();
    }
}