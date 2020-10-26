package com.atguigu.guli.service.edu.controller;

import com.atguigu.guli.common.base.result.R;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
@RequestMapping("/user")
public class LoginController {

    @PostMapping("login")
    public R login(){
      return R.ok().data("token","admin");
    }
   //返回用户信息
    @GetMapping("info")
    public R info(){
        return R.ok().data("name","admin")
                .data("roles","[admin]")
                .data("avater","https://mvnrepository.com/img/70fbaa82a9c47b218af1e3fc5cf5c809");
    }

    //登出
    @PostMapping("logout")
    public R louOut(){
        return R.ok();
    }

}
