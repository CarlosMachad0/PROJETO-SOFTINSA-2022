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
import { useNavigate, useParams } from "react-router-dom";
import {FiEdit}  from 'react-icons/fi';
import Menu from '../../components/NavBar/SideBar';

function EditUtilizador() {

  const{id} = useParams()

  const [PNome, setPNome] = useState("");
  const [UNome, setUNome] = useState("");
  const [Email, setEmail] = useState("");
  const [Cargo,setCargo] = useState("");
  const [TipoGestor,setTipoGestor] = useState("");
  const [PalavraPasse,setPalavraPasse] = useState("");
  const [CentroId, setCentroId] = useState("");

  const [Centros,setCentros] = useState([]);
  const [Cargos,setCargos] = useState([]);
  const [TiposGestor,setTiposGestor] = useState([]);
  const [UmCentro, setUmCentro] = useState({})

  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const idcentro = user.CentroId

  const Navigate = useNavigate();

  var datapost

  useEffect(()=>{
    axios.get('utilizadores/get/'+id)
    .then(response=>{
      if(response.data.sucesso){
        /* console.log(response.data.data)
        console.log(response.data.data.Pnome)
        console.log(response.data.data.Unome) */
        setPNome(response.data.data.Pnome)
        setUNome(response.data.data.Unome)
        setEmail(response.data.data.Email)
        setCargo(response.data.data.CargoId)
        setTipoGestor(response.data.data.TiposGestorId)
        setCentroId(response.data.data.CentroId)
      }else{
        console.log('Nao foi possivel obter as informacoes do user');
      }
    })
  },[])

  /* useEffect(()=>{
    axios.get('utilizadores/pertence/'+id)
    .then(response=>{
      if(response.data.sucesso){
        setCentroId(response.data.data.CentroId)
      }else{
        console.log('Nao foi possivel obter as informacoes do user');
      }
    })
  },[]) */

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

  useEffect(()=> {
        axios.get('testdata/listCargos')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                setCargos(data);
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
  },[])

  useEffect(()=> {
        axios.get('testdata/listTiposGestores')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                setTiposGestor(data);
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
  },[])

  return (
    <Menu>
    <Container fluid>
      <Row className='mt-3'>
        <Col className=' d-flex justify-content-start'>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <FiEdit/>
                </div>
            </IconContext.Provider>
            <h1 className='Titulo_Pagina mx-4'>Editar Utilizador </h1>
        </Col>
      </Row>
      <Row>
        <Col className=' mt-2 d-flex justify-content-start' >
          <div className='formulario'>
            <p className='formulario-titulo'>Formulário</p>
          <Form>
            <Row>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Nome</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={PNome} onChange={(value) => setPNome(value.target.value)} className='formulario-input'/>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>Apelido</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={UNome} onChange={(value) => setUNome(value.target.value)} className='formulario-input'/>
                </Form.Group>
              </Col>
            </Row>
            <Row className='pt-2'>
              <Col>
                  <Form.Group>
                    <Form.Label className='formulario-label-input'>Cargo</Form.Label>
                    <Form.Select value={Cargo} onChange={(value) => setCargo(value.target.value)} className="formulario-input visible" size="sm">
                      <option>Selecione um Cargo</option>
                          <LoadCargoData />
                    </Form.Select>
                  </Form.Group>
              </Col>
              <Col>
                  <Form.Group>
                    <Form.Label className='formulario-label-input'>Tipo Gestor</Form.Label>
                    <Form.Select value={TipoGestor} onChange={(value) => setTipoGestor(value.target.value)} className="formulario-input visible" size="sm">
                      <option>Selecione um Tipo Gestor</option>
                          <LoadTipoGestorData />
                    </Form.Select>
              </Form.Group>
              </Col>
            </Row>
            <Row className='pt-2'>
              <Col>
                <Form.Group>
                  <Form.Label className='formulario-label-input'>E-mail</Form.Label>
                  <br/>
                  <Form.Control size="sm" type="text" value={Email} onChange={(value) => setEmail(value.target.value)} className='formulario-input-largo'/>
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col>
                  <Form.Group className='py-2'>
                    <Form.Label className='formulario-label-input'>Centros</Form.Label>
                    <Form.Select value={CentroId} onChange={(value) => setCentroId(value.target.value)} className='formulario-input-largo' size="sm">
                          if(tipoGestor === 1)
                            <LoadCentroData />
                          else
                            <LoadCentro />
                    </Form.Select>
              </Form.Group>
              </Col>
            </Row>
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

  function LoadCargoData() {
        return Cargos.map((data, index) => {
            return (
                <option key={index} value={data.id}>{data.descricao}</option>
            )
        });
  }

  function LoadTipoGestorData() {
        return TiposGestor.map((data, index) => {
            return (
                <option key={index} value={data.id}>{data.descricao}</option>
            )
        });
  }

  function OnSave(){
        Swal.fire({
            title: 'Deseja editar o utilizador?',
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
      /* console.log(PNome)
      console.log(UNome)
      console.log(Email)
      console.log(PalavraPasse) */
      //console.log(Cargo)
      //console.log(TipoGestor)
      /* console.log(CentroId)
      console.log(id) */
        if (PNome==="") {
              Swal.fire('Insira um nome')
          }
          else if (UNome==="") {
              Swal.fire('Insira um apelido')
          }
          else if (Email ==="") {
              Swal.fire('Insira um email')
          }
          else  if(Cargo ==="" || Cargo === "0"){
              Swal.fire('Selecione um cargo')
          }
          else  if(CentroId ==="" || CentroId === "0"){
              Swal.fire('Selecione um centro')
          }
          else  if(Cargo === "1" && TipoGestor === ""){
              Swal.fire('Selecione um tipo de gestor')
          }else if(Cargo === "1" && TipoGestor === null){
              Swal.fire('Selecione um tipo de gestor')
          }else{ 
            if(Cargo === 1){
                const datapost = {
                  PNome : PNome,
                  UNome : UNome,
                  Email : Email,
                  PalavraPasse: PalavraPasse,
                  Cargos : Cargo,
                  TipoGestor : TipoGestor,
                  Centros: CentroId
                }  
                //console.log(datapost)
                axios.put("utilizadores/update/" + id,datapost)
                .then(response=>{
                if (response.data.sucesso===true) {
                    Swal.fire(
                            'Sucesso!',
                            'O utilizador foi editado',
                            'success'
                    );
                    Navigate('/home/utilizadores/list')
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
            }else{
                const datapost = {
                  PNome : PNome,
                  UNome : UNome,
                  Email : Email,
                  PalavraPasse: PalavraPasse,
                  Cargos : Cargo,
                  TipoGestor : 0,
                  Centros: CentroId
                }
                  axios.put("utilizadores/update/" + id,datapost)
                .then(response=>{
                if (response.data.sucesso===true) {
                    Swal.fire(
                            'Sucesso!',
                            'O utilizador foi editado',
                            'success'
                    );
                    Navigate('/home/utilizadores/list')
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
}

export default EditUtilizador