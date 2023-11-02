package com.myblog.service;

import com.myblog.entity.Comment;
import com.myblog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDTO);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId , Long commentId);


    CommentDto updateComment(Long postId, long id, CommentDto commentDto);

    void deleteComment(long postId, long id);
}
