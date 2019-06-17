package com.asiainfo.selfstudy.vo;

import com.asiainfo.selfstudy.interfaces.Add;
import com.asiainfo.selfstudy.interfaces.Update;
import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Data
public class WorkInfoForm {



    @NotNull(groups = Update.class,message = "id不能为null")
    @Null(groups = Add.class)
    Long  id;

    //@Size(min = 3,max = 6)
    String name;

    //@Email
    String email;
}
