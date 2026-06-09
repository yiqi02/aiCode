package com.yy.aicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.yy.aicode.VO.LoginUserVO;
import com.yy.aicode.VO.UserVO;
import com.yy.aicode.dto.UserQueryRequest;
import com.yy.aicode.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/yiqi02">yy</a>
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 返回新生成的新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request   浏览器更新session使用
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户 微服务需要调用 高频率使用就不用脱敏后的
     * 登陆后的用户信息已经存入session 只需要从session中拿取数据
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销
     *
     * @param request
     * @return 退出登录是否成功
     */
    boolean userLogout(HttpServletRequest request);



    /**
     * 获取脱敏后的用户信息
     *
     * @param user 用户信息
     * @return
     */
    UserVO getUserVO(User user);


    /**
     * 获取脱敏后的用户信息（分页）
     *
     * @param userList 用户列表
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 根据查询条件构造数据查询参数
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);


    //密码加密算法
    String getEncryptPassword(String userPassword);
}
