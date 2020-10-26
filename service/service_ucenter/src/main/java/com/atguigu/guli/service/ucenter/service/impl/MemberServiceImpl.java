package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.JwtInfo;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.common.base.util.MD5;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-09-20
 */
@Service
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 会员注册
     * @param registerVo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo registerVo) {
        // 获取用户输入的值
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        // 校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname)) {
            throw new GuLiExcpetion(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验验证码
        String  checkCode = (String) redisTemplate.opsForValue().get(mobile);
        if (!code.equals(checkCode)){
            throw new GuLiExcpetion(ResultCodeEnum.CODE_ERROR);
        }

        // 是否被注册
        QueryWrapper<Member > queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new GuLiExcpetion(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://guli-file-helen.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);


    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 校验参数
        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)) {
            throw new GuLiExcpetion(ResultCodeEnum.PARAM_ERROR);
        }

        // 校验手机号
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if (member == null){
            throw new GuLiExcpetion(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        // 校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuLiExcpetion(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 检查用户是否被吊销
        if (member.getDisabled()){
            throw new GuLiExcpetion(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 创建token
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);
        return jwtToken;
    }

    @Override
    public Member getByOpenid(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member,memberDto);
        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {
        return baseMapper.selectRegisterNumByDay(day);
    }
}
