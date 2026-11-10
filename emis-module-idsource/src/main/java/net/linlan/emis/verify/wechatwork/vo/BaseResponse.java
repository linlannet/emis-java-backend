/*
 * emis-module-idsource - idsource身份源
 * Copyright © 2020-2025 Linlan (open@linlan.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.linlan.emis.verify.wechatwork.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import net.linlan.emis.verify.wechatwork.WeChatWorkConstant;

/**
 * 企业微信返回
 *
 * @author Linlan
 * 
 */
@Data
public class BaseResponse implements Serializable {
    /**
     * 出错返回码，为0表示成功，非0表示调用失败
     */
    @JsonProperty("errcode")
    private Integer errCode;
    /**
     * 返回码提示语
     */
    @JsonProperty("errmsg")
    private String  errMsg;

    public boolean isSuccess() {
        return WeChatWorkConstant.REQUEST_SUCCESS.equals(errCode);
    }
}
