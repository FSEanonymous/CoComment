package util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 读写文件辅助类
 * 封装了读写文件的操作接口，给有加载文件或保存文件需求的相关模块调用
 */
public class ReadWriteUtil {

    /**
     *
     * @param path 要读取的文件路径
     * @return  文件内容字符串
     */
    public static String readFile(String path){
        StringBuffer buffer = new StringBuffer();
        try{
            InputStream inStr = new FileInputStream(path);
            InputStreamReader inStrReader = new InputStreamReader(inStr, StandardCharsets.UTF_8);
            BufferedReader bufferreader = new BufferedReader(inStrReader);
            String line;
            while((line=bufferreader.readLine())!=null){
                buffer.append(line);
                buffer.append("\n");
            }
            bufferreader.close();
            inStrReader.close();
            inStr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     *
     * @param path 要写入的文件路径
     * @param s 文件内容字符串
     */
    public static void writeFile(String path,String s){
        try{
//            System.out.println(path);
            File newfile = new File(path);
            File fileParent = newfile.getParentFile();
            if(!fileParent.exists()) {
                fileParent.mkdirs();
            }
            newfile.createNewFile();
            FileOutputStream fos=new FileOutputStream(newfile);
            OutputStreamWriter osw=new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter bw=new BufferedWriter(osw);
            bw.write(s);
            bw.close();
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
