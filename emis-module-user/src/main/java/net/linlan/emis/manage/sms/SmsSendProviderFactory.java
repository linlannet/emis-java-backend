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
package net.linlan.emis.manage.sms;

import net.linlan.emis.manage.sms.aliyun.AliyunSmsProviderConfig;
import net.linlan.emis.manage.sms.aliyun.AliyunSmsProviderSend;
import net.linlan.emis.manage.sms.qiniu.QiNiuSmsProviderConfig;
import net.linlan.emis.manage.sms.qiniu.QiNiuSmsProviderSend;
import net.linlan.emis.manage.sms.tencent.TencentSmsProviderConfig;
import net.linlan.emis.manage.sms.tencent.TencentSmsProviderSend;
import net.linlan.emis.uext.constant.SmsProvider;

/**
 *
 * @author Linlan
 */
public class SmsSendProviderFactory {

    private SmsSendProviderFactory() {
    }

    /**
     * 获取实例化
     *
     * @param config {@link SmsProviderConfig}
     * @return {@link SmsProviderSend}
     */
    public static SmsProviderSend newInstance(SmsProviderConfig config) {
        SmsProvider provider = config.getProvider();
        //阿里云
        if (SmsProvider.ALIYUN.equals(provider)) {
            return new AliyunSmsProviderSend((AliyunSmsProviderConfig) config);
        }
        //腾讯
        if (SmsProvider.TENCENT.equals(provider)) {
            return new TencentSmsProviderSend((TencentSmsProviderConfig) config);
        }
        //七牛
        if (SmsProvider.QINIU.equals(provider)) {
            return new QiNiuSmsProviderSend((QiNiuSmsProviderConfig) config);
        }
        throw new IllegalArgumentException("暂未支持该短信提供商类型");
    }
}
