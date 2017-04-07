package com.suclogger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suclogger.domain.Oxx;
import com.suclogger.repository.OxxRepository;
import com.suclogger.utils.ThreadPoolFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class OxxspiderApplication implements CommandLineRunner {

    @Autowired
    private OxxRepository repo;

    @Override
    public void run(String... strings) throws Exception {
        for(int i=1; i<21; i++) {
            ThreadPoolFactory.getThreadPool().execute(new OxxTask(i, repo));
        }
    }

    class OxxTask implements Runnable {

        private Integer pageNo;

        private OxxRepository repo;

        public OxxTask() {
        }

        public OxxTask(Integer pageNo, OxxRepository repo) {
            this.pageNo = pageNo;
            this.repo = repo;
        }

        @Override
        public void run() {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                // 创建httpget.
                HttpGet httpget = new HttpGet("https://oxxvideo.com/?g=&page=" + pageNo + "&pagesize=500&datatype=json");
                // 执行get请求.
                CloseableHttpResponse response = httpclient.execute(httpget);
                try {
                    // 获取响应实体
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String content = EntityUtils.toString(entity);
                        JSONObject parent = JSON.parseObject(content);
                        JSONArray jsonArray = parent.getJSONArray("data");
                        System.out.println("pageNo : " + pageNo + ", size : " + jsonArray.size());
                        for(Object tmp : jsonArray) {
                            JSONObject jtmp = (JSONObject)tmp;
                            List<Oxx> existOxx = repo.findOxxesByVideoId(jtmp.getString("id"));
                            if(CollectionUtils.isEmpty(existOxx)) {
                                Oxx oxx = new Oxx(jtmp.getString("id"),
                                        jtmp.getString("title"),
                                        jtmp.getString("description"),
                                        jtmp.getString("length"),
                                        jtmp.getInteger("viewTimes"),
                                        new Date());
                                repo.save(oxx);
                            }
                        }
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭连接,释放资源
                try {
                    httpclient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "/Users/suclogger/MyWorkspace/oxxspider/src/main/resources/cert/jssecacerts");
        SpringApplication.run(OxxspiderApplication.class, args);
    }
}
