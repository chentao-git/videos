package com.imooc.service;

import com.imooc.pojo.Videos;

public interface VideoService {
    /**
     * 保存视频
     * @param video
     * @return
     */
    String saveVideo(Videos video);

    /**
     * 修改视频封面
     * @param videoId
     * @param coverPath
     */
    void updateVideo(String videoId,String coverPath);
}
