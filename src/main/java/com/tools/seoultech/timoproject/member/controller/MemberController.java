package com.tools.seoultech.timoproject.member.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberFacade memberFacade;

    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(memberRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<APIDataResponse<MemberInfoResponse>> getMember(@CurrentMemberId Long memberId) {

        MemberInfoResponse memberInfo = memberFacade.getMemberInfo(memberId);

        return ResponseEntity.ok(APIDataResponse.of(memberInfo));
    }

    @GetMapping("/player/verify")
    public ResponseEntity<APIDataResponse<?>> verifyPlayer(
            AccountDto.Request request
    ) {
        AccountDto.Response response = memberFacade.verifyPlayer(request);

        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @PostMapping("/nickname/random")
    public ResponseEntity<APIDataResponse<?>> getRandomNickname() {

        String randomNickname = memberFacade.createRandomNickname();

        return ResponseEntity.ok(APIDataResponse.of(randomNickname));
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<APIDataResponse<?>> checkNickname(@RequestParam String nickname) {
        if(memberFacade.checkNickname(nickname)) {
            return ResponseEntity.badRequest().body(APIDataResponse.of("사용 중인 닉네임입니다."));
        } else {
            return ResponseEntity.ok(APIDataResponse.of("사용 가능한 닉네임입니다."));
        }
    }

}
