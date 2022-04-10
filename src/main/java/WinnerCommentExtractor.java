import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.Config;
import org.apache.commons.io.FileUtils;
import util.ReadWriteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 抽取出获奖队伍的注释，后续将其用百度翻译翻译成英文
 */
public class WinnerCommentExtractor {
    public static String []winnerTeams = {
            "我想毕业",
            "斫码",
            "共产主义接班人",
            "芜湖起飞队",
            "蒙的都队",
            "发际线上扬战队",
            "共同学习组",
            "biubiubiu",
            "秃头扛把子",
            "Clown",
            "啊这",
            "lzugcers",
            "随便小分队",
            "山西老陈醋",
            "隐秘的小马驹",
            "大佬说的都队",
            "学习小分队",
            "有手就行",
            "Codeyouth",
            "程序员鼓励师团队",
            "KK",
            "五个菜没有酒",
            "星星之火",
            "TW",
            "SentimentTech",
            "一看就会",
            "忘得差不多队",
            "玄之",
            "代码溯源",
            "程序猿小分队",
            "OneCoin",
            "SHZYJBR"
    };

    public static void main(String args[]) throws Exception{
        String Path = Config.annotateResultPath;
        String jsonString = FileUtils.readFileToString(new File(Path));
        JSONObject data = JSONObject.parseObject(jsonString);
        JSONArray array = data.getJSONArray("RECORDS");
        HashMap<String, HashMap<String,String>> openGuassComments = new HashMap<>();
        HashMap<String, HashMap<String,String>> mindsporeComments = new HashMap<>();
        HashMap<String, HashMap<String,String>> iSuladComments = new HashMap<>();
        List<String> Teams = new ArrayList<>();
        for(String t:winnerTeams){
            Teams.add(t);
        }

        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);

            String is_to_comp = jo.getString("is_to_comp");
            if(!is_to_comp.equals("1"))continue;
            String team_name = jo.getString("team_name");
            if(!Teams.contains(team_name))continue;

            String path = jo.getString("path");
            String linenum = jo.getString("linenum");
            String team_id = jo.getString("team_id");
            String team = team_id + "_" + team_name;
            String content = jo.getString("content");

            String name = jo.getString("name");

            if(name.equals("openGauss-server")){
                if(openGuassComments.containsKey(path+"@"+linenum)){
                    openGuassComments.get(path+"@"+linenum).put(team,content);
                }else{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(team,content);
                    openGuassComments.put(path+"@"+linenum,map);
                }
            }else if(name.equals("mindspore")){
                if(mindsporeComments.containsKey(path+"@"+linenum)){
                    mindsporeComments.get(path+"@"+linenum).put(team,content);
                }else{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(team,content);
                    mindsporeComments.put(path+"@"+linenum,map);
                }
            }else if(name.equals("iSulad")){
                if(iSuladComments.containsKey(path+"@"+linenum)){
                    iSuladComments.get(path+"@"+linenum).put(team,content);
                }else{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(team,content);
                    iSuladComments.put(path+"@"+linenum,map);
                }
            }
        }

        ReadWriteUtil.writeFile(Config.openGaussWinnerCommentPath, JSON.toJSONString(openGuassComments, true));
        ReadWriteUtil.writeFile(Config.mindsporeWinnerCommentPath, JSON.toJSONString(mindsporeComments, true));
        ReadWriteUtil.writeFile(Config.iSuladWinnerCommentPath, JSON.toJSONString(iSuladComments, true));
    }
}
