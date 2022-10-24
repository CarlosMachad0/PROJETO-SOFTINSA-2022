import React, {useState, useEffect} from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/editar.scss';
import Swal from 'sweetalert2';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form'
import {FiEdit}  from 'react-icons/fi';
import { IconContext } from "react-icons";
import { Link, useParams, useNavigate } from "react-router-dom";
import Menu from '../../components/NavBar/SideBar';


function EditCentro() {

  const [Data, setData] = useState([]);
  const [Nome, setNome] = useState("");
  const [Morada, setMorada] = useState("");
  const [Hora_abertura, setHora_abertura] = useState("")
  const [Hora_fecho, setHora_fecho] = useState("")
  const [Telefone, setTelefone] = useState("")
  const Navigate = useNavigate();

  const{id} = useParams();

  useEffect(()=>{
    axios.get("centros/get/" + id)
    .then(res=>{
      if(res.data.sucesso){
        const Data = res.data.data;
        setNome(Data.Nome);
        setMorada(Data.Endereco);
        setHora_abertura(Data.Hora_abertura);
        setHora_fecho(Data.Hora_fecho);
        setTelefone(Data.Telefone);
      }else{
        alert("Erro ao obter info do centro");
      }
    })
    .catch(error=>{
      alert(error);
    });
  },[]);


  return (
    <Menu>
    <Container fluid>
      <Row className='mt-3'>
        <Col className='d-flex justify-content-start'>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <FiEdit/>
                </div>
            </IconContext.Provider>
            <h1 className='Titulo_Pagina mx-4'>Editar Centro </h1>
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

            {/**Endereco */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Endereço</Form.Label>
              <br/>
              <Form.Control size="sm" type="text" value={Morada} onChange={(value) => setMorada(value.target.value)} className='formulario-input'/>
            </Form.Group>

            {/**Telefone */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Telefone</Form.Label>
              <br/>
              <Form.Control size="sm" type="text" value={Telefone} onChange={(value) => setTelefone(value.target.value)} className='formulario-input'/>
            </Form.Group>

            {/**Horario */}
            <Form.Group>
              <Form.Label className='formulario-label-input'>Hora Inicio</Form.Label>
              <br/>
              <Form.Control size="sm" type="time" value={Hora_abertura} onChange={(value) => setHora_abertura(value.target.value)} className='formulario-input'/>
            </Form.Group>

            <Form.Group className='pb-3'>
              <Form.Label className='formulario-label-input'>Hora Fim</Form.Label>
              <br/>
              <Form.Control size="sm" type="time" value={Hora_fecho} onChange={(value) => setHora_fecho(value.target.value)} className='formulario-input'/>
            </Form.Group>
          </Form>
          </div>
        </Col>
      </Row>
      <Row>
        <Col className='my-5 mx-5 d-flex justify-content-end fixed-bottom px-5' >
          <Button className='BotaoCancelar d-flex justify-content-center' onClick={()=>Navigate(-1)}>Cancelar</Button>
          <Button className='BotaoConfirmar d-flex justify-content-center'onClick={()=>OnUpdate()}>Concluir</Button>
        </Col>
      </Row>
    </Container>
    </Menu>
  )

    function OnUpdate(){
        Swal.fire({
            title: 'Deseja editar este Centro?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar'
        })
        .then((result) => {
            if (result.value) {
                SendUpdate()
            } 
        })
    } 

    function SendUpdate(){
        if (Nome==="") {
            Swal.fire('Insira um nome')
        }
        else if (Morada==="") {
            Swal.fire('Insira uma morada')
        }
        else if (Hora_abertura==="") {
            Swal.fire('Insira uma hora início')
        }
        else if (Hora_fecho==="") {
            Swal.fire('Insira uma hora fim')
        }
        else  if(Telefone===""){
            Swal.fire('Insira um número de telefone')
        }
        else if( /^\d+$/.test(Telefone) == 0)
              Swal.fire('O numero de telefone so pode ter digitos')
        else {
            const datapost = {
                    Nome : Nome,
                    Morada : Morada,
                    Telefone: Telefone,
                    Hora_abertura: Hora_abertura,
                    Hora_fecho: Hora_fecho
            }
            axios.put("centros/update/"+id,datapost)
            .then(response=>{
                if (response.data.sucesso===true) {
                    Swal.fire(
                        'Sucesso!',
                        'O centro foi atualizado',
                        'success'
                    );
                    Navigate('/home/centros/list');
                }
                else {
                    Swal.fire(
                    'Erro!',
                    response.data.message,
                    'error'
                )
                }
            })
            .catch(error=>{
                alert("Error: "+error)
            })
        }
    }
}

export default EditCentro