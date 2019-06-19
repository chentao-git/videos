const app = getApp()

Page({
  data: {
    //用于分页的属性
    totalPage: 1,
    page: 1,
    videoList:[],
    //存放wx api获取的屏幕宽度，作用于图片宽度上
    screenWidth: 350,
    serverUrl: "",
    searchContent: ""
  },

  onLoad: function (params) {
    var me = this;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    me.setData({
      screenWidth: screenWidth,
    });
    //搜索内容
    var searchContent = params.search;
    var isSaveRecord = params.isSaveRecord;
    //0 热搜词不需要保存 ，或者为空的时候 1 - 需要保存
    if (isSaveRecord == null || isSaveRecord == '' || isSaveRecord == undefined) {
      isSaveRecord = 0;
    }

    me.setData({
      searchContent: searchContent,
    });
    //获取当前分页数
    var page = me.data.page;
    me.getAllVideoList(page, isSaveRecord);

  },
  getAllVideoList: function (page, isSaveRecord){
    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中...',
    })
    //搜索内容
    var searchContent = me.data.searchContent;

    //发起请求
    wx.request({
      url: serverUrl + '/video/showAll?page=' + page + "&isSaveRecord=" + isSaveRecord,
      method: "POST",
      data: {
        videoDesc: searchContent
      },
      success: function (res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        //停止当前页面的当前刷新
        wx.stopPullDownRefresh();
        
        console.log(res);
        //判断当前页的page是否是第一页，如果是第一页，那么设置videoList为空
        if (page == 1) {
          me.setData({
            videoList: []
          });
        }
        var videoList = res.data.data.rows;
        var newVideoList = me.data.videoList;
        me.setData({
          videoList: newVideoList.concat(videoList),
          page: page,
          totalPage: res.data.data.total,
          serverUrl: serverUrl
        });
      }
    })
  },
  //上拉刷新至第一页
  onPullDownRefresh: function(){
    //在当前导航栏的j加载动画
    wx.showNavigationBarLoading();
    this.getAllVideoList(1,0);
  },
  //下拉刷新
  onReachBottom:function(){
    var me = this;
    var currentPage = me.data.page;
    var totalPage = me.data.totalPage;
    //判断当前页数和总页数是否相等，如果相等则无需查询
    if (currentPage == totalPage){
      wx.showToast({
        title: '已经没有视频啦~~',
        icon: "none"
      })
      return;
    }
    var page = currentPage +1;
    me.getAllVideoList(page, 0);

  },
  showVideoInfo: function (e) {//视频详情页
    var me = this;
    var videoList = me.data.videoList;
    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoList[arrindex]);

    wx.redirectTo({
      url: '../videoinfo/videoinfo?videoInfo=' + videoInfo
    })
  }
})
