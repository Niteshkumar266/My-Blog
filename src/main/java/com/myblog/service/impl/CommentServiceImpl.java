package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.BlogAPIException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository ;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDTO) {
 Comment comment  = mapToEntity(commentDTO);
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","Id",postId)
        );
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
         return mapToDTO (newComment) ;


    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);// wheather post exists or not
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());

              }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));
 //////////////////////////////////////|\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

            return mapToDTO(comment);
        }

    @Override
    public CommentDto updateComment(Long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow( // Post exist with this Id
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(// retrieve comment  by Id
                () -> new ResourceNotFoundException("Comment", "id", id)
        );


        // comment belong to this post
        if(!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to this post");
                    }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);

    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to this post");
        }
        commentRepository.deleteById(comment.getId());
        }

    private CommentDto mapToDTO(Comment newComment) {
        CommentDto dto = new CommentDto();
        dto.setId(newComment.getId());
        dto.setName(newComment.getName());
        dto.setEmail(newComment.getEmail());
        dto.setBody(newComment.getBody());
        return dto;

    }

    private Comment mapToEntity(CommentDto commentDTO) {
        Comment comment = new Comment();
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        return comment;

         }

         }
