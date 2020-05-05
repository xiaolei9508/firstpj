 package com.cmft.slas.cmuop.controller;

 import com.cmft.slas.cmuop.dto.UserDTO;
 import com.cmft.slas.cmuop.dto.UserLikeCommentDTO;
 import com.cmft.slas.cmuop.dto.UserLikeDTO;
 import com.cmft.slas.cmuop.service.UserLikeCommentService;
 import com.cmft.slas.cmuop.service.UserLikeService;
 import com.cmft.slas.cmuop.service.UserService;
 import com.cmft.slas.cmuop.vo.UserVO;
 import com.cmft.slas.common.pojo.WebResponse;

 import io.swagger.annotations.Api;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;


@RestController
@Api("用户点赞接口")
@RequestMapping("/app")
public class UserLikeControllerForCmuop {

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private UserLikeCommentService userLikeCommentService;

    @Autowired
    private UserService userService;

    @PostMapping("/like")
    public WebResponse addUserLike(@RequestBody UserLikeDTO userLikeDTO) {
        WebResponse<String> response = new WebResponse<String>();
        if(StringUtils.isAnyBlank(userLikeDTO.getArticleId(), userLikeDTO.getUid())) {
            return WebResponse.error("用户Id或文章Id为空");
        }
//        if(!userService.isValidUser(userLikeDTO.getUid())) {
//            return WebResponse.error("用户无效");
//        }

        if (userLikeDTO.getType() == 1) {
            if (userLikeService.ifUserLiked(userLikeDTO)) {
                return WebResponse.success("已经点过赞了");
            }
        } else if(userLikeDTO.getType() == -1) {
            if (!userLikeService.ifUserLiked(userLikeDTO)) {
                return WebResponse.error("未点赞，取消点赞失败");
            }
        } else {
            return WebResponse.error("点赞类型错误");
        }
        int result = userLikeService.likeThisArticle(userLikeDTO);
        return result != 1 ?
                WebResponse.error("操作失败") : WebResponse.success("操作成功");
    }


    @GetMapping("/likes/{articleId}")
    public WebResponse<List<UserVO>> getUserLikes(@PathVariable String articleId) {
        WebResponse<List<UserVO>> res = new WebResponse<>();
        List<UserVO> userLikeVOS = userLikeService.getUserLikes(articleId);
        res.setBody(userLikeVOS);
        return res;
    }

    @PostMapping("/comment/like")
    public WebResponse addUserCommentLike(@RequestBody UserLikeCommentDTO userLikeCommentDTO) {
        WebResponse<String> response = new WebResponse<String>();
        if(StringUtils.isAnyBlank(userLikeCommentDTO.getArticleId(), userLikeCommentDTO.getUid()) || userLikeCommentDTO.getCommentId() == null) {
            return WebResponse.error("用户Id,或文章Id,或评论Id为空");
        }
//        if(!userService.isValidUser(userLikeCommentDTO.getUid())) {
//            return WebResponse.error("用户无效");
//        }
        if (userLikeCommentDTO.getType() == 1) {
            if (userLikeCommentService.ifUserLikeComment(userLikeCommentDTO)) {
                return WebResponse.success("已经点过赞了");
            }
        } else if(userLikeCommentDTO.getType() == -1) {
            if (!userLikeCommentService.ifUserLikeComment(userLikeCommentDTO)) {
                return WebResponse.error("未点赞，取消点赞失败");
            }
        } else {
            return WebResponse.error("点赞类型错误");
        }
        int result = userLikeCommentService.likeThisArticleComment(userLikeCommentDTO);
        return result != 1 ?
                WebResponse.error("操作失败") : WebResponse.success("操作成功");
    }
}
