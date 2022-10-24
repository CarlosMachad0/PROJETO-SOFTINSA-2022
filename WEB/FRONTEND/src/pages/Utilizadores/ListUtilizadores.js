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
import {FiUsers}  from 'react-icons/fi';
import { IconContext } from "react-icons";
import { Link, useNavigate} from "react-router-dom";
import Menu from '../../components/NavBar/SideBar';

function ListUtilizadores() {

  const Navigate = useNavigate();
  const [DataUtilizadores, setDataUtilizadores] = useState([]);
  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const CentroId = user.CentroId

  useEffect(()=>{
    if(tipoGestor === 1){
      axios.get('utilizadores/listUsers/'+CentroId)
      .then(res=>{
        if(res.data.sucesso){
          setDataUtilizadores(res.data.data)
        }else{
          alert("Error Web Service");
        }
      })
    }else{
      axios.get('utilizadores/list')
      .then(res=>{
        if(res.data.sucesso){
          setDataUtilizadores(res.data.data)
        }else{
          alert("Error Web Service");
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
                  <FiUsers/>
                </div>
            </IconContext.Provider>
        </Col>
        <Col className='px-0 d-flex justify-content-start'>
          <h1 className='Titulo_Pagina pt-2'>Lista de Utilizadores</h1>
        </Col>
        <Col className='mx-3 d-flex justify-content-end'>
          <Button className='botao_adicionar pt-1 h-75 w-25 mt-2' onClick={()=>Navigate('/home/utilizadores/add')}>
          <b>Adicionar Utilizador</b>
          </Button>
        </Col>
      </Row>
      <Row>
        <Col className='mx-3'>
          <Table className="tabela " striped borderless hover variant="light" responsive="sm" size="sm">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Email</th>
                    {/* <th scope="col">Foto</th> */}
                    <th scope="col">Cargo</th>
                    <th scope="col">Tipo Gestor</th>
                    <th scope="col">Centro</th>
                    <th scope="col">Estado</th>
                    <th scope="col" colSpan={5}>Ações</th>
                </tr>
            </thead>
            <tbody>
                <LoadTableData/>
            </tbody>
        </Table>
        </Col>
      </Row>
    </Container>
    </Menu>
  )

  function LoadData() {
        axios.get('utilizadores/list')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                    setDataUtilizadores(data);
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
    } 

    function SendAtivarDesativar(id, estado){
        if(estado === 1){ 
          axios.put('utilizadores/desativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'O utilizador foi desativado',
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
          axios.put('utilizadores/ativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'O utilizador foi ativado',
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
      if(estado===1){
        Swal.fire({
            title: 'Deseja desativar este utilizador?',
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
            title: 'Deseja ativar este utilizador?',
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
        axios.post('utilizadores/delete/' + id)
        .then(response =>{
            if (response.data.sucesso) {
                Swal.fire(
                    'Eliminado!',
                    'O utilizador foi eliminado',
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
            title: 'Deseja eliminar este utilizador?',
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
        let tipoGestor = ""
        let Cargo = ""
        var centros = ""

        return DataUtilizadores.map((data, index)=>{

            if(data.Estado.descricao === "Ativo")
              botaoEstado = "Desativar"
            else
              botaoEstado = "Ativar"

            if(data.TiposGestor === null)
              tipoGestor = " "
            else
              tipoGestor = data.TiposGestor.descricao

            if(data.Cargo === null)
              Cargo = " "
            else
              Cargo = data.Cargo.descricao

            //console.log(data.Pnome)
            //console.log(data.Unome)
            if(data.Centros[0].Nome != null){
              centros =data.Centros[0].Nome
            }
            //console.log(data.Centros[0].Nome)
            /* console.log(DataUtilizadores[index].Centros[0].Nome) */

            return(
                <tr key={index}>
                    <td>{data.Pnome +' '+ data.Unome}</td>
                    <td>{data.Email}</td>
                    {/* <td><Image src={data.Foto} roundedCircle className='imagem' /></td> */}
                    <td>{Cargo}</td>
                    <td>{tipoGestor}</td>
                    <td>{centros}</td>
                    <td>{data.Estado.descricao}</td>
                    <td>
                        <Link className="btn btn-info botaoAcao" to={"/home/utilizadores/reservas/" + data.id}>Reservas</Link>
                    </td>
                    <td>
                        <Link className="btn btn-warning botaoAcao" to={"/home/utilizadores/edit/" + data.id}>Editar</Link>
                    </td>
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

export default ListUtilizadores