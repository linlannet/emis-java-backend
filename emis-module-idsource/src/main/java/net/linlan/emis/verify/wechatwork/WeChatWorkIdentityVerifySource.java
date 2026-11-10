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
package net.linlan.emis.verify.wechatwork;

import java.io.Serializable;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson2.JSON;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.idsource.constant.IdentitySourceProvider;
import net.linlan.emis.manage.AbstractDefaultIdentityVerifySource;
import net.linlan.emis.manage.client.IdentitySourceClient;
import net.linlan.emis.manage.constant.IdentitySourceEventReceiveType;
import net.linlan.emis.manage.processor.IdentitySourceEventPostProcessor;
import net.linlan.emis.manage.processor.IdentitySourceSyncDeptPostProcessor;
import net.linlan.emis.manage.processor.IdentitySourceSyncUserPostProcessor;
import net.linlan.emis.manage.processor.modal.IdentitySourceEventProcessData;
import net.linlan.emis.support.exception.BadParamsException;
import net.linlan.emis.verify.wechatwork.enums.WeChatWorkEventType;
import net.linlan.emis.verify.wechatwork.util.AesException;
import net.linlan.emis.verify.wechatwork.util.WxBizMsgCrypt;
import net.linlan.utils.http.HttpRequestUtils;

/**
 * 企业微信
 *
 * @author Linlan
 *
 */
@Slf4j
@SuppressWarnings("AlibabaSwitchStatement")
public class WeChatWorkIdentityVerifySource extends
                                            AbstractDefaultIdentityVerifySource<WeChatWorkConfig> {
    public static final String MSG_SIGNATURE = "msg_signature";
    public static final String TIMESTAMP     = "timestamp";
    public static final String NONCE         = "nonce";
    public static final String ECHOSTR       = "echostr";

    /**
     * 企业微信事件回调事件
     *
     * @param request  {@link  HttpServletRequest}
     * @param body {@link  String}
     * @return {@link  ResponseEntity}
     */
    @Override
    public Object event(HttpServletRequest request, String body) {
        LocalDateTime eventTime = LocalDateTime.now();
        Map<String, String> params = HttpRequestUtils.getParams(request);
        WeChatWorkRequest weWorkResult = null;
        try {
            if (RequestMethod.POST.name().equals(request.getMethod())) {
                try {
                    weWorkResult = HttpRequestUtils.getXml(request, WeChatWorkRequest.class);
                } catch (JAXBException e) {
                    log.error("企业微信身份源 [{}] 事件回调入参转换异常: {}", getId(), e.getMessage());
                    throw new BadParamsException("企业微信事件回调数据转换异常: " + e.getMessage());
                }
            }

            log.debug("企业微信身份源 [{}] 事件回调入参: {}, {}", getId(), params, weWorkResult);
            String result = eventCallBack(eventTime, params, weWorkResult);
            log.debug("企业微信身份源 [{}] 事件回调返回: {}", getId(), JSON.toJSONString(result));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("企业微信回调通知异常: 身份源id: [{}], 入参: [{}, {}], 异常信息: [{}]", getId(), params,
                weWorkResult, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 企业微信事件事件回调
     *
     * @param params {@link Map}
     * @return 返回第三方平台结果
     */
    private String eventCallBack(LocalDateTime eventTime, Map<String, String> params,
                                 WeChatWorkRequest weWorkResult) throws IllegalArgumentException {
        try {
            String msgSignature = params.get(MSG_SIGNATURE);
            String timeStamp = params.get(TIMESTAMP);
            String nonce = params.get(NONCE);
            if (params.containsKey(ECHOSTR)) {
                String echoStr = params.get(ECHOSTR);
                return verifyUrl(msgSignature, timeStamp, nonce, echoStr);
            } else {
                EventParameter callBackDTO = processMessage(msgSignature, timeStamp, nonce,
                    weWorkResult);
                WeChatWorkEventType changeType = WeChatWorkEventType
                    .getType(callBackDTO.getChangeType());
                IdentitySourceEventReceiveType eventType = null;
                List<? extends Serializable> param = null;
                if (changeType.equals(WeChatWorkEventType.USER_ADD_ORG)) {
                    //用户添加
                    eventType = IdentitySourceEventReceiveType.USER_ADD;
                    param = getUser(callBackDTO.getUserId());
                } else if (changeType.equals(WeChatWorkEventType.USER_MODIFY_ORG)) {
                    //用户修改
                    eventType = IdentitySourceEventReceiveType.USER_MODIFY;
                    param = getUser(callBackDTO.getUserId());
                } else if (changeType.equals(WeChatWorkEventType.USER_LEAVE_ORG)) {
                    //用户离职
                } else if (changeType.equals(WeChatWorkEventType.USER_MODIFY_ORG)) {
                    eventType = IdentitySourceEventReceiveType.USER_LEAVE;
                    param = getIdList(callBackDTO.getUserId());
                } else if (changeType.equals(WeChatWorkEventType.ORG_DEPT_CREATE)) {
                    //组织创建
                    eventType = IdentitySourceEventReceiveType.DEPT_CREATE;
                    param = getDept(callBackDTO.getId());
                } else if (changeType.equals(WeChatWorkEventType.ORG_DEPT_MODIFY)) {
                    //组织修改
                    eventType = IdentitySourceEventReceiveType.DEPT_MODIFY;
                    param = getDept(callBackDTO.getId());
                } else if (changeType.equals(WeChatWorkEventType.ORG_DEPT_REMOVE)) {
                    //组织删除
                    eventType = IdentitySourceEventReceiveType.DEPT_REMOVE;
                    param = getIdList(callBackDTO.getId());
                } else {
                    throw new IllegalArgumentException("企业微信身份提供商事件回调非法事件");
                }
                IdentitySourceEventProcessData<?> data = new IdentitySourceEventProcessData<>(
                    getId(), param, IdentitySourceProvider.WECHAT_WORK, eventTime, eventType);
                identitySourceEventPostProcessor.process(data);
            }
        } catch (Exception e) {
            log.error("企业微信身份源 [{}] 事件回调发生异常: {}", getId(), e.getMessage());
        }
        return null;
    }

    private String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) {
        try {
            WxBizMsgCrypt wxCpt = getWxBizJsonMsgCrypt();
            // 需要返回的明文
            String sEchoStr = wxCpt.verifyUrl(msgSignature, timeStamp, nonce, echoStr);
            log.debug("验证URL成功, 返回: {}", sEchoStr);
            // 验证URL成功，将sEchoStr返回
            return sEchoStr;
        } catch (Exception e) {
            // 验证URL失败，错误原因请查看异常
            log.error("验证URL失败, 错误原因: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 企业收到post请求之后应该：
     *
     *  1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
     *  2.验证消息体签名的正确性。
     *  3.将post请求的数据进行json解析，并将"encrypt"标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档 第2，3步可以用企业微信提供的库函数DecryptMsg来实现。
     */
    private EventParameter processMessage(String msgSignature, String timeStamp, String nonce,
                                          WeChatWorkRequest weWorkResult) {
        try {
            WxBizMsgCrypt wxCpt = getWxBizJsonMsgCrypt();
            String xmlMessage = wxCpt.decryptMsg(msgSignature, timeStamp, nonce,
                weWorkResult.getEncrypt());
            // 解析出明文xml标签的内容进行处理
            JAXBContext jc = JAXBContext.newInstance(EventParameter.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (EventParameter) unmarshaller.unmarshal(new StringReader(xmlMessage));
        } catch (AesException | JAXBException e) {
            // 解密失败，失败原因请查看异常
            throw new BadParamsException(e.getMessage());
        }
    }

    /**
     * 获取解密对象
     */
    private WxBizMsgCrypt getWxBizJsonMsgCrypt() throws AesException {
        WeChatWorkConfig config = getConfig();
        String token = config.getToken();
        String aesKey = config.getEncodingAESKey();
        String corpId = config.getCorpId();
        return new WxBizMsgCrypt(token, aesKey, corpId);
    }

    /**
     * 事件回调DTO
     */
    @Data
    @XmlRootElement(name = "xml")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EventParameter {
        @XmlElement(name = "ToUserName")
        private String toUserName;
        @XmlElement(name = "FromUserName")
        private String fromUserName;
        @XmlElement(name = "CreateTime")
        private String createTime;
        @XmlElement(name = "MsgType")
        private String msgType;
        @XmlElement(name = "Event")
        private String event;
        @XmlElement(name = "ChangeType")
        private String changeType;
        /**
         * 部门ID
         */
        @XmlElement(name = "Id")
        private String id;
        /**
         * 用户ID
         */
        @XmlElement(name = "UserID")
        private String userId;
    }

    /**
     * 企业微信事件回调入参
     *
     */
    @Data
    @XmlRootElement(name = "xml")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class WeChatWorkRequest implements Serializable {
        @XmlElement(name = "ToUserName")
        private String toUserName;
        @XmlElement(name = "AgentID")
        private String agentId;
        @XmlElement(name = "Encrypt")
        private String encrypt;
    }

    public WeChatWorkIdentityVerifySource(String id, String name, WeChatWorkConfig config,
                                          IdentitySourceClient identitySourceClient,
                                          IdentitySourceSyncUserPostProcessor identitySourceSyncUserPostProcessor,
                                          IdentitySourceSyncDeptPostProcessor identitySourceSyncDeptPostProcessor,
                                          IdentitySourceEventPostProcessor identitySourceEventPostProcessor) {
        super(id, name, config, identitySourceClient, identitySourceSyncUserPostProcessor,
            identitySourceSyncDeptPostProcessor, identitySourceEventPostProcessor);
    }

}
