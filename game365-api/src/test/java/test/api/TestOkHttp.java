package test.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wz.cashloan.core.common.util.MD5;
import com.wz.cashloan.core.common.util.MapUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOkHttp {
	public static String paramsString(Map<String,Object> map) {
		Map<String, Object> rec = MapUtil.simpleSort(map);
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Object> entry : rec.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			sb.append(name + "=" + value).append("|");
		}

		if (sb.length() > 1)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	public static void main(String[] args) {
		OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
		Map<String, Object> hashMap = new HashMap<>();
		hashMap.put("deviceId", "7a24850535f25ef4");
		hashMap.put("userId", 4);
		hashMap.put("deviceId", "7a24850535f25ef4");
		hashMap.put("versionNumber", "a");
		String sign = MD5.md5(paramsString(hashMap), "oQIhAP24Kb3Bsf7IE14wpl751bQc9VAPsFZ+LdB4riBgg2TDAiEAsSomOO1v8mK2VWhEQh6mttgN");
		//post方式提交的数据
        Builder bulider = new FormBody.Builder().add("signMsg", sign);
		for(Map.Entry<String, Object> entry : hashMap.entrySet()){
			bulider.add(entry.getKey(), entry.getValue().toString());
        }
		FormBody formBody = bulider.build();
        final Request request = new Request.Builder()
                .url("http://47.92.137.197/api/index/extension/count.htm")//请求的url
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(formBody)
                .build();

        
        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
                    System.out.println(response.body().string());
                }
                call.cancel();
            }
        });
	}
}
