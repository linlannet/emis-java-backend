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
// noinspection DuplicatedCode

import React, { useState } from 'react';
import { App, Button, Drawer, Form, Space, Spin } from 'antd';
import { useAsyncEffect } from 'ahooks';
import { UpdateFormProps } from './data.d';
import { getUser, updateUser, userParamCheck } from '@/services/account';
import { omit } from 'lodash';
import { useIntl } from '@@/exports';
import { mobileIsValidNumber, mobileParseNumber } from '@/utils/utils';
import { useForm } from 'antd/es/form/Form';
import { USER_DRAWER_FORM_ITEM_LAYOUT } from '@/pages/account/UserList/constant';
import {
  ProFormDatePicker,
  ProFormDependency,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { ParamCheckType } from '@/constant';
import FormMobileAreaCodeSelect from '@/components/FormMobileAreaCodeSelect';
import { RangePickerProps } from 'antd/es/date-picker';
import dayjs from 'dayjs';
import { createStyles } from 'antd-style';

/**
 * 基础字段
 *
 * @param props
 * @constructor
 */
const useStyle = createStyles(({ prefixCls }) => {
  return {
    main: {
      [`.${prefixCls}-form-item-control-input`]: {
        width: '100%',
      },
    },
  };
});
const UpdateUser = (props: UpdateFormProps) => {
  const { visible, onCancel, onFinish, id, afterClose } = props;
  const [form] = useForm();
  const [submitLoading, setSubmitLoading] = useState(false);
  const [getUserLoading, setGetUserLoading] = useState(false);
  const intl = useIntl();
  let useApp = App.useApp();
  const { styles } = useStyle();

  const validateMobileOrEmail = () => {
    if (form.getFieldValue('mobile') || form.getFieldValue('email')) {
      return Promise.resolve();
    }
    return Promise.reject(
      new Error(intl.formatMessage({ id: 'pages.account.user_list.user.form.mobile.extra' })),
    );
  };

  const disabledDate: RangePickerProps['disabledDate'] = (current) => {
    return current && current < dayjs().endOf('day');
  };
  /**手机号已更改*/
  const [mobileChanged, setMobileChanged] = useState<boolean>(false);

  useAsyncEffect(async () => {
    if (visible) {
      setGetUserLoading(true);
      const { success, data } = await getUser(id).finally(() => {
        setGetUserLoading(false);
      });
      if (success && data) {
        form.setFieldsValue({ ...omit(data, 'custom') });
        if (data?.mobile) {
          const mobileNumber = mobileParseNumber(data.mobile);
          form.setFieldsValue({ mobile: mobileNumber?.getNationalNumber(), id: id });
        }
      }
    }
  }, [visible]);

  /**
   * 取消
   */
  const cancel = async () => {
    if (onCancel) {
      onCancel();
    }
    setSubmitLoading(false);
    form.resetFields();
  };
  return (
    <Drawer
      width={530}
      open={visible}
      onClose={cancel}
      footer={
        <Space style={{ float: 'right' }}>
          <Button type="default" onClick={cancel}>
            {intl.formatMessage({ id: 'pages.account.user_list.common.cancel' })}
          </Button>
          <Button
            loading={submitLoading}
            type="primary"
            onClick={async () => {
              setSubmitLoading(true);
              //校验基础
              try {
                await form?.validateFields();
              } catch (e) {
                setSubmitLoading(false);
                return;
              }

              const values = {
                ...form?.getFieldsValue(),
              };
              let params = omit(values, 'mobileAreaCode');
              if (values.mobile) {
                params = { ...params, mobile: `${values.mobileAreaCode}${values.mobile}` };
              }
              if (values.expireDate) {
                params = { ...params, expireDate: dayjs(values.expireDate).format('YYYY-MM-DD') };
              }
              const { success, data } = await updateUser(params).finally(() => {
                setSubmitLoading(false);
              });
              if (success && data) {
                useApp.message.success(intl.formatMessage({ id: 'app.operation_success' }));
                onFinish(success);
              }
            }}
          >
            {intl.formatMessage({ id: 'pages.account.user_list.common.save' })}
          </Button>
        </Space>
      }
      destroyOnClose
      afterOpenChange={(open) => {
        if (!open) {
          afterClose();
        }
      }}
    >
      <Spin spinning={submitLoading || getUserLoading}>
        <Form
          labelAlign="right"
          layout="horizontal"
          {...USER_DRAWER_FORM_ITEM_LAYOUT}
          form={form}
          className={styles.main}
          preserve={false}
        >
          <ProFormText name={'id'} hidden={true} />
          <ProFormText
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.username' })}
            name={'username'}
            readonly
          />
          <ProFormText
            name="fullName"
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.full_name' })}
            placeholder={intl.formatMessage({
              id: 'pages.account.user_list.user.form.full_name.placeholder',
            })}
            rules={[
              {
                message: intl.formatMessage({
                  id: 'pages.account.user_list.user.form.full_name.rule.0.message',
                }),
                required: true,
              },
            ]}
            fieldProps={{
              autoComplete: 'off',
            }}
          />
          <ProFormText
            name="nickName"
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.nick_name' })}
            placeholder={intl.formatMessage({
              id: 'pages.account.user_list.user.form.nick_name.placeholder',
            })}
            fieldProps={{
              autoComplete: 'off',
            }}
          />
          <ProFormDependency name={['mobileAreaCode', 'id']}>
            {({ mobileAreaCode, id }) => {
              return (
                <ProFormText
                  name="mobile"
                  placeholder={intl.formatMessage({
                    id: 'pages.account.user_list.user.form.mobile.placeholder',
                  })}
                  label={intl.formatMessage({ id: 'pages.account.user_list.user.form.mobile' })}
                  extra={intl.formatMessage({
                    id: 'pages.account.user_list.user.form.mobile.extra',
                  })}
                  validateFirst={true}
                  rules={[
                    {
                      pattern: new RegExp('^[0-9]*$'),
                      message: intl.formatMessage({
                        id: 'pages.account.user_list.user.form.mobile.rule.0.message',
                      }),
                    },
                    {
                      validator: async (_, value) => {
                        if (!value || !mobileChanged) {
                          return Promise.resolve();
                        }
                        setSubmitLoading(true);
                        //校验手机号格式
                        const isValidNumber = await mobileIsValidNumber(
                          value,
                          mobileAreaCode,
                        ).finally(() => {
                          setSubmitLoading(false);
                        });
                        if (!isValidNumber) {
                          return Promise.reject<Error>(
                            new Error(
                              intl.formatMessage({
                                id: 'pages.account.user_list.user.form.mobile.rule.0.message',
                              }),
                            ),
                          );
                        }
                        setSubmitLoading(true);
                        const { success, data } = await userParamCheck(
                          ParamCheckType.PHONE,
                          `${mobileAreaCode}${value}`,
                          id,
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
                                id: 'pages.account.user_list.user.form.mobile.rule.1.message',
                              }),
                            ),
                          );
                        }
                      },
                      validateTrigger: ['onBlur'],
                    },
                    {
                      validateTrigger: ['onBlur'],
                      validator: validateMobileOrEmail,
                    },
                  ]}
                  addonWarpStyle={{
                    flexWrap: 'nowrap',
                  }}
                  addonBefore={
                    <FormMobileAreaCodeSelect name={'mobileAreaCode'} showSearch noStyle />
                  }
                  fieldProps={{
                    autoComplete: 'off',
                    onChange: () => {
                      setMobileChanged(true);
                    },
                  }}
                />
              );
            }}
          </ProFormDependency>
          <ProFormText
            name="email"
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.email' })}
            placeholder={intl.formatMessage({
              id: 'pages.account.user_list.user.form.email.placeholder',
            })}
            rules={[
              {
                type: 'email',
                message: intl.formatMessage({
                  id: 'pages.account.user_list.user.form.email.rule.1.message',
                }),
              },
              {
                validator: async (_rule, value) => {
                  if (!value) {
                    return Promise.resolve();
                  }
                  setSubmitLoading(true);
                  const { success, data } = await userParamCheck(
                    ParamCheckType.EMAIL,
                    value,
                    form.getFieldValue('id'),
                  ).finally(() => {
                    setSubmitLoading(false);
                  });
                  if (success && !data) {
                    return Promise.reject<Error>(
                      new Error(
                        intl.formatMessage({
                          id: 'pages.account.user_list.user.form.email.rule.2.message',
                        }),
                      ),
                    );
                  }
                },
                validateTrigger: ['onBlur'],
              },
              {
                validator: validateMobileOrEmail,
                validateTrigger: ['onBlur'],
              },
            ]}
            fieldProps={{
              autoComplete: 'off',
            }}
            extra={intl.formatMessage({ id: 'pages.account.user_list.user.form.email.extra' })}
          />
          <ProFormDatePicker
            name="expireDate"
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.expire_time' })}
            placeholder={intl.formatMessage({
              id: 'pages.account.user_list.user.form.expire_time',
            })}
            fieldProps={{
              disabledDate: disabledDate,
            }}
            extra={intl.formatMessage({
              id: 'pages.account.user_list.user.form.expire_time.extra',
            })}
          />
          <ProFormTextArea
            name={'remark'}
            label={intl.formatMessage({ id: 'pages.account.user_list.user.form.remark' })}
            extra={intl.formatMessage({ id: 'pages.account.user_list.user.form.remark.extra' })}
            fieldProps={{
              placeholder: intl.formatMessage({ id: 'pages.account.user_list.user.form.remark' }),
              rows: 2,
              maxLength: 20,
              autoComplete: 'off',
              showCount: true,
            }}
          />
        </Form>
      </Spin>
    </Drawer>
  );
};

export default UpdateUser;
