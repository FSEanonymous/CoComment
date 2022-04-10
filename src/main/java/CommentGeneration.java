import util.NLPUtil;

import java.util.ArrayList;
import java.util.List;

public class CommentGeneration {
    public static String generate(List<String>methodSplitResult,List<Concept>concepts){
        StringBuilder comment = new StringBuilder("");
        StringBuilder supplementary = new StringBuilder("");
        for(String s:methodSplitResult){
            boolean isConcept = false;
            for(Concept c:concepts){
                if(c.name.equals(s)){
                    comment.append(c.NP);
                    isConcept = true;
                    if(c.type==1){
                        supplementary.append(". ");
                        supplementary.append(c.DS);
                    }
                    break;
                }
            }
            if(isConcept==false)
                comment.append(s);
            comment.append(" ");
        }

        comment.append(supplementary.toString());
        comment.append(".");
        return comment.toString();
    }

    public static void main(String args[]){
        Concept c1 = new Concept("xlog",1,"transaction log","A logocal node can hava only one .xlog file");
        Concept c2 = new Concept("cmp",0,"compare","");
        Concept c3 = new Concept("lsn",0,"log sequence number","");
        List<Concept>list1 = new ArrayList<>();
        list1.add(c1);

        List<Concept>list2 = new ArrayList<>();
        list2.add(c2);
        list2.add(c3);

        String method1 = "logical_read_xlog_page";
        String method2 = "cmp_lsn";

        System.out.println(generate(NLPUtil.camelSplit(NLPUtil.snakeSplit(method1)),list1));
        System.out.println(generate(NLPUtil.camelSplit(NLPUtil.snakeSplit(method2)),list2));
    }
}