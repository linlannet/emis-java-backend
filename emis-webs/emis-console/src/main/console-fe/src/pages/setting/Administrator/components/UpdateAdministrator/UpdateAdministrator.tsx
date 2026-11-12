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
import { administratorParamCheck, getAdministrator } from '../../service';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
  ModalForm,
  ProFormDependency,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Skeleton, Spin } from 'antd';
import * as React from 'react';
import { useRef, useState } from 'react';
import { useIntl } from '@umijs/max';
import { mobileIsValidNumber, mobileParseNumber } from '@/utils/utils';
import { ParamCheckType } from '@/constant';
import FormMobileAreaCodeSelect from '@/components/FormMobileAreaCodeSelect';
import { omit } from 'lodash';
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
  id?: string;
  visible: boolean;
  onCancel: (e: React.MouseEvent<HTMLButtonElement>) => void;
  onFinish: (formData: Record<string, string>) => Promise<boolean | void>;
}) => {
  const { visible, onCancel, onFinish, id } = props;
  const formRef = useRef<ProFormInstance>();
  const [updateLoading, setUpdateLoading] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const intl = useIntl();
  /**手机号已更改*/
  const [mobileChanged, setMobileChanged] = useState<boolean>(false);

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

  useAsyncEffect(async () => {
    if (visible && id) {
      setLoading(true);
      const { success, data } = await getAdministrator(id).finally(() => {
        setLoading(false);
      });
      if (success && data) {
        formRef.current?.setFieldsValue({ ...data });
        if (data?.mobile) {
          const mobileNumber = mobileParseNumber(data.mobile);
          formRef.current?.setFieldsValue({
            mobile: mobileNumber?.getNationalNumber(),
            mobileAreaCode: `+${mobileNumber?.getCountryCode() as unknown as string}`,
          });
        }
      }
    }
  }, [visible, id]);

  return (
    <ModalForm
      title={intl.formatMessage({ id: 'pages.setting.administrator.edit_administrator' })}
      width={'500px'}
      {...layout}
      formRef={formRef}
      className={styles.main}
      preserve={false}
      layout={'horizontal'}
      autoFocusFirstInput
      open={visible}
      modalProps={{
        destroyOnClose: true,
        onCancel: (e) => {
          setUpdateLoading(false);
          onCancel(e);
        },
      }}
      onFinish={async (values: Record<string, string>) => {
        try {
          setUpdateLoading(true);
          let params = omit(values, 'mobileAreaCode');
          if (values.mobile) {
            params = { ...values, mobile: `${values.mobileAreaCode || ''}${values.mobile || ''}` };
          }
          await onFinish(params);
        } finally {
          setUpdateLoading(false);
        }
      }}
    >
      <Skeleton loading={loading} active={true}>
        <Spin spinning={updateLoading}>
          <ProFormText name="id" hidden />
          <ProFormText name="initialized" hidden />
          <ProFormText
            name="username"
            label={intl.formatMessage({
              id: 'pages.setting.administrator.modal.from.username',
            })}
            placeholder={intl.formatMessage({
              id: 'pages.setting.administrator.modal.from.username.placeholder',
            })}
            readonly
          />
          <ProFormDependency name={['mobileAreaCode', 'id']}>
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
                        if (!value || !mobileChanged) {
                          return Promise.resolve();
                        }
                        setUpdateLoading(true);
                        //校验手机号格式
                        const isValidNumber = await mobileIsValidNumber(
                          value,
                          mobileAreaCode,
                        ).finally(() => {
                          setUpdateLoading(false);
                        });
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
                          id,
                        ).finally(() => {
                          setUpdateLoading(false);
                        });
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
                    onChange: () => {
                      setMobileChanged(true);
                    },
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
                  const { success, data } = await administratorParamCheck(
                    ParamCheckType.EMAIL,
                    value,
                    id,
                  );
                  if (!success) {
                    return Promise.reject<Error>();
                  }
                  if (!data) {
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
      </Skeleton>
    </ModalForm>
  );
};
