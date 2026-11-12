/**
 * emis-portal - portal门户平台
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
import { ProForm, ProFormProps, ProFormText } from '@ant-design/pro-components';
import { LockTwoTone } from '@ant-design/icons';
import { FormattedMessage, useIntl } from '@@/exports';
import useStyle from './style';
import Title from '@/components/Title';
import { Typography } from 'antd';

const prefixCls = 'emis-forget-password';
const { Paragraph } = Typography;

type PasswordProps = {
  mobile: string;
};
const Password = ({ mobile, ...rest }: PasswordProps & ProFormProps) => {
  const intl = useIntl();
  const { styles } = useStyle(prefixCls);
  return (
    <div className={styles.main}>
      <Title size={'h1'} title={intl.formatMessage({ id: 'pages.login.forget-password' })} />
      <Paragraph>{`${intl.formatMessage(
        { id: 'pages.login.forget-password.password.desc' },
        { mobile: mobile },
      )}`}</Paragraph>
      <ProForm {...rest}>
        <ProFormText.Password
          name="newPassword"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.new',
          })}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
          ]}
        />
        <ProFormText.Password
          name="passwordAgain"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.confirm',
          })}
          validateTrigger={'onBlur'}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
            {
              validator: (_, value, callback) => {
                const password = rest?.formRef?.current?.getFieldValue('newPassword');
                if (password && password !== value) {
                  callback(
                    intl.formatMessage({
                      id: 'pages.login.forget-password.inconsistency',
                    }),
                  );
                }
                callback();
              },
              message: <FormattedMessage id="pages.login.forget-password.inconsistency" />,
            },
          ]}
        />
      </ProForm>
    </div>
  );
};
export default Password;
