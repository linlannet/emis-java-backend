/**
 * emis-console - enterprise multiple identity system
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
import {
  ModalForm,
  ProFormDependency,
  ProFormInstance,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Spin } from 'antd';
import * as React from 'react';
import { useRef, useState } from 'react';
import { useIntl } from '@@/exports';
import { mobileIsValidNumber } from '@/utils/utils';
import { omit } from 'lodash';
import { administratorParamCheck } from '../../service';
import { ParamCheckType } from '@/constant';
import FormMobileAreaCodeSelect from '@/components/FormMobileAreaCodeSelect';
import { createStyles } from 'antd-style';

const layout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 20 },
};

const useStyle = createStyles(({ prefixCls }) => {
  return {
    main: {
      [`.${prefixCls}-form-item-control-input`]: {
        width: '100%',
      },
    },
  };
});

export default (props: {
  onFinish: (formData: Record<string, string>) => Promise<boolean | void>;
  visible: boolean;
  onCancel: (e: React.MouseEvent | React.KeyboardEvent) => void;
}) => {
  const intl = useIntl();
  const { onFinish, visible, onCancel } = props;
  const formRef = useRef<ProFormInstance>();
  const [submitLoading, setSubmitLoading] = useState<boolean>(false);

  const { styles } = useStyle();

  const validatePhoneOrEmail = () => {
    if (formRef.current?.getFieldValue('mobile') || formRef.current?.getFieldValue('email')) {
      return Promise.resolve();
    }
    return Promise.reject(
      new Error(
        intl.formatMessage({
          id: 'pages.setting.administrator.modal.from.mobile_email.required.message',
        }),
      ),
    );
  };

  return (
    <ModalForm
      preserve={false}
      {...layout}
      open={visible}
      layout={'horizontal'}
      labelAlign={'right'}
      className={styles.main}
      title={intl.formatMessage({ id: 'pages.setting.administrator.add_administrator' })}
      width={'500px'}
      formRef={formRef}
      modalProps={{
        maskClosable: true,
        destroyOnClose: true,
        onCancel: (e) => {
          setSubmitLoading(false);
          onCancel(e);
        },
      }}
      onFinish={async (values: Record<string, any>) => {
        setSubmitLoading(true);
        try {
          let params = omit(values, 'mobileAreaCode');
          if (values.mobile) {
            params = { ...values, mobile: `${values.mobileAreaCode}${values.mobile}` };
          }
          params = {
            ...params,
          };
          const data = await onFinish(params);
          if (data) {
            return Promise.resolve(true);
          }
          return Promise.resolve(false);
        } finally {
          setSubmitLoading(false);
        }
      }}
    >
      <Spin spinning={submitLoading}>
        <ProFormText
          name="username"
          label={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.username',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.username.placeholder',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.setting.administrator.modal.from.username.rule.0.message',
              }),
            },
            {
              pattern: new RegExp('^[a-zA-Z0-9\\-_]{4,}$'),
              message: intl.formatMessage({
                id: 'pages.setting.administrator.modal.from.username.rule.1.message',
              }),
            },
            {
              validator: async (_rule, value) => {
                if (!value) {
                  return Promise.resolve();
                }
                setSubmitLoading(true);
                const { success, data } = await administratorParamCheck(
                  ParamCheckType.USERNAME,
                  value,
                ).finally(() => {
                  setSubmitLoading(false);
                });
                if (!success) {
                  return Promise.reject<Error>();
                }
                if (!data) {
                  return Promise.reject<Error>(
                    new Error(
                      intl.formatMessage({
                        id: 'pages.setting.administrator.modal.from.username.rule.2.message',
                      }),
                    ),
                  );
                }
              },
              validateTrigger: ['onBlur'],
            },
          ]}
          extra={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.username.extra',
          })}
        />
        <ProFormText.Password
          name="password"
          label={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.password',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.password.placeholder',
          })}
          fieldProps={{
            autoComplete: 'new-password',
          }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.setting.administrator.modal.from.password.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormDependency name={['mobileAreaCode']}>
          {({ mobileAreaCode }) => {
            return (
              <ProFormText
                name={['mobile']}
                label={intl.formatMessage({
                  id: 'pages.setting.administrator.modal.from.mobile',
                })}
                placeholder={intl.formatMessage({
                  id: 'pages.setting.administrator.modal.from.mobile.placeholder',
                })}
                validateFirst
                rules={[
                  {
                    pattern: new RegExp('^[0-9]*$'),
                    message: intl.formatMessage({
                      id: 'pages.account.user_list.user.form.mobile.rule.0.message',
                    }),
                  },
                  {
                    validator: validatePhoneOrEmail,
                    validateTrigger: ['onBlur'],
                  },
                  {
                    validator: async (_rule, value) => {
                      if (!value) {
                        return Promise.resolve();
                      }
                      //校验手机号格式
                      setSubmitLoading(true);
                      const isValidNumber = await mobileIsValidNumber(value, mobileAreaCode).finally(
                        () => {
                          setSubmitLoading(false);
                        },
                      );
                      if (!isValidNumber) {
                        return Promise.reject<Error>(
                          new Error(
                            intl.formatMessage({
                              id: 'pages.setting.administrator.modal.from.mobile.rule.0.message',
                            }),
                          ),
                        );
                      }
                      const { success, data } = await administratorParamCheck(
                        ParamCheckType.PHONE,
                        `${mobileAreaCode}${value}`,
                      );
                      if (!success) {
                        return Promise.reject<Error>();
                      }
                      if (!data) {
                        return Promise.reject<Error>(
                          new Error(
                            intl.formatMessage({
                              id: 'pages.setting.administrator.modal.from.mobile.rule.1.message',
                            }),
                          ),
                        );
                      }
                    },
                    validateTrigger: ['onBlur'],
                  },
                ]}
                addonWarpStyle={{
                  flexWrap: 'nowrap',
                }}
                addonBefore={
                  <FormMobileAreaCodeSelect
                    name={'mobileAreaCode'}
                    showSearch
                    noStyle
                    allowClear={false}
                    style={{ maxWidth: '200px' }}
                    fieldProps={{
                      placement: 'bottomLeft',
                    }}
                  />
                }
                fieldProps={{
                  autoComplete: 'off',
                }}
                extra={intl.formatMessage({
                  id: 'pages.setting.administrator.modal.from.mobile.extra',
                })}
              />
            );
          }}
        </ProFormDependency>
        <ProFormText
          name="email"
          label={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.email',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.email.placeholder',
          })}
          validateFirst
          rules={[
            {
              validator: validatePhoneOrEmail,
              validateTrigger: ['onBlur'],
            },
            {
              type: 'email',
              message: intl.formatMessage({
                id: 'pages.setting.administrator.modal.from.email.rule.0.message',
              }),
            },
            {
              validator: async (_rule, value) => {
                if (!value) {
                  return Promise.resolve();
                }
                setSubmitLoading(true);
                const { success, data } = await administratorParamCheck(
                  ParamCheckType.EMAIL,
                  value,
                ).finally(() => {
                  setSubmitLoading(false);
                });
                if (success && !data) {
                  return Promise.reject<Error>(
                    new Error(
                      intl.formatMessage({
                        id: 'pages.setting.administrator.modal.from.email.rule.1.message',
                      }),
                    ),
                  );
                }
              },
              validateTrigger: ['onBlur'],
            },
          ]}
          extra={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.email.extra',
          })}
        />
        <ProFormTextArea
          name="remark"
          fieldProps={{ rows: 2 }}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.remark.placeholder',
          })}
          label={intl.formatMessage({
            id: 'pages.setting.administrator.modal.from.remark',
          })}
        />
      </Spin>
    </ModalForm>
  );
};
