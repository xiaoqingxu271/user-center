package com.lc.usercenter.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chun0
 * @version 1.0
 * @since 2026/3/10 11:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModifyUserDTO extends UserInfoDTO {
    /**
     * 用户ID
     */
    private Long id;

}
