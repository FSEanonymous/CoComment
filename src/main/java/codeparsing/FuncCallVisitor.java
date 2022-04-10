package codeparsing;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于抽取函数调用信息的visitor
 */
public class FuncCallVisitor extends ASTVisitor {
    private List<IASTExpression> funcCalls = new ArrayList<>();
    public FuncCallVisitor(){
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

    public int visit(IASTExpression expression){
        if(expression instanceof IASTFunctionCallExpression){
            funcCalls.add(expression);
        }
        return PROCESS_CONTINUE;
    }

    public List<IASTExpression> getFuncCalls(){
        return funcCalls;
    }
}
