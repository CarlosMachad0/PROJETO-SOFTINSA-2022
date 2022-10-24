import React, {useState, useEffect} from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/adicionar.scss'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form'
import Swal from 'sweetalert2';
import axios from 'axios';
import { IconContext } from "react-icons";
import { useNavigate } from "react-router-dom";
import { IoAddCircleOutline } from "react-icons/io5";
import Menu from '../../components/NavBar/SideBar';

function AddSala() {

  const [Nome, setNome] = useState("");
  const [Capacidade, setCapacidade] = useState("");
  const [Alocacao, setAlocacao] = useState("");
  const [Tempo_Limpeza, setTempo_Limpeza] = useState("");

  const [CentroId, setCentroId] = useState("");

  const [Centros,setCentros] = useState([]);
  const [UmCentro, setUmCentro] = useState({})

  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const idcentro = user.CentroId
  //console.log(idcentro)
  const Navigate = useNavigate();

  useEffect(()=> {
    if(tipoGestor === 1){
      axios.get('centros/listCentro/'+idcentro)
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                setUmCentro(data);
                //console.log(UmCentro)
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
    }else{
        axios.get('centros/list')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                setCentros(data);
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
    }
  },[])

  return (
    <Menu>
    <Container fluid>
      <Row className='mt-3'>
        <Col className=' d-flex justify-content-start'>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <IoAddCircleOutline/>
                </div>
            </IconContext.Provider>
            <h1 className='Titulo_Pagina mx-4'>Adicionar Sala </h1>
        </Col>
      </Row>
      <Row>
        <Col className=' mt-2 d-flex justify-content-start' >
          <div className='formulario'>
            <p className='formulario-titulo'>Formulário</p>
          <Form>
            {/**Nome */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Nome</Form.Label>
              <br/>
              <Form.Control size="sm" type="text" value={Nome} onChange={(value) => setNome(value.target.value)} className='formulario-input'/>
            </Form.Group>

            {/**Capacidade */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Capacidade</Form.Label>
              <br/>
              <Form.Control size="sm" type="number" min = "1" value={Capacidade} onChange={(value) => setCapacidade(value.target.value)} className='formulario-input'/>
            </Form.Group>

            {/**Alocacao */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Alocação em %</Form.Label>
              <br/>
              <Form.Control size="sm" type="number" min = "1" max = "100" value={Alocacao} onChange={(value) => setAlocacao(value.target.value)} className='formulario-input'/>
            </Form.Group>

            {/**Tempo Limpeza */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Tempo_Limpeza</Form.Label>
              <br/>
              <Form.Control size="sm" type="time" value={Tempo_Limpeza} onChange={(value) => setTempo_Limpeza(value.target.value)} className='formulario-input'/>
            </Form.Group>

            <Form.Group className='pb-3'>

                    <Form.Label className='formulario-label-input'>Centros</Form.Label>

                    <Form.Select size="sm" value={CentroId} onChange={(value) => setCentroId(value.target.value)} className='formulario-input'>
                          if(tipoGestor === 1)
                            <LoadCentroData />
                          else
                            <LoadCentro />
                    </Form.Select>

              </Form.Group>
          </Form>
          </div>
        </Col>
      </Row>
      <Row>
        <Col className='my-5 mx-5 d-flex justify-content-end fixed-bottom px-5' >
          <Button className='BotaoCancelar d-flex justify-content-center' onClick={()=>Navigate(-1)}>Cancelar</Button>
          <Button className='BotaoConfirmar d-flex justify-content-center'onClick={()=>OnSave()}>Confirmar</Button>
        </Col>
      </Row>
    </Container>
    </Menu>
  )

  function LoadCentro(){
    
            return (
                <option value={UmCentro.id}>{UmCentro.Nome}</option>
            )
  }

  function LoadCentroData() {
        return Centros.map((data, index) => {
            return (
                <option key={index} value={data.id}>{data.Nome}</option>
            )
        });
  }

  function OnSave(){
        Swal.fire({
            title: 'Deseja criar a sala?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar'
        })
        .then((result) => {
            if (result.value) {
                SendSave()
            }
        })
    }  

    function SendSave(){
        if (Nome==="") {
            Swal.fire('Insira um nome')
        }
        else if (Capacidade==="") {
            Swal.fire('Insira uma capacidade válida')
        }
        else if (Alocacao==="") {
            Swal.fire('Insira uma alocação válida')
        }
        else  if(Tempo_Limpeza===""){
            Swal.fire('Insira um tempo de limpeza válido')
        }
        else  if(CentroId=== 0 || CentroId==="Selecione um Centro" || CentroId===""){
            Swal.fire('Escolha um centro')
        }
        else {
          console.log(CentroId)
            const datapost = {
                Nome : Nome,
                Capacidade : Capacidade,
                Alocacao: Alocacao,
                TempoLimpeza : Tempo_Limpeza,
                Centro: CentroId
            }
            axios.post("salas/add",datapost)
            .then(response=>{
            if (response.data.sucesso===true) {
                Swal.fire(
                        'Sucesso!',
                        'A sala foi criada com sucesso',
                        'success'
                );
                Navigate('/home/salas/list')
            }
            else {
                Swal.fire(
                    'Erro',
                    response.data.message,
                    'error'
                )
            }
            }).catch(error=>{
                alert(error)
            })
        }
    }
}

export default AddSala