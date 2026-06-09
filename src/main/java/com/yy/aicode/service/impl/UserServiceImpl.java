package com.yy.aicode.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yy.aicode.VO.LoginUserVO;
import com.yy.aicode.VO.UserVO;
import com.yy.aicode.dto.UserQueryRequest;
import com.yy.aicode.exception.BusinessException;
import com.yy.aicode.exception.ErrorCode;
import com.yy.aicode.model.entity.User;
import com.yy.aicode.mapper.UserMapper;
import com.yy.aicode.model.entity.enums.UserRoleEnum;
import com.yy.aicode.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yy.aicode.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://github.com/yiqi02">yy</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        //1、校验参数  参数为空、账号长度过短、密码长度过短、两次输入密码不一致
        if(StrUtil.hasBlank(userAccount,checkPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        if(userPassword.length()<8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        if(!checkPassword.equals(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
        }
        //2、查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        //自定义一个查询对象 判断用户是否存在 因此查询对象为用户账户
        queryWrapper.eq("userAccount",userAccount);
        //使用service里面自带的mapper方法 匹配用户账户判断是否存在 所需参数为wrapper
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户存在");
        }
        //3、密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        //4、创建用户加入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        //设置默认用户名
        user.setUserName("无名");
        //获取用户角色
        user.setUserRole(UserRoleEnum.USER.getValue());
        //使用save方法保存
        boolean ResultUser = this.save(user);
        if(!ResultUser){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据库错误");
        }
        return user.getId();
    }

    //将登录的用户信息传入给脱敏后的user对象
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if(user == null){
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        //使用springBeanUtils的方法将对象复制给脱敏后的对象 方便后续写登录方法
        BeanUtils.copyProperties(user,loginUserVO);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验参数
        if(StrUtil.hasBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        if(userPassword.length()<8 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //2、加密
        String encryptPassword = getEncryptPassword(userPassword);
        //3、查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或者密码错误");
        }
        //4、如果用户存在，记录登录状态
        //定义一个常量 后面还要读取 保证key的一致
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        //获取脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //从当前session中拿取数据 判断用户是否登录（user为空或者id为空） 拿取数据后转换成user类型对象 在数据库中根据id查询 用户对象
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null || currentUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        //先判断用户是否登录 如果登录移除状态码
        //从当前session中拿取数据 判断用户是否登录（user为空或者id为空） 拿取数据后转换成user类型对象 在数据库中根据id查询 用户对象
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null ){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if(user == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userlist) {
        //利用hutool工具类的集合判空类判断集合列表是否为空
        if(CollUtil.isEmpty(userlist)){
            return new ArrayList<>();
        }
        //利用list的stream对象中的map映射
        return userlist.stream().map(this::getUserVO).collect(Collectors.toList());
        }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if(userQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Long userId = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        //数据库查询
        return QueryWrapper.create()
                .eq("id", userId) // where id = ${id}
                .eq("userRole", userRole) // and userRole = ${userRole}
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    //密码加密算法
    @Override
    public String getEncryptPassword(String userPassword){
        //盐值加密算
        //自定义一个盐值
        final String SALT = "yy";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }
}
