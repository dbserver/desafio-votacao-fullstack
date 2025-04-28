import React, { useEffect, useState } from "react";
import { Card, Spin, Space, Button, Tooltip, message, Badge, Modal, Divider, Pagination } from "antd";
import { CheckOutlined, CloseOutlined, PlusOutlined, DeleteOutlined } from "@ant-design/icons";
import "./AllDiscuss.css";
import apiService from "../../service/api/api";
import DiscussModal from "../../components/Modais/CreateNewDiscuss";
import NewVote from "../../components/Modais/NewVote";
import Associate from "../../components/Modais/Associate"
import { enumStatusMap } from "../../service/api/util/EnumStatus";

const AllDiscuss = () => {
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [data, setData] = useState([]);
  const [voteValue, setVoteValue] = useState(null);
  const [discussId, setDiscussId] = useState();
  const [isVoteVisible, setIsVoteVisible] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchStatus, setSearchStatus] = useState(0);
  const [totalDiscusses, setTotalDiscusses] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [isAssociateModalVisible, setIsAssociateModalVisible] = useState(false);
  const [associateName, setAssociateName] = useState("");
  const [associateCpf, setAssociateCpf] = useState("");
  const [pendingVoteValues, setPendingVoteValues] = useState(null);


  useEffect(() => {
    fetchDiscusses(currentPage - 1, searchStatus);
  }, [currentPage, searchStatus]);

  const fetchDiscusses = async (page, status) => {
    setLoading(true);
    try {
      const response = await apiService.getAllDiscusses(page, status);
      setData(response.data.content);
      setTotalDiscusses(response.data.totalElements);
    } catch (error) {
      message.error("Erro ao carregar as pautas");
    } finally {
      setLoading(false);
    }
  };

  const deleteDiscuss = async (id) => {
    Modal.confirm({
      title: "Deseja realmente deletar esta pauta?",
      okText: "Sim",
      cancelText: "Cancelar",
      onOk: async () => {
        setLoading(true);
        try {
          await apiService.deleteDiscuss(id);
          await fetchDiscusses(currentPage -1, 0);
          message.success("Pauta deletada com sucesso");
        } catch (error) {
          message.error("Erro ao deletar a pauta");
        } finally {
          setLoading(false);
        }
      },
    });
  };


  const handleAssociateRegister = async (values) => {
    if (!values.associateCpf.trim()) {
      message.warning("Preencha o campo de cpf corretamente");
      return;
    }

    try {
      await apiService.registerAssociate({ name: associateName, cpf: values.associateCpf.replace(/\D/g, "") }).then((response) => {
        if(response === "ASSOCIATED_REGISTERED_SUCCESSFULLY"){
        message.success("Associado cadastrado com sucesso!");
        }else{
          message.success("Associado já cadastrado!");
        }
      })
      

      if (pendingVoteValues) {
        await continueVote(values.associateCpf);
      }
      setIsAssociateModalVisible(false);
      setIsVoteVisible(false);
    } catch (error) {
      message.error("Erro ao cadastrar associado");
    }
  };

  const handleAddVote = async (value, id) => {
    Modal.confirm({
      title: "Você já é um associado?",
      okText: "Sim",
      cancelText: "Não",
      onOk: async () => {
        handleVote(value, id)
      },
      onCancel: () => {
        setVoteValue(value);
        setDiscussId(id);
        setAssociateCpf(value);
        setIsAssociateModalVisible(true);
        setPendingVoteValues(value);
      },
    });
  };

  const continueVote = async (associatedCpf) => {
    const vote = {
      associatedCpf: associatedCpf !== "" ? associatedCpf.replace(/\D/g, "") : associateCpf.replace(/\D/g, ""),
      vote: voteValue,
      discussId,
    };

    try {
      const { data: responseData } = await apiService.addVote(vote);
      const messages = {
        ABLE_TO_VOTE: () => message.success("Voto registrado com sucesso"),
        CPF_ALREADY_VOTED: () => message.warning("Você já votou"),
        UNABLE_TO_VOTE: () => message.error("Você não está habilitado a votar"),
        CPF_INVALID: () => message.error("CPF inválido"),
        ASSOCIATED_NOT_REGISTERED: () => message.error("Associado não cadastrado"),
      };
      (messages[responseData] || (() => { }))();
      await fetchDiscusses(currentPage -1 , 0);
      setIsVoteVisible(false);
    } catch (error) {
      message.error("Erro ao votar na pauta");
    }
  };

  const handleAddDiscuss = async (values) => {
    const newDiscuss = {
      name: values.name,
      description: values.description,
      session: { duration: values.duration, status: 0 },
    };

    try {
      await apiService.addDiscuss(newDiscuss);
      message.success("Pauta adicionada com sucesso");
      await fetchDiscusses(currentPage -1, 0);
      setIsModalVisible(false);
    } catch (error) {
      message.error("Erro ao adicionar a pauta");
    }
  };

  const handleVote = (value, id) => {
    setVoteValue(value);
    setDiscussId(id);
    setIsVoteVisible(true);
  };

  const renderVoteButtons = (rowData) => {
    const isVoteDisabled = rowData.session.status === 2;
    return (
      <Space>
        <Tooltip title="Votar Sim">
          <Button
            shape="circle"
            icon={<CheckOutlined />}
            disabled={isVoteDisabled}
            onClick={() => handleAddVote("sim", rowData.id)}
          />
          {isVoteDisabled && <span style={{ marginLeft: 8 }}>{rowData.totalVotesYes}</span>}
        </Tooltip>
        <Tooltip title="Votar Não">
          <Button
            shape="circle"
            icon={<CloseOutlined />}
            disabled={isVoteDisabled}
            onClick={() => handleAddVote("não", rowData.id)}
          />
          {isVoteDisabled && <span style={{ marginLeft: 8 }}>{rowData.totalVotesNo}</span>}
        </Tooltip>
      </Space>
    );
  };

  const circleStatus = (status) => {
    const colors = {
      1: "#4CAF50",
      2: "#ff4d4f",
    };
    return (
      <Badge color={colors[status] || "#ccc"} text={enumStatusMap[status] || "Status desconhecido"} />
    );
  };

  return (
    <div className="container" style={{ minHeight: "100vh", padding: "20px", backgroundColor: "#fff" }}>
      <Card
        title="Pautas"
        className="card"
        headStyle={{ backgroundImage: "linear-gradient(90deg, #003366 0%, #0050b3 100%)", color: "white", borderTopLeftRadius: "12px", borderTopRightRadius: "12px" }}
        extra={
          <Button
            icon={<PlusOutlined />}
            type="primary"
            style={{ backgroundImage: "linear-gradient(90deg, #003366 0%, #0050b3 100%)", borderRadius: '12px' }}
            onClick={() => setIsModalVisible(true)}
          >
            Nova Pauta
          </Button>
        }
      >
        {loading ? (
          <Spin />
        ) : (
          <>
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
                gap: "20px",
                paddingBottom: "20px",
              }}
            >
              {data.map((pauta) => (
                <Card
                  key={pauta.id}
                  hoverable
                  style={{
                    width: "100%",
                    height: "100%",
                    padding: "15px",
                    boxSizing: "border-box",
                    position: "relative",
                    transition: "transform 0.3s ease",
                    backgroundColor: "#e6f3fb",
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      marginBottom: "20px",
                    }}
                  >
                    <h2 style={{ fontWeight: "bold", fontSize: "clamp(16px, 2vw, 18px)", margin: 0 }}>{pauta.name}</h2>
                    <div style={{ marginLeft: "20px" }}>{circleStatus(pauta.session?.status)}</div>
                  </div>
                  <Divider style={{ margin: "12px 0" }} />
                  <div style={{ marginBottom: "20px" }}>
                    <h3 style={{ fontSize: "clamp(14px, 1.5vw, 16px)", margin: 0 }}>{pauta.description}</h3>
                  </div>
                  {renderVoteButtons(pauta)}
                  <Button
                    type="text"
                    icon={<DeleteOutlined style={{ color: "#ff4d4f", fontSize: "20px" }} />}
                    onClick={() => deleteDiscuss(pauta.id)}
                    style={{
                      position: "absolute",
                      bottom: "10px",
                      right: "10px",
                      background: "transparent",
                      border: "none",
                    }}
                  />
                </Card>
              ))}
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', marginTop: 20 }}>
              <Pagination
                current={currentPage}
                pageSize={pageSize}
                total={totalDiscusses}
                onChange={(page, pageSize) => setCurrentPage(page)}
                showSizeChanger={false}
              />
            </div>
          </>
        )}
      </Card>

      <DiscussModal
        visible={isModalVisible}
        onCreate={handleAddDiscuss}
        onCancel={() => setIsModalVisible(false)}
      />

      {isAssociateModalVisible && (
        <Associate
          setAssociateCpf={setAssociateCpf}
          associateCpf={associateCpf}
          message={message}
          setIsVoteVisible={setIsVoteVisible}
          handleAssociateRegister={handleAssociateRegister}
          associateName={associateName}
          setAssociateName={setAssociateName}
          setIsAssociateModalVisible={setIsAssociateModalVisible}
          isAssociateModalVisible={isAssociateModalVisible}
        
        />
      )}

      {isVoteVisible && (
        <NewVote
          continueVote={continueVote}
          visible={isVoteVisible}
          onCancel={() => setIsVoteVisible(false)}
          setAssociateCpf={setAssociateCpf}
        />
      )}
    </div>
  );
};

export default AllDiscuss;