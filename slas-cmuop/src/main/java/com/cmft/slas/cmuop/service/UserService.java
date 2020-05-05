/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface UserService {

    /**
     * 新增记录
     *
     * @param userDTO
     * @return
     */
    int addUser(UserDTO userDTO);

    /**
     * 修改记录
     *
     * @param userDTO
     * @return
     */
    int updateUser(UserDTO userDTO);

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteUser(Long id);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    UserDTO queryById(Long id);

    /**
     * 按条件查询总数
     *
     * @param userDTO
     * @return
     */
    long countByCondition(UserDTO userDTO);

    /**
     * 按条件查询
     *
     * @param userDTO
     * @return
     */
    List<UserDTO> queryByCondition(UserDTO userDTO);

    /**
     * 分页查询数据
     *
     * @param userDTO
     * @param page
     * @return
     */
    PageInfo<UserDTO> queryByPage(UserDTO userDTO, Page page);

    List<UserDTO> getUserList();

    List<UserDTO> queryUserEntityRelated();

    String getUserInfoFromNuc(String userName);

    boolean isValidUser(String uid);

    UserDTO getUserInfoByUid(String uid);

    List<String> queryUserUidList();

    List<UserDTO> queryUserEntityRelatedByIsValid();

    void synUserInfo();

    int updateUserByUid(UserDTO userDTO);

    String queryUserBusinessUnit(String accountId);

    void insetUserBusinessUnit(User user, String businessUnitString);
}
