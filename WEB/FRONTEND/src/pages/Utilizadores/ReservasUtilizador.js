import React,{useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/listar.scss';
import Swal from 'sweetalert2';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Table from 'react-bootstrap/Table';
import {BsFillCalendarDateFill}  from 'react-icons/bs';
import { IconContext } from "react-icons";
import { useNavigate, useParams } from "react-router-dom";
import Menu from '../../components/NavBar/SideBar';

function PerfilUtilizador() {

    const{id} = useParams()

    const [DataReservas, setDataReservas] = useState([]);
    const [Pnome, setPnome] = useState("")
    const [Unome, setUnome] = useState("")

    const utilizador = localStorage.getItem('user');
    var user = JSON.parse(utilizador)
    const tipoGestor = user.TiposGestorId; 
    const idcentro = user.CentroId
    const Navigate = useNavigate();

    useEffect(()=>{
      if(tipoGestor === 1){
        const datapost = {
          centroId: idcentro
        }
        axios.get('reservas/listReservas/'+id,datapost)
        .then(response=>{
          //console.log(response.data.data)
            if(response.data.sucesso){
                setDataReservas(response.data.data)
            }else{
                console.log('Nao foi possivel obter as reservas do utilizador');
            }
            })
      }else{
        axios.get('reservas/todasreservas/'+id)
        .then(response=>{
          //console.log(response.data.data)
            if(response.data.sucesso){
                setDataReservas(response.data.data)
            }else{
                console.log('Nao foi possivel obter as reservas do utilizador');
            }
            })
      }
    },[])

    useEffect(() => {
        LoadData();
    },[]); 

 
  return (
    <Menu>
    <Container fluid>
      <Row className='mt-3'>
        <Col className='mt-2 d-flex justify-content-center' md={1}>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <BsFillCalendarDateFill/>
                </div>
            </IconContext.Provider>
        </Col>
        <Col className='px-0 d-flex justify-content-start'>
          <h1 className='Titulo_Pagina pt-2'>{Pnome} {Unome} (Reservas)</h1>
        </Col>
      </Row>
      <Row>
        <Col className='mx-3'>
          <Table className="tabela " striped borderless hover variant="light" responsive="sm" size="sm">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Data</th>
                    <th scope="col">Número de Participantes</th>
                    <th scope="col">Hora Inicio</th>
                    <th scope="col">Hora Fim</th>
                    <th scope="col">Sala</th>
                    <th scope="col">Estado</th>
                    <th colSpan={2}>Ações</th>
                </tr>
            </thead>
            <tbody>
                <LoadTableData/>
            </tbody>
        </Table>
        </Col>
        <Row>
        <Col className='my-5 mx-5 d-flex justify-content-end fixed-bottom px-5' >
          <Button className='BotaoVoltar d-flex justify-content-center' onClick={()=>Navigate(-1)}>Voltar</Button>
        </Col>
      </Row>
      </Row>
    </Container>
    </Menu>
  )

  function LoadData() {
        axios.get('reservas/todasreservas/'+id)
        .then(res => {
            if(res.data.sucesso){
                setDataReservas(res.data.data);
            }else{
                Swal.fire(
                      'Erro',
                      res.data.message,
                      'error'
                  )
            }
        })
        .catch(error => {
            alert(error)
        });
    } 

    function SendAtivarDesativar(id, estado){
      /* console.log(id)
      console.log(estado) */
        if(estado === 1){ 
          axios.put('reservas/desativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'A reserva foi desativada',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                      'Erro',
                      res.data.message,
                      'error'
                  )
            }
          })
          .catch ( error => {
            alert("Error "+error)
          })
        }else{
          axios.put('reservas/ativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'A reserva foi ativada',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                      'Erro',
                      res.data.message,
                      'error'
                  )
            }
          })
          .catch ( error => {
            alert("Error "+error)
          })
        }
    }

    function onAtivarDesativar(id, estado){
      //console.log(estado)
      if(estado===1){
        Swal.fire({
            title: 'Deseja ativar esta reserva?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar',
            closeOnConfirm: false,
            closeOnCancel: false
        })
        .then((result) => {
            if (result.value) {
                SendAtivarDesativar(id, estado)
            } 
        })
      }else{
        Swal.fire({
            title: 'Deseja ativar esta reserva?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar',
            closeOnConfirm: false,
            closeOnCancel: false
        })
        .then((result) => {
            if (result.value) {
                SendAtivarDesativar(id, estado)
            } 
        })
      }
    }

    function SendDelete(id)
    {
        axios.post('reservas/delete/' + id)
        .then(response =>{
            if (response.data.sucesso) {
                Swal.fire(
                    'Eliminado!',
                    'A reserva foi eliminada',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                      'Erro',
                      response.data.message,
                      'error'
                  )
            }
        })
        .catch ( error => {
            alert("Error "+error)
        })
    }


    function OnDelete(id){
        Swal.fire({
            title: 'Deseja eliminar esta reserva?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar',
            closeOnConfirm: false,
            closeOnCancel: false
        })
        .then((result) => {
            if (result.value) {
                SendDelete(id)
            } 
        })
    } 

    function LoadTableData(){
        let botaoEstado = ""

        return DataReservas.map((data, index)=>{

            if(data.Estado.descricao === "Ativo")
              botaoEstado = "Desativar"
            else
              botaoEstado = "Ativar"

            setPnome(DataReservas[index].Utilizadore.Pnome)
            setUnome(DataReservas[index].Utilizadore.Unome)

            return(
                <tr key={index}>
                    <td>{data.NomeReserva}</td>
                    <td>{data.DataReserva}</td>
                    <td>{data.NumeroParticipantes}</td>
                    <td>{data.HoraInicio}</td>
                    <td>{data.HoraFim}</td>
                    <td>{data.Sala.Nome}</td>
                    <td>{data.Estado.descricao}</td>
                    <td>
                        <button className="btn btn-danger botaoAcao" type="button" onClick={()=>OnDelete(data.id)}>Eliminar</button>
                    </td>
                    <td>
                        <button className="btn btn-primary botaoAcao" type="button" onClick={()=>onAtivarDesativar(data.id,data.Estado.id)}>{botaoEstado}</button>
                    </td>
                </tr>
            )
        });
    }
}

export default PerfilUtilizador