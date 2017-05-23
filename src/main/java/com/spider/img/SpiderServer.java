package com.spider.img;

import com.alibaba.fastjson.JSONArray;
import com.spider.img.task.DownloadTask;
import com.spider.img.task.HtmlTask;
import com.spider.img.task.ImgTask;

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
    private final int htmlExecutorNum = 15;
    private final int imgExecutorNum = 50;
    private final int downloadExecutorNum = 35;

    private ExecutorService htmlExecutorService = Executors.newFixedThreadPool(htmlExecutorNum);
    private BlockingQueue<JSONArray> htmlQueue = new ArrayBlockingQueue(100);
    private Integer offset = 0;
    private Integer perPage = 50;

    private ExecutorService imgExecutorService = Executors.newFixedThreadPool(imgExecutorNum);
    private BlockingQueue<Map<String, List>> imgQueue = new ArrayBlockingQueue(1000);

    private ExecutorService downloadExecutorService = Executors.newFixedThreadPool(downloadExecutorNum);


    public void start() {
        for (int i = 0; i < htmlExecutorNum; i++) {
            htmlExecutorService.execute(new HtmlTask(htmlQueue, offset, perPage));
        }

        for (int i = 0; i < imgExecutorNum; i++) {
            imgExecutorService.execute(new ImgTask(htmlQueue, imgQueue));
        }

        for (int i = 0; i < downloadExecutorNum; i++) {
            downloadExecutorService.execute(new DownloadTask(imgQueue));
        }

    }

    public static void main(String[] args) {
        new SpiderServer().start();
    }

}
