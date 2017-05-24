package com.spider.img.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.img.util.HttpUtil;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangkai on 17/5/23.
 */
public class HtmlTask implements Runnable {

    Integer offset;
    Integer perPage;
    BlockingQueue<JSONArray> htmlQueue;

    public HtmlTask(BlockingQueue htmlQueue, Integer offset, Integer perPage) {
        this.htmlQueue = htmlQueue;
        this.offset = offset;
        this.perPage = perPage;
    }

    public void run() {
        while (true) {
            try {
                String response = HttpUtil.sendGet(getUrl());
                JSONObject json = JSON.parseObject(response);
                JSONArray jsonArray = json.getJSONArray("results");
                htmlQueue.put(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public synchronized String getUrl() {
        String url = String.format("http://www.metmuseum.org/api/collection/collectionlisting?artist=&department=&era=&geolocation=&material=&offset=%d&pageSize=0&perPage=%d&q=china&showOnly=&sortBy=Relevance&sortOrder=asc", offset, perPage);
        offset += perPage;
        return url;
    }
}
