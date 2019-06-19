package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.CommentsMapper;
import com.imooc.mapper.SearchRecordsMapper;
import com.imooc.mapper.VideosMapper;
import com.imooc.mapper.VideosMapperCustom;
import com.imooc.pojo.Comments;
import com.imooc.pojo.SearchRecords;
import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.VideosVO;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    //视频dao
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;
    //注入 用户视频 dao
    @Autowired
    private VideosMapperCustom videosMapperCustom;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper; //热搜词mapper
    @Autowired
    private CommentsMapper commentsMapper;//评论 mapper

    /**
     * 视频保存
     * @param video
     * @return
     */
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    /**
     * 视频更新（封面更新）
     * @param videoId
     * @param coverPath
     */
    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

        //保存热搜词
        String desc = video.getVideoDesc();//搜索词
        if (null != isSaveRecord && isSaveRecord ==1){
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);//搜索的内容
            searchRecordsMapper.insert(record);
        }

        //分页插件
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(desc);

        PageInfo<VideosVO> pageList = new PageInfo<>(list);
        //获取结果集
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    /**
     * 获取热搜词列表
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotwords(){
        return searchRecordsMapper.getHotwords();
    }

    /**
     * 保存评论
     * @param comment
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comment) {
        String id = sid.nextShort();
        comment.setId(id);
        comment.setCreateTime(new Date());
        commentsMapper.insert(comment);
    }
}
