package com.spider.img.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.img.util.HttpUtil;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangkai on 17/5/23.
 */
public class GrabTask implements Runnable {

    String url;
    Integer offset;
    Integer perPage;
    BlockingQueue<JSONArray> htmlQueue;

    public GrabTask(BlockingQueue htmlQueue, String url, Integer offset, Integer perPage) {
        this.htmlQueue = htmlQueue;
        this.url = url;
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
        String u = String.format(url, offset, perPage);
        offset += perPage;
        return u;
    }
}
