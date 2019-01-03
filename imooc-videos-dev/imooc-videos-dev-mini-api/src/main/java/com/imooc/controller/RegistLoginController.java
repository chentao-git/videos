package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import java.util.UUID;

@RestController
@Api(value = "用户登录测试接口", tags = "注册和登录的controller")
public class RegistLoginController extends BasicController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "用户注册",notes = "用户注册接口")
	@PostMapping("/regist")
	public IMoocJSONResult regist(@RequestBody Users users) throws Exception{
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
		users.setPassword("");

		UsersVO usersVO = setUserRedisSessionToken(users);
		return IMoocJSONResult.ok(usersVO);
	}
	//userId加默认字符串生成key，唯一标识为value存入redis中
	public UsersVO setUserRedisSessionToken(Users userModel) {
		//生成唯一标识
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);

		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userModel, userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}

	@ApiOperation(value = "用户登录",notes = "用户登录接口")
	@PostMapping(value = "/login")
	public IMoocJSONResult login(@RequestBody Users user) throws Exception{
		String username = user.getUsername();
		String password = user.getPassword();
		//判断用户名密码不为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
		}
		//判断用户是否存在
		Users userResult = userService.queryUserForLogin(username,MD5Utils.getMD5Str(user.getPassword()));
		//返回
		if (userResult != null){
			userResult.setPassword("");
			UsersVO usersVO = setUserRedisSessionToken(userResult);
			return IMoocJSONResult.ok(usersVO);
		}else{
			return IMoocJSONResult.errorMsg("用户名或者密码不正确，请重试！");
		}
	}
	@ApiOperation(value="用户注销", notes="用户注销的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true,
			dataType="String", paramType="query")
	@PostMapping("/logout")
	public IMoocJSONResult logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return IMoocJSONResult.ok();
	}
}
