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

function EditSala() {

  const [DataSala, setDataSala] = useState([])

  const [Nome, setNome] = useState("");
  const [Capacidade, setCapacidade] = useState("");
  const [Alocacao, setAlocacao] = useState("");
  const [Tempo_Limpeza, setTempo_Limpeza] = useState("");
  const [Motivo_Bloqueio, setMotivo_Bloqueio] = useState("");

  const [CentroId, setCentroId] = useState("");
  const [CentroString, setCentroString] = useState("");

   const [UmCentro, setUmCentro] = useState({})

  const [Estado,setEstado] = useState("");

  const [classe,setClasse] = useState("");

  const [Centros,setCentros] = useState([]);

  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const idcentro = user.CentroId
  const Navigate = useNavigate();

  const{id} = useParams();

  useEffect(()=>{
    axios.get("salas/get/" + id)
    .then(res=>{
      if(res.data.sucesso){
        const Data = res.data.data;

        setDataSala(Data);
        setNome(Data.Nome);
        setCapacidade(Data.Capacidade);
        setAlocacao(Data.Alocacao)
        setTempo_Limpeza(Data.Tempo_Limpeza);
        setMotivo_Bloqueio(Data.Motivo_Bloqueio);
        setCentroId(Data.CentroId)

        setCentroString(Data.Centro.Nome);
        setEstado(Data.EstadoId);

      }else{
        alert("Erro ao obter info do centro");
      }
    })
    .catch(error=>{
      alert(error);
    });
  },[]);

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

        <Col className='d-flex justify-content-start'>

            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <FiEdit/>
                </div>
            </IconContext.Provider>

            <h1 className='Titulo_Pagina mx-4'>Editar Sala </h1>

        </Col>

      </Row>

      <Row>

        <Col className=' mt-2 d-flex justify-content-start' >

          <div className='formulario'>

            <p className='formulario-titulo'>Formulário</p>

            <Form>

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

              <Form.Group>

                    <Form.Label className='formulario-label-input'>Centros</Form.Label>

                    <Form.Select size="sm" value={CentroId} onChange={(value) => setCentroId(value.target.value)} className='formulario-input'>
                          if(tipoGestor === 1)
                            <LoadCentroData />
                          else
                            <LoadCentro />
                    </Form.Select>

              </Form.Group>

              <Form.Group className={classe}>

                <Form.Label className='formulario-label-input'>Motivo Bloqueio</Form.Label>

                <br/>

                <Form.Control size="sm" type="text" value={Motivo_Bloqueio} onChange={(value) => setMotivo_Bloqueio(value.target.value)} className='formulario-input'/>

              </Form.Group>

            </Form>

          </div>

        </Col>

      </Row>

      <Row>

        <Col className='my-5 mx-5 d-flex justify-content-end fixed-bottom px-5' >

          <Button className='BotaoCancelar d-flex justify-content-center' onClick={()=>Navigate(-1)}>Cancelar</Button>

          <Button className='BotaoConfirmar d-flex justify-content-center'onClick={()=>OnUpdate()}>Confirmar</Button>

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

  function OnUpdate(){
        Swal.fire({
            title: 'Deseja editar esta sala?',
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
        else if (Capacidade==="") {
            Swal.fire('Insira uma capacidade válida')
        }
        else if (Alocacao==="") {
            Swal.fire('Insira uma Alocacao válida')
        }
        else  if(Tempo_Limpeza===""){
            Swal.fire('Insira um tempo de limpeza válido')
        }
        else  if(CentroId===0 || CentroId == "" || CentroId==="Selecione um Centro"){
            Swal.fire('Escolha um centro')
        }
        else  if(Motivo_Bloqueio==="" && Estado===2){
            Swal.fire('Insira um motivo de bloqueio')
        }
        else {
            const datapost = {
                    Nome : Nome,
                    Capacidade : Capacidade,
                    Alocacao: Alocacao,
                    TempoLimpeza: Tempo_Limpeza,
                    Centro: CentroId,
                    Estado: Estado,
                    MotivoBloqueio: Motivo_Bloqueio
            }
            axios.put("salas/update/"+id,datapost)
            .then(response=>{
                if (response.data.sucesso===true) {
                    Swal.fire(
                        'Sucesso!',
                        'A sala foi atualizada',
                        'success'
                    );
                    Navigate('/home/salas/list')
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

export default EditSala