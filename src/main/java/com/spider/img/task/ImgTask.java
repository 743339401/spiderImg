package com.spider.img.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.img.util.HttpUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangkai on 17/5/23.
 */
public class ImgTask implements Runnable {
    BlockingQueue<JSONArray> grabQueue;
    BlockingQueue<Map<String, List>> imgQueue;
    String detailUrl;

    public ImgTask(BlockingQueue grabQueue, BlockingQueue imgQueue, String detailUrl) {
        this.grabQueue = grabQueue;
        this.imgQueue = imgQueue;
        this.detailUrl = detailUrl;
    }

    public void run() {
        while (true) {
            try {
                JSONArray jsonArray = grabQueue.take();
                Map<String, List> productMap = new HashMap<>();
                for (Object obj : jsonArray) {
                    JSONObject json = (JSONObject) obj;
                    List<String> imgList = Arrays.asList();
                    String title = json.getString("title");
                    String url = json.getString("url");
                    String id = url.substring(url.indexOf("/search/") + 8, url.indexOf("?"));
                    String response = HttpUtil.sendGet(getUrl(id));
                    JSONObject jsonObject = JSON.parseObject(response);
                    JSONArray picArray = jsonObject.getJSONArray("results");
                    for (Object pic : picArray) {
                        JSONObject picJson = (JSONObject) pic;
                        String imageUrl = picJson.getString("webImageUrl");
                        imgList.add(imageUrl);
                    }
                    productMap.put(title, imgList);
                    imgQueue.put(new HashMap<>(productMap));
                    productMap.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getUrl(String id) {
        return String.format(detailUrl, id);
    }
}
