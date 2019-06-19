var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    cover: "cover",
    videoId: "",
    src: "",
    videoInfo: {},
    userLikeVideo: false,
    commentsPage: 1,
    commentsTotalPage: 1,
    commentsList: [],
    placeholder: "说点什么吧..."
  },
  videoCtx: {},
  onLoad: function (params) { 
    var me = this;
    me.videoCtx = wx.createVideoContext("myVideo", me);
    // 获取上一个页面传入的参数
    var videoInfo = JSON.parse(params.videoInfo);
    var height = videoInfo.videoHeight;
    var width = videoInfo.videoWidth;
    var cover = "cover";
    if (width >= height) {
      cover = "";
    }
    me.setData({
      videoId: videoInfo.id,
      src: app.serverUrl + videoInfo.videoPath,
      videoInfo: videoInfo,
      cover: cover
    });
    //TODO 用户不为空的时候查询出点赞人数等信息

  },
  showSearch: function () {//点击搜索按钮
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  },
  leaveComment: function () {//点击评论按钮
    this.setData({
      commentFocus: true
    });
  },
  saveComment: function(e) {//评论提交事件
    var me = this;
    var content = e.detail.value;
    var user = app.getGlobalUserInfo();
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;
    //没登录就跳转登录页面
    if (user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      //提交评论
      wx.showLoading({
        title: '请稍后...',
      })
      wx.request({
        url: app.serverUrl + '/video/saveComment?',
        method: 'POST',
        header: {
          'content-type': 'application/json', // 默认值
          'headerUserId': user.id,
          'headerUserToken': user.userToken
        },
        data: {
          fromUserId: user.id,
          videoId: me.data.videoInfo.id,
          comment: content
        },
        success: function (res) {
          console.log(res.data)
          wx.hideLoading();
          me.setData({
            contentValue: "",
            commentsList: []
          });

          // me.getCommentsList(1);
        }
      })
    }
  },
  showIndex: function () {//跳转首页
    wx.redirectTo({
      url: '../index/index',
    })
  },
  showMine: function () {//跳转个人主页
    var user = app.getGlobalUserInfo();

    if (user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/login',
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine',
      })
    }
  },
  
})
