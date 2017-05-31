package com.spider.img;

import com.alibaba.fastjson.JSONArray;
import com.spider.img.task.DownloadTask;
import com.spider.img.task.GrabTask;
import com.spider.img.task.ImgTask;
import com.spider.img.util.ConfigUtils;
import com.typesafe.config.Config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangkai on 17/5/23.
 */
public class SpiderServer {
    private final static Config executorConf = ConfigUtils.getConfig("executor");
    private final static Config queueConf = ConfigUtils.getConfig("queue");
    private final static Config targetConf = ConfigUtils.getConfig("target");
    private final static int grabExecutorNum = executorConf.getInt("grabExecutorNum");
    private final static int imgExecutorNum = executorConf.getInt("imgExecutorNum");
    private final static int downloadExecutorNum = executorConf.getInt("downloadExecutorNum");
    private final static int grabQueueNum = queueConf.getInt("grabQueueNum");
    private final static int imgQueueNum = queueConf.getInt("imgQueueNum");
    private Integer offset = targetConf.getInt("offset");
    private Integer perPage = targetConf.getInt("perPage");
    private String url = targetConf.getString("url");
    private String detailUrl = targetConf.getString("detailUrl");

    private ExecutorService grabExecutorService = Executors.newFixedThreadPool(grabExecutorNum);
    private BlockingQueue<JSONArray> grabQueue = new ArrayBlockingQueue(grabQueueNum);


    private ExecutorService imgExecutorService = Executors.newFixedThreadPool(imgExecutorNum);
    private BlockingQueue<Map<String, List>> imgQueue = new ArrayBlockingQueue(imgQueueNum);

    private ExecutorService downloadExecutorService = Executors.newFixedThreadPool(downloadExecutorNum);


    public void start() {

        for (int i = 0; i < grabExecutorNum; i++) {
            grabExecutorService.execute(new GrabTask(grabQueue, url, offset, perPage));
        }

        for (int i = 0; i < imgExecutorNum; i++) {
            imgExecutorService.execute(new ImgTask(grabQueue, imgQueue, detailUrl));
        }

        for (int i = 0; i < downloadExecutorNum; i++) {
            downloadExecutorService.execute(new DownloadTask(imgQueue));
        }

    }

    public static void main(String[] args) {
        new SpiderServer().start();
    }

}
