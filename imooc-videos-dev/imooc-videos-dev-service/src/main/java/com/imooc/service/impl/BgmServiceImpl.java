package com.imooc.service.impl;

import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.service.BgmService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmMapper bgmMapper;
    @Autowired
    private Sid sid;

    /**
     * 获取bgm列表
     * @return
     */
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    /**
     * 根据id获取bgm信息
     * @param bgmId
     * @return
     */
    @Override
    public Bgm queryByBgmId(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
