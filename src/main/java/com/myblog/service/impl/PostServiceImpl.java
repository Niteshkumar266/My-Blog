package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper ;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper ;
    }

    @Override
    public PostDto createPost(PostDto postDto) {// Its a DTO
        Post post = mapToEntity(postDto);// Convert DTO to Entity
        Post newPost = postRepository.save(post);// Saved in Entity form
        PostDto dto = mapToDto(newPost);// Convert entity back to Dto
        return dto;

    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize , String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();// Turnary  Operator

//         Sort sort = null ;
//                if(sortDir.equalsIgnoreCase("asc")){
//           sort =  sort.by(sortBy).ascending();
//        }else {
//           sort =  sort.by(sortBy).descending();
//        }

        PageRequest pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts = content.getContent();
        List<PostDto> dto = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(dto);
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setLast(content.isLast());
        postResponse.setTotalElements((int)content.getTotalElements());
         return postResponse ;
    //return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id)
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updtedpost = postRepository.save(post);
        return mapToDto(updtedpost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id",id)
        );
        postRepository.deleteById(id);
              }

    PostDto mapToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
//        return dto;
       return postDto;
                    }

    Post mapToEntity(PostDto dto) {//
        Post post = mapper.map(dto, Post.class);
//        Post post = new Post();
//        post.setTitle(dto.getTitle());
//        post.setDescription(dto.getDescription());
//        post.setContent(dto.getContent());
//        return post;
          return post ;
    }

   }
