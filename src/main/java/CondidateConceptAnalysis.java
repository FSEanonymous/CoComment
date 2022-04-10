import codeparsing.*;
import config.openGaussConfig;
import javafx.util.Pair;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.parser.ParserLanguage;
import util.CoreNLP;
import util.NLPUtil;
import java.io.File;
import java.util.*;
import static codeparsing.FuncDefineExtractor.getComment;
import static codeparsing.FuncDefineExtractor.getVarNames;

public class CondidateConceptAnalysis {
    public static int fileNum = 0;
    public static int funcNum = 0;
    public static List<FuncDefineInfo> funcDefineInfos = new ArrayList<>();
    public static List<FuncCallInfo> funcCalls = new ArrayList<>();

    public static void ParseCode(String sourceFile){
        ASTTranslationUnitCore astTranslationUnitCore = new ASTTranslationUnitCore();
        IASTTranslationUnit astTranslationUnit = astTranslationUnitCore.parse(sourceFile, ParserLanguage.CPP, false, false);

        FuncDefineVisitor visitor = new FuncDefineVisitor();
        astTranslationUnit.accept(visitor);
        List<IASTFunctionDefinition> functions = visitor.getFunctions();
        List<IASTComment> comments = visitor.getComments();
        List<Pair<Integer,String>>varNames = visitor.getVarNames();

        for(IASTFunctionDefinition f:functions){
            funcNum++;
            int loc = f.getFileLocation().getStartingLineNumber();
            String comment = getComment(f,comments);
            if(comment.equals(""))continue;
            List<String>varName = getVarNames(f,varNames);
            funcDefineInfos.add(new FuncDefineInfo(sourceFile,loc,f.getDeclarator().getName().toString(),comment,varName));
        }

        FuncCallVisitor visitor1 = new FuncCallVisitor();
        astTranslationUnit.accept(visitor1);

        List<IASTExpression> calls = visitor1.getFuncCalls();

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

    public static void main(String args[])throws Exception{

        Parse(openGaussConfig.codePath);

        HashMap<String,List<String>> token2file = new HashMap<>();
        HashMap<String,Integer> token2Num = new HashMap<>();
        HashMap<String,Integer> token2function = new HashMap<>();
        HashMap<String,Integer> token2Location = new HashMap<>();


        for(FuncDefineInfo info:funcDefineInfos){
            List<String>tokens = NLPUtil.camelSplit(NLPUtil.snakeSplit(info.name));
            for(String s:tokens){
                if(token2Num.containsKey(s.toLowerCase())){
                    token2Num.put(s.toLowerCase(),token2Num.get(s.toLowerCase())+1);
                }else{
                    token2Num.put(s.toLowerCase(),1);
                }

                if(token2file.containsKey(s.toLowerCase())){
                    if(!token2file.get(s.toLowerCase()).contains(info.file)){
                        token2file.get(s.toLowerCase()).add(info.file);
                    }
                }else{
                    List<String>files = new ArrayList<>();
                    files.add(info.file);
                    token2file.put(s.toLowerCase(),files);
                }

                if(token2function.containsKey(s.toLowerCase())){
                    token2function.put(s.toLowerCase(),token2function.get(s.toLowerCase())+1);
                }else{
                    token2function.put(s.toLowerCase(),1);
                }

                if(token2Location.containsKey(s.toLowerCase())){
                    token2Location.put(s.toLowerCase(),token2Location.get(s.toLowerCase())+1);
                }else{
                    token2Location.put(s.toLowerCase(),1);
                }
            }
        }

        for(FuncCallInfo info:funcCalls){
            List<String>tokens = NLPUtil.camelSplit(NLPUtil.snakeSplit(info.name));
            for(String s:tokens){
                if(token2Num.containsKey(s.toLowerCase())){
                    token2Num.put(s.toLowerCase(),token2Num.get(s.toLowerCase())+1);
                }else{
                    token2Num.put(s.toLowerCase(),1);
                }

                if(token2file.containsKey(s.toLowerCase())){
                    if(!token2file.get(s.toLowerCase()).contains(info.file)){
                        token2file.get(s.toLowerCase()).add(info.file);
                    }
                }else{
                    List<String>files = new ArrayList<>();
                    files.add(info.file);
                    token2file.put(s.toLowerCase(),files);
                }

                if(token2Location.containsKey(s.toLowerCase())){
                    token2Location.put(s.toLowerCase(),token2Location.get(s.toLowerCase())+1);
                }else{
                    token2Location.put(s.toLowerCase(),1);
                }
            }
        }



        List<Map.Entry<String,Integer>> result = new ArrayList<>(token2Num.entrySet());
        List<Map.Entry<String,Integer>> result1 = new ArrayList<>();
        for(Map.Entry<String,Integer>entry:result){
            if(!token2function.containsKey(entry.getKey()))continue;
            if(CoreNLP.getPos(entry.getKey()).startsWith("N")==false)continue;
            result1.add(entry);
        }

        result = result1;


        result.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }
        });


        for(Map.Entry<String,Integer>entry:result){
            System.out.println(entry.getKey());
        }
    }
}