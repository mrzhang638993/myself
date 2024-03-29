package com.asiainfo.selfstudy.Controller;

import com.asiainfo.selfstudy.interfaces.Add;
import com.asiainfo.selfstudy.interfaces.Update;
import com.asiainfo.selfstudy.vo.WorkInfoForm;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
@Api(value = "测试swagger以及sprngmvc验证功能的controller")
public class Test {


    @GetMapping("/test")
    @ResponseBody
    public   String  test(@Validated(Update.class)WorkInfoForm  workVo, BindingResult  bindingResult){
        if (bindingResult.hasErrors()){
            return "参数校验错误";
        }
        return   null;
    }
}
