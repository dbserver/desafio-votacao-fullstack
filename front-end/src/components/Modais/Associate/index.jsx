import React, { useEffect } from "react";
import { Modal, Form, Input } from "antd";
import InputMask from 'react-input-mask';

const Associate = ({ handleAssociateRegister,isAssociateModalVisible, setIsAssociateModalVisible  }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (isAssociateModalVisible) {
      form.resetFields();
    }
  }, [isAssociateModalVisible]);


   const CPFInput = (props) => {
      return (
        <InputMask mask="999.999.999-99" value={props.value} onChange={props.onChange}>
          {() => <Input {...props} />}
        </InputMask>
      );
    };

  return (
    <Modal
    title="Cadastrar Novo Associado"
    open={isAssociateModalVisible}
    onCancel={() => setIsAssociateModalVisible(false)}
    onOk={() => form.submit()}
    okText="Cadastrar"
    cancelText="Cancelar"
  >
    <Form
      form={form}
      layout="vertical"
      onFinish={(values) => {
        handleAssociateRegister(values);
        form.resetFields();
      }}
    >
  
      <Form.Item
        name="associateCpf"
        label="CPF"
        rules={[
          { required: true, message: "Por favor insira o CPF!" },
        ]}
      >
        <CPFInput />
      </Form.Item>
    </Form>
  </Modal>
  
  );
};


export default Associate;