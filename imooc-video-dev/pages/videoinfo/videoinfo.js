var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    cover: "cover",
    videoId: "",
    src: "",
    videoInfo: {},
  },
  showSearch: function () {
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  }
})