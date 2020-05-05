package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.dto.UserCommentDTO;
import com.cmft.slas.cmuop.service.UserCommentService;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.cmuop.vo.CommentVO;
import com.cmft.slas.common.pojo.Page;
import com.github.pagehelper.PageInfo;
import com.cmft.slas.common.pojo.WebResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
public class CommentControllerForCmuop {

    @Autowired
    private UserCommentService userCommentService;

    @Autowired
    private UserService userService;

    @PostMapping("/comment")
    public WebResponse addUserComment(@RequestBody UserCommentDTO userCommentDTO) {
        WebResponse<String> response = new WebResponse<String>();
        if(StringUtils.isAnyBlank(userCommentDTO.getArticleId(), userCommentDTO.getUid())) {
            return WebResponse.error("用户Id或文章Id为空");
        }
//        if(!userService.isValidUser(userCommentDTO.getUid())) {
//            return WebResponse.error("用户无效");
//        }
        Long result = userCommentService.addUserComment(userCommentDTO);
        return result == null ?
                WebResponse.error("操作失败") : WebResponse.success("操作成功", result.toString());
    }

    @DeleteMapping("/comment/{commentId}")
    public WebResponse deleteUserComment(@PathVariable Long commentId) {
        WebResponse<String> response = new WebResponse<String>();
        if(commentId == null) {
            return WebResponse.error("评论Id为空");
        }
        int result = userCommentService.deleteUserComment(commentId);
        return result != 1 ?
                WebResponse.error("操作失败") : WebResponse.success("操作成功");

    }

    @GetMapping("/comments/{articleId}")
    public WebResponse<PageInfo<CommentVO>> getUserComments(@PathVariable Long articleId,
                                                            @RequestParam String uid,
                                                            @ModelAttribute Page page) {
        WebResponse<PageInfo<CommentVO>> response = new WebResponse<>();
        if(articleId == null) {
            return WebResponse.error("文章Id为空", null);
        }
        PageInfo<CommentVO> pageInfo = userCommentService.getUserComments(articleId, uid, page);
        return response.setBody(pageInfo);
    }


}
