/**
 */
package com.cmft.slas.cmuop.service;

import java.util.List;

import com.cmft.slas.cmuop.dto.UserEntityDTO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;

public interface UserEntityService {

    /**
     * 新增记录
     *
     * @param userEntityDTO
     * @return
     */
    int addUserEntity(UserEntityDTO userEntityDTO);

    /**
     * 修改记录
     *
     * @param userEntityDTO
     * @return
     */
    int updateUserEntity(UserEntityDTO userEntityDTO);

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteUserEntity(Long id);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    UserEntityDTO queryById(Long id);

    /**
     * 按条件查询总数
     *
     * @param userEntityDTO
     * @return
     */
    long countByCondition(UserEntityDTO userEntityDTO);

    /**
     * 按条件查询
     *
     * @param userEntityDTO
     * @return
     */
    List<UserEntityDTO> queryByCondition(UserEntityDTO userEntityDTO);

    /**
     * 分页查询数据
     *
     * @param userEntityDTO
     * @param page
     * @return
     */
    PageInfo<UserEntityDTO> queryByPage(UserEntityDTO userEntityDTO, Page page);

    List<UserEntityDTO> getUserEntityList(UserEntityDTO userEntityDTO);

    String queryEntityCodeFromNuc(String accountId) throws Exception;
}
