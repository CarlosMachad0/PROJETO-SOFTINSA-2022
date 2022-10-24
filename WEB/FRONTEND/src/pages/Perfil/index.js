import React, {useState, useEffect} from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/adicionar.scss'
import Container from 'react-bootstrap/Container'
import Image from 'react-bootstrap/Image'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form'
import Swal from 'sweetalert2';
import axios from 'axios';
import { IconContext } from "react-icons";
import { useNavigate, useParams, Link } from "react-router-dom";
import {CgProfile}  from 'react-icons/cg';
import Menu from '../../components/NavBar/SideBar';

function Perfil() {

    const [PNome, setPNome] = useState("");
    const [UNome, setUNome] = useState("");
    const [Email, setEmail] = useState("");
    const [Cargo,setCargo] = useState("");
    const [TipoGestor,setTipoGestor] = useState("");
    const [Estado,setEstado] = useState("");
    const [CentroId, setCentroId] = useState("");

    const Navigate = useNavigate();

    const id = localStorage.getItem('id')

    var datapost
    var json

    useEffect(()=>{
        axios.get('utilizadores/get/'+id)
        .then(response=>{
        if(response.data.sucesso){
            /* console.log(response.data.data)
            console.log(response.data.data.Pnome)
            console.log(response.data.data.Unome)
            console.log(response.data.data.Email)
            console.log(response.data.data.Cargo.descricao)
            console.log(response.data.data.TiposGestor.descricao)
            console.log(response.data.data.Estado.descricao) */
            setPNome(response.data.data.Pnome)
            setUNome(response.data.data.Unome)
            setEmail(response.data.data.Email)
            setCargo(response.data.data.Cargo.descricao)
            setTipoGestor(response.data.data.TiposGestor.descricao)
            setEstado(response.data.data.Estado.descricao)
        }else{
            console.log('Nao foi possivel obter as informacoes do user');
        }
        })
    },[])

    useEffect(()=>{
        axios.get('utilizadores/pertence/'+id)
        .then(response=>{
            console.log(response.data.data[0].Nome)
        if(response.data.sucesso){
            setCentroId(response.data.data[0].Nome)
        }else{
            console.log('Nao foi possivel obter as informacoes do user');
        }
        })
    },[])

    return (
    <Menu>
    <Container fluid>
      <Row className='mt-3'>
        <Col className=' d-flex justify-content-start'>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <CgProfile/>
                </div>
            </IconContext.Provider>
            <h1 className='Titulo_Pagina mx-4'>{PNome} {UNome} </h1>
        </Col>
      </Row>
      <Row>
        <Col className=' mt-2 d-flex justify-content-start' >
          <div className='formulario'>
            <p className='formulario-titulo'>Dados</p>
          <Form>
            <Row>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Nome</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={PNome} readOnly className='formulario-input'/>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Apelido</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={UNome} readOnly className='formulario-input'/>
                </Form.Group>
              </Col>
            </Row>
            <Row className='pt-2'>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Cargo</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={Cargo} readOnly className='formulario-input'/>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Tipo Gestor</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={TipoGestor} readOnly className='formulario-input'/>
                </Form.Group>
              </Col>
            </Row>
            <Row className='pt-2'>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>E-mail</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={Email} readOnly className='formulario-input-largo'/>
                </Form.Group>
              </Col>
            </Row>
            <Row className='pt-2 pb-4'>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Centro</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={CentroId} readOnly className='formulario-input-largo'/>
                </Form.Group>
              </Col>
            </Row>
            {/* <Row className='py-4'>
                <Col className='d-flex justify-content-end px-5'>
                    <Link className="btn btn-warning botaoAcao mx-2" to={"/home/utilizadores/edit/" + id}>Editar</Link>
                </Col>
            </Row> */}
          </Form>
          </div>
        </Col>
      </Row>
      <Row>
        <Col className='my-5 mx-5 d-flex justify-content-end fixed-bottom px-5' >
          <Button className='BotaoVoltar d-flex justify-content-center' onClick={()=>Navigate(-1)}>Voltar</Button>
        </Col>
      </Row>
    </Container>
    </Menu>
  )
}

export default Perfil