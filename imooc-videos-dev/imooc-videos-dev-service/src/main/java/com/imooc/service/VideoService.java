package com.imooc.service;

import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.utils.PagedResult;

import java.util.List;

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

    /**
     * 分页查询视频列表
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllVideos(Videos video, Integer isSaveRecord,Integer page, Integer pageSize);

    /**
     * 获取热搜词列表
     * @return
     */
    List<String> getHotwords();

    /**
     * 保存评论
     * @param comments
     */
    void saveComment(Comments comments);
}
