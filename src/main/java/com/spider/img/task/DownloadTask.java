package com.spider.img.task;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangkai on 17/5/23.
 */
public class DownloadTask implements Runnable {

    BlockingQueue<Map<String,List>> imgQueue;
    public DownloadTask(BlockingQueue imgQueue) {
        this.imgQueue = imgQueue;
    }

    public void run() {
        while (true){
            try {
                Map<String, List> productMap = imgQueue.take();
                for (Map.Entry<String ,List> map : productMap.entrySet()){
                    downloadPicture(map.getKey(), map.getValue());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void downloadPicture(String title,List<String> urlList) {
        URL url = null;
        int imageNumber = 1;
        String downloadDir = "/Users/maxent/Documents/images/"+title;
        File downloadFilePath = new File(downloadDir);
        if(!downloadFilePath.exists()) downloadFilePath.mkdirs();

        for (String urlString : urlList) {
            try {
                url = new URL(urlString);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                String imageName = downloadDir + "/" + title + imageNumber + ".jpg";
                FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                dataInputStream.close();
                fileOutputStream.close();
                imageNumber++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
