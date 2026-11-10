/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.manage.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.core.exception.uext.MailMessageSendException;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.MessageSettingConstants;
import net.linlan.emis.uext.entity.MailSendRecord;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.emis.uext.entity.TemplateInfo;
import net.linlan.emis.uext.service.MailSendRecordService;
import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.springframework.web.util.HtmlUtils.htmlUnescape;

/**
 * 消息通知事件
 *
 * @author Linlan
 */
@Async
@Slf4j
@Component
public class MailMsgEventListener implements ApplicationListener<MailMsgEvent> {
    /**
     * redis
     */
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private Configuration                 freeMarkerConfiguration;
    /**
     * MailSend
     */
    @Resource
    private MailProviderSend              mailProviderSend;

    /**
     * MailSendRecordRepository
     */
    @Resource
    private MailSendRecordService         mailSendRecordRepository;

    @Override
    public void onApplicationEvent(@NonNull MailMsgEvent event) {
        // 邮件通知类型
        MailType type = event.getType();
        String content = htmlUnescape(MailUtils.readEmailContent(type.getContent()));
        String subject = type.getSubject();
        String sender = type.getSender();
        try {
            Map<String, Object> parameter = event.getParameter();
            // 判断是否存在自定义
            TemplateInfo templateEntity = (TemplateInfo) redisTemplate.opsForValue()
                .get(MessageSettingConstants.SETTING_EMAIL_TEMPLATE_CACHE_NAME + StringUtils.COLON
                     + type.getKey());
            if (!Objects.isNull(templateEntity)) {
                content = htmlUnescape(templateEntity.getContent());
                subject = templateEntity.getName();
                sender = templateEntity.getSignature();
            }
            // 主题
            StringWriter themeStringWriter = new StringWriter();
            Template themeTpl = createTemplate(type.getKey() + "-theme", subject);
            themeTpl.process(parameter, themeStringWriter);
            // 测试邮件
            if (parameter.containsKey(MsgVariable.TEST)) {
                String test = parameter.get(MsgVariable.TEST).toString();
                themeStringWriter.append(test);
            }
            // 内容
            StringWriter contentStringWriter = new StringWriter();
            Template contentTpl = createTemplate(type.getKey() + "-content", content);
            contentTpl.process(parameter, contentStringWriter);
            // 发送邮件
            //@formatter:off
            SendMailRequest params = new SendMailRequest()
                    //  发送人
                    .setSender(sender)
                    // 接收人
                    .setReceiver(event.getReceiver())
                    // 主题
                    .setSubject(themeStringWriter.toString())
                    // 内容
                    .setBody(contentStringWriter.toString());
            //@formatter:on
            // 发送邮件
            mailProviderSend.sendMailHtml(params);
            // 保存发送记录
            MailSendRecord record = new MailSendRecord();
            //@formatter:off
            record.setSenderName(event.getReceiver());
            record.setTitle(params.getSubject());
            record.setContent(params.getBody());
            record.setProvider(mailProviderSend.getProvider().getKey());
            record.setBizType(event.getType().getKey());
            record.setReceiverEmail(event.getReceiver());
            record.setSendTime(new Date());
            record.setSendStatus("1");
            record.setStatus("true");
            //@formatter:on
            mailSendRecordRepository.save(record);
        } catch (Exception e) {
            log.error("邮件信息发送失败: {}", e.getMessage());
            throw new MailMessageSendException("邮件信息发送失败！", e);
        }
    }

    /**
     * 创建模板
     *
     * @param name            {@link String} 模板名称
     * @param templateContent {@link String} 模板内容
     * @return {@link Template}
     * @throws IOException IOException
     */
    private Template createTemplate(String name, String templateContent) throws IOException {
        try {
            Template template = freeMarkerConfiguration.getTemplate(name, UTF_8);
            if (template != null) {
                if (template.toString().trim().equals(templateContent.trim())) {
                    return template;
                }
            }
        } catch (Exception ignored) {
        }
        // 以下操作不是线程安全，要加上同步
        synchronized (this) {
            // 获取模板加载器
            TemplateLoader templateLoader = freeMarkerConfiguration.getTemplateLoader();
            if (templateLoader instanceof StringTemplateLoader) {
                // 如果加载器已经是字符串加载器，则在原来的加载器上put一个新的模板
                ((StringTemplateLoader) templateLoader).putTemplate(name, templateContent);
                freeMarkerConfiguration.setTemplateLoader(templateLoader);
            } else {
                // 如果原来的模板加载器不是字符串的（默认是文件加载器），则新建
                StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
                stringTemplateLoader.putTemplate(name, templateContent);
                freeMarkerConfiguration.setTemplateLoader(stringTemplateLoader);
            }
            // 这里要清一下缓存，不然下面可能会获取不到模板
            freeMarkerConfiguration.clearTemplateCache();

            return freeMarkerConfiguration.getTemplate(name, UTF_8);
        }
    }

}
