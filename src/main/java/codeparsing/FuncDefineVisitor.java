package codeparsing;

import javafx.util.Pair;
import org.eclipse.cdt.core.dom.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于抽取函数定义信息的visitor
 */
public class FuncDefineVisitor extends ASTVisitor {
    private List<IASTFunctionDefinition> functions = new ArrayList<>();//所有函数定义
    private List<IASTComment> comments = new ArrayList<>();//所有函数定义处的注释
    private List<Pair<Integer,String>>varNames = new ArrayList<>();//所有函数中的变量名

    public FuncDefineVisitor(){
        super(true);
        super.shouldVisitAmbiguousNodes = true;
        super.shouldVisitArrayModifiers = true;
        super.shouldVisitBaseSpecifiers = true;
        super.shouldVisitDeclarations = true;
        super.shouldVisitDeclarators = true;
        super.shouldVisitDeclSpecifiers = true;
        super.shouldVisitDesignators = true;
        super.shouldVisitEnumerators = true;
        super.shouldVisitExpressions = true;
        super.shouldVisitImplicitNameAlternates = true;
        super.shouldVisitImplicitNames = true;
        super.shouldVisitInitializers = true;
        super.shouldVisitNames = true;
        super.shouldVisitNamespaces = true;
        super.shouldVisitParameterDeclarations = true;
        super.shouldVisitPointerOperators = true;
        super.shouldVisitProblems = true;
        super.shouldVisitStatements = true;
        super.shouldVisitTemplateParameters = true;
        super.shouldVisitTranslationUnit = true;
        super.shouldVisitTypeIds = true;
    }

    public int visit(IASTDeclaration declaration){
        if(declaration instanceof IASTFunctionDefinition){
            functions.add((IASTFunctionDefinition)declaration);
        }
        return PROCESS_CONTINUE;
    }

    public int visit(IASTTranslationUnit unit){
        IASTComment[] Comments = unit.getComments();
        for(IASTComment comment:Comments){
            comments.add(comment);
        }
        return PROCESS_CONTINUE;
    }

    public int visit(IASTName name){
        varNames.add(new Pair(name.getParent().getFileLocation().getStartingLineNumber(),name.toString()));
        return PROCESS_CONTINUE;
    }

    public List<IASTFunctionDefinition> getFunctions(){
        return functions;
    }

    public List<IASTComment> getComments(){
        return comments;
    }

    public List<Pair<Integer,String>>getVarNames(){
        return varNames;
    }
}
