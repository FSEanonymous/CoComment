package translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import config.Config;
import org.apache.commons.io.FileUtils;
import util.ReadWriteUtil;

import java.io.File;
import java.util.HashMap;

/**
 * 负责将获奖的参赛队伍的标注用百度翻译API翻译成英文
 */
public class Translate {
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20210122000678363";
    private static final String SECURITY_KEY = "tP80NSsBmWYPo0UmNY1M";

    public static void translate(String commentPath,String englishCommentPath){
        try{
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);

            String map =  FileUtils.readFileToString(new File(commentPath));
            HashMap<String, HashMap<String,String>> comments = JSON.parseObject(map, new TypeReference<HashMap<String, HashMap<String,String>>>() {});

            for(String loc:comments.keySet()){
                HashMap<String,String>TeamComment = comments.get(loc);
                for(String t:TeamComment.keySet()){
                    String prevComment = TeamComment.get(t);
                    try{
                        String result = api.getTransResult(prevComment, "auto", "en");
                        Object transResult = JSONObject.parseObject(result).get("trans_result");
                        System.out.println(result);
                        JSONArray arr = JSONArray.parseArray(transResult.toString());
                        String obj = arr.getJSONObject(0).getString("dst");
                        System.out.println(obj);
                        TeamComment.put(t,obj);
                        Thread.sleep(1000); //中间停止1s，防止因为网络延迟使得api调用失败
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            ReadWriteUtil.writeFile(englishCommentPath, JSON.toJSONString(comments, true));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        translate(Config.openGaussWinnerCommentPath, Config.openGaussWinnerEnglishCommentPath);
        translate(Config.mindsporeWinnerCommentPath, Config.mindsporeWinnerEnglishCommentPath);
        translate(Config.iSuladWinnerCommentPath, Config.iSuladWinnerEnglishCommentPath);
    }
}
