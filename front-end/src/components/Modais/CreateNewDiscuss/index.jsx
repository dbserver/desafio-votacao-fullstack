import React, { useEffect } from "react";
import { Modal, Form, Input, InputNumber } from "antd";

const DiscussModal = ({ visible, onCreate, onCancel }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (visible) {
      form.resetFields();
    }
  }, [visible]);

  return (
    <Modal
      title="Adicionar Pauta"
      open={visible}
      onOk={() => form.submit()}
      onCancel={onCancel}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={(values) => {
          onCreate(values);
          form.resetFields();
        }}
      >
        <Form.Item
          name="name"
          label="Título da pauta"
          rules={[{ required: true, message: 'Por favor insira o título da pauta!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="description"
          label="Descrição da Pauta"
          rules={[{ required: true, message: 'Por favor insira a descrição da pauta!' }]}
        >
          <Input.TextArea />
        </Form.Item>
        <Form.Item
          name="duration"
          label="Duração da sessão da pauta em minutos"
          initialValue={1}
          rules={[{ required: true, message: 'Por favor insira a duração da sessão!' }]}
        >
          <InputNumber min={1} defaultValue={1}/>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DiscussModal;
