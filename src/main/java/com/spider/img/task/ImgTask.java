package com.spider.img.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.img.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangkai on 17/5/23.
 */
public class ImgTask implements Runnable {
    BlockingQueue<JSONArray> htmlQueue;
    BlockingQueue<Map<String, List>> imgQueue;

    public ImgTask(BlockingQueue htmlQueue, BlockingQueue imgQueue) {
        this.htmlQueue = htmlQueue;
        this.imgQueue = imgQueue;
    }

    public void run() {
        while (true) {
            try {
                JSONArray jsonArray = htmlQueue.take();
                Map<String, List> productMap = new HashMap<String, List>();
                for (Object obj : jsonArray) {
                    JSONObject json = (JSONObject) obj;
                    List<String> imgList = new ArrayList<String>();
                    String title  = json.getString("title");
                    String url = json.getString("url");
                    String id = url.substring(url.indexOf("/search/")+8,url.indexOf("?"));
                    String response = HttpUtil.sendGet(getUrl(id));
                    JSONObject jsonObject = JSON.parseObject(response);
                    JSONArray picArray = jsonObject.getJSONArray("results");
                    for(Object pic : picArray){
                        JSONObject picJson = (JSONObject) pic;
                        String imageUrl = picJson.getString("webImageUrl");
                        imgList.add(imageUrl);
                    }
                    productMap.put(title, imgList);
                    Map<String, List> map =new HashMap<String, List>();
                    map.putAll(productMap);
                    imgQueue.put(map);
                    productMap.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getUrl(String id){
        return String.format("http://www.metmuseum.org/api/Collection/additionalImages?crdId=%s&page=1&perPage=50",id);
    }
}
