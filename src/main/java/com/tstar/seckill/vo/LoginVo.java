package com.tstar.seckill.vo;

import com.tstar.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/29 10:42 上午
 */
@Data
public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;
    @NotNull

    @Length(min = 32)
    private String password;

}
