package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value = "用户业务相关接口", tags = "用户业务相关的controller")
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "用户上传头像",notes = "用户上传头像接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true,
			dataType="String", paramType="query")
	@PostMapping("/uploadFace")
	public IMoocJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) throws IOException {
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空...");
		}
		//文件保存的命名空间
		String fileSpace = "F:/videos/business";
		//保存到数据的相对路径
		String uploadPathDB = "/"+ userId +"/face";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try{
			if (null != files  && files.length > 0){
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)){
					//文件保存最终路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					uploadPathDB += ("/" + fileName);
					File outFile = new File(finalFacePath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream,fileOutputStream);
				}
			}else{
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传异常...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		//更新用户头像
		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);
		//返回数据库相对路径
		return IMoocJSONResult.ok(uploadPathDB);
	}

	@ApiOperation(value = "用户基础信息查询",notes = "用户基础信息查询接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true,
			dataType="String", paramType="query")
	@PostMapping("/query")
	public IMoocJSONResult query(String userId) throws IOException {
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空...");
		}

		Users userInfo = userService.queryUserInfo(userId);
		UsersVO usersVO = new UsersVO();
		//UsersVO中password使用 @JsonIgnore 就不会返回出去了
		BeanUtils.copyProperties(userInfo,usersVO);

		return IMoocJSONResult.ok(usersVO);
	}



}
