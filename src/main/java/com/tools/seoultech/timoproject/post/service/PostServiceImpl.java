package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.post.domain.Post;
import com.tools.seoultech.timoproject.post.dto.PageDTO;
import com.tools.seoultech.timoproject.post.dto.PostDTO;
import com.tools.seoultech.timoproject.post.repository.PostRepository;

import com.tools.seoultech.timoproject.post.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserAccountRepository userAccountRepository;

    public PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    public PostDTO read(Long id){
        Optional<Post> post = postRepository.findById(id);
        return post.isPresent() ? entityToDto(post.get()) : null;
    }
    @Transactional
    public APIErrorResponse create(PostDTO postDto){
        Post post = this.dtoToEntity(postDto, userAccountRepository);
        postRepository.save(post);
        return APIErrorResponse.of(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage()+": create 성공.");
    }
    @Transactional
    public APIErrorResponse update(PostDTO postDto){
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());
        APIErrorResponse response;
        // 엄격한 post. > ID 조회 시 없는 경우, 해당 id로 create 하지 않고 4XX 에러.
        if(optionalPost.isPresent()){
            postRepository.save(dtoToEntity(postDto, userAccountRepository));
            response = APIErrorResponse.of(true, ErrorCode.OK);
        }
        else{
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return response;
    }
    @Transactional
    public APIErrorResponse delete(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        APIErrorResponse response;
        if(optionalPost.isPresent()){
            postRepository.deleteById(id);
            response =APIErrorResponse.of(true, ErrorCode.OK);
        }
        else {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return response;
    }
}
