package tv.newtv.screening.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

/**
 * create by zhaikn on 2018/12/3
 */

public class httpClient {
    private static final String TAG = "httpClient_udpComDemo";
    private static final int TIME_OUT = 10 * 1000;
    private static final String CHARSET = "utf-8";

    /**
     * 上传文件
     * @param file
     * @param postUrl
     * @return 服务器响应信息
     */
    public static String uploadFile(File file, String postUrl){
        Log.d(TAG,"uploadFile start");
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);


            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"postFile\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                // if(res==200)
                // {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Log.e(TAG, "result : " + result);
                // }
                // else{
                // Log.e(TAG, "request error");
                // }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String httpPostCommand(String address, String data) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            // 至少要设置的两个请求头
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Content-Length", data.length() + "");

            // post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream(); // getOutputStream会隐含的进行connect
            /**
             * JSON数据的请求 outputStream.write(stringJson.getBytes(), 0,
             * stringJson.getBytes().length); outputStream.close();
             **/
            outputStream.write(data.getBytes());
            Log.i(TAG, "POST URL : " + address);
            Log.i(TAG, "POST paramdata:" + data);

            // 获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // 请求成功
                // InputStream is = connection.getInputStream();
                // 读取返回的数据
                // 返回打开连接读取的输入流，输入流转化为StringBuffer类型，这一套流程要记住，常用
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    // 转化为UTF-8的编码格式
                    line = new String(line.getBytes("UTF-8"));
                    stringBuffer.append(line);
                }
//				Log.i(LOG_TAG,"POST respone data:" + stringBuffer.toString());
                bufferedReader.close();
                connection.disconnect();

//				return mAppsInfo.toString();
                Log.i(TAG,"--------http server response :" + stringBuffer.toString());
                return stringBuffer.toString();// IOSUtil.inputStream2String(is);
            } else {
                // 请求失败
                Log.i(TAG, "Access server error,errorCode：" + responseCode);
//				return mAppsInfo.toString();
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
