import React,{useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/listar.scss';
import Swal from 'sweetalert2';
import axios from 'axios';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Table from 'react-bootstrap/Table';
import {HiLocationMarker}  from 'react-icons/hi';
import { IconContext } from "react-icons";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom"
import Menu from '../../components/NavBar/SideBar';

function ListarCentros() {

  const [dataCentro, setdataCentro] = useState([]);
  const [classe,setClasse] = useState("");
  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const CentroId = user.CentroId
  //console.log(CentroId)
  //console.log(tipoGestor)
  const Navigate = useNavigate();

  useEffect(()=>{
    axios.get('centros/list')
    .then(res=>{
      if(res.data.sucesso){
        setdataCentro(res.data.data)
      }else{
        alert("Error Web Service");
      }
    })
  },[])

  useEffect(() => {
        if(tipoGestor===1){
          setClasse("botao_adicionar pt-1 h-75 w-25 mt-2 invisible")
        }else{
          setClasse("botao_adicionar pt-1 h-75 w-25 mt-2 mx-3")
        }
        LoadData();
  },[]); 

  return (
    <>
      <Menu>
      <Row className='mt-3'>
        <Col className='mt-2 d-flex justify-content-center' sm={2} md={1}>
            <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                <div className='pt-2 icon_box d-flex justify-content-center'>
                  <HiLocationMarker/>
                </div>
            </IconContext.Provider>
        </Col>
        <Col className='px-0 d-flex justify-content-start' sm={4} md={5}>
          <h1 className='Titulo_Pagina pt-2'>Lista de Centros</h1>
        </Col>
        <Col className='d-flex justify-content-end' sm={4} md={6}>
          <Button className={classe} onClick={()=>Navigate('/home/centros/add')}>
          <b>Adicionar Centro</b>
          </Button>
        </Col>
      </Row>
      <Row>
        <Col className='mx-3'>
          <Table className="tabela " striped borderless hover variant="light" responsive="sm" size="sm">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Endereço</th>
                    <th scope="col">Horário</th>
                    <th scope="col">Telefone</th>
                    <th scope="col">Estado</th>
                    <th colSpan={5}>Ações</th>
                </tr>
            </thead>
            <tbody>
                <LoadTableData/>
            </tbody>
        </Table>
        </Col>
      </Row>
    </Menu>
    </>
  )

  function LoadData() {
        axios.get('centros/list')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                    setdataCentro(data);
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
          axios.put('centros/desativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'O centro foi desativado',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                    'Erro!',
                    res.data.message,
                    'error'
                )
            }
          })
          .catch ( error => {
            alert("Error "+error)
          })
        }else{
          axios.put('centros/ativar/' + id)
          .then(res => {
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'O centro foi ativado',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                    'Erro!',
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
            title: 'Deseja desativar este centro?',
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
            title: 'Deseja ativar este centro?',
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
        axios.post('centros/delete/' + id)
        .then(response =>{
            if (response.data.sucesso) {
                Swal.fire(
                    'Eliminado!',
                    'O centro foi eliminado',
                    'success'
                )
                LoadData()
            }else{
              Swal.fire(
                    'Erro!',
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
            title: 'Deseja eliminar este centro?',
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
        let botaoEditar = ""
        let botaoEliminar = ""
        let botaoAtivarDesativar = ""

        return dataCentro.map((data, index)=>{

            if(data.Estado.descricao === "Ativo")
              botaoEstado = "Desativar"
            else
              botaoEstado = "Ativar"
            
            if(tipoGestor === 1){
              if(CentroId === dataCentro[index].id){
                botaoEditar = "btn btn-warning botaoAcao visible"
                botaoEliminar = "btn btn-danger botaoAcao visible"
                botaoAtivarDesativar = "btn btn-primary botaoAcao visible"
              }else{
                botaoEditar = "btn btn-warning botaoAcao invisible"
                botaoEliminar = "btn btn-danger botaoAcao invisible"
                botaoAtivarDesativar = "btn btn-primary botaoAcao invisible"
              }
            }else{
                botaoEditar = "btn btn-warning botaoAcao visible"
                botaoEliminar = "btn btn-danger botaoAcao visible"
                botaoAtivarDesativar = "btn btn-primary botaoAcao visible"
            }

            return(
                <tr key={index}>
                    <td>{data.Nome}</td>
                    <td>{data.Endereco}</td>
                    <td>{data.Hora_abertura + '-' + data.Hora_fecho}</td>
                    <td>{data.Telefone}</td>
                    <td>{data.Estado.descricao}</td>
                    <td>
                        <Link className={botaoEditar} to={"/home/centros/edit/" + data.id}>Editar</Link>
                    </td>
                    <td>
                        <button className={botaoEliminar} type="button" onClick={()=>OnDelete(data.id)}>Eliminar</button>
                    </td>
                    <td>
                        <button className={botaoAtivarDesativar} type="button" 
                        onClick={()=>onAtivarDesativar(data.id,data.Estado.id)}>{botaoEstado}</button>
                    </td>
                </tr>
            )
        });
    }
}

export default ListarCentros