package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

@RestController
@Api(value = "用户登录测试接口", tags = "注册和登录的controller")
public class RegistLoginController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "用户注册",notes = "用户注册接口")
	@PostMapping("/regist")
	public IMoocJSONResult Hello(@RequestBody Users users) throws Exception{
		// 判断用户名 密码不为空
		if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
			return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
		}
		// 判断用户是否存在
		boolean usernameIsExist = userService.queryUsernameIsExist(users.getUsername());
		if (!usernameIsExist){
			users.setNickname(users.getUsername());
			users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
			users.setFansCounts(0); //粉丝数
			users.setReceiveLikeCounts(0); //我接受到的赞美/收藏 的数量
			users.setFollowCounts(0); //我关注的人总数
			userService.saveUser(users);

		}else{
			return IMoocJSONResult.errorMsg("用户名已经存在！");
		}
		// 保存用户 注册信息

		return null;
	}
	
}
