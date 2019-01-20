package com.imooc.service;

import com.imooc.pojo.Bgm;

import java.util.List;

public interface BgmService {
    //获取背景音乐列表
    List<Bgm> queryBgmList();

    //根据id查询bgm的信息
    Bgm queryByBgmId(String bgmId);

}
