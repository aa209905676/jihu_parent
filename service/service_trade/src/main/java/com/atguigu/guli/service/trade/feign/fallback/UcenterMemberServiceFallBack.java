package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.info("熔断保护");
        return null;
    }
}