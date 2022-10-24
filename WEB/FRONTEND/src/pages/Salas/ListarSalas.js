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
import {BsFillHouseDoorFill}  from 'react-icons/bs';
import { IconContext } from "react-icons";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Menu from '../../components/NavBar/SideBar';

function ListarSalas() {

  const [dataSala, setdataSala] = useState([]);
  const utilizador = localStorage.getItem('user');
  var user = JSON.parse(utilizador)
  const tipoGestor = user.TiposGestorId; 
  const CentroId = user.CentroId
  const Navigate = useNavigate();

  useEffect(()=>{
    if(tipoGestor===1){
      axios.get('salas/listSalas/'+CentroId)
      .then(res=>{
        if(res.data.sucesso){
          setdataSala(res.data.data)
        }else{
          alert("Error Web Service");
        }
      })
    }else{
      axios.get('salas/list')
      .then(res=>{
        if(res.data.sucesso){
          setdataSala(res.data.data)
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
                  <BsFillHouseDoorFill/>
                </div>
            </IconContext.Provider>
        </Col>
        <Col className='px-0 d-flex justify-content-start'>
          <h1 className='Titulo_Pagina pt-2'>Lista de Salas</h1>
        </Col>
        <Col className='mx-3 d-flex justify-content-end'>
          <Button className="botao_adicionar pt-1 h-75 w-25 mt-2" onClick={()=>Navigate('/home/salas/add')}>
            <b>Adicionar Sala</b>
          </Button>
        </Col>
      </Row>
      <Row>
        <Col className='mx-3'>
          <Table className="tabela " striped borderless hover variant="light" responsive="sm" size="sm">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Capacidade/Alocação</th>
                    <th scope="col">Tempo de Limpeza</th>
                    <th scope="col">Centro</th>
                    <th scope="col">Motivo Bloqueio</th>
                    <th scope="col">Estado de Limpeza</th>
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
    </Container>
    </Menu>
  )

    function LoadData() {
        axios.get('salas/list')
        .then(res => {
            if(res.data.sucesso){
                const data = res.data.data;
                    setdataSala(data);
            }else{
                alert("Error Web Service!");
            }
        })
        .catch(error => {
            alert(error)
        });
    } 

    async function onAtivarDesativar(id, estado){
      if(estado===1){
          const { value: text } = await Swal.fire({
            input: 'textarea',
            inputLabel: 'Desativação ',
            inputPlaceholder: 'Insira o motivo aqui...',
            inputAttributes: {
              'aria-label': 'Insira o motivo aqui...'
            },
            showCancelButton: true
          })
        if (text) {
          const data={
            Motivo: text
          }
          axios.put('salas/desativar/'+id,data)
          .then(res=>{
            if(res.data.sucesso){
              Swal.fire(
                    'Sucesso!',
                    'A sala foi desativada',
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
        }
      }else{
        Swal.fire({
            title: 'Deseja ativar esta sala?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar',
            closeOnConfirm: false,
            closeOnCancel: false
        })
        .then((result) => {
            if (result.value) {
                axios.put('salas/ativar/' + id)
                .then(res => {
                  if(res.data.sucesso){
                    Swal.fire(
                          'Sucesso!',
                          'A sala foi ativada',
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
        })
      }
    }

    function SendDelete(id)
    {
        axios.post('salas/delete/' + id)
        .then(response =>{
            if (response.data.sucesso) {
                Swal.fire(
                    'Eliminado!',
                    'A sala foi eliminada',
                    'success'
                )
                LoadData()
            }
        })
        .catch ( error => {
            alert("Error "+error)
        })
    }


    function OnDelete(id){
        Swal.fire({
            title: 'Deseja eliminar esta sala?',
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

        return dataSala.map((data, index)=>{

            if(data.Estado.descricao === "Ativo")
              botaoEstado = "Desativar"
            else
              botaoEstado = "Ativar"

            return(
                <tr key={index}>
                    <td>{data.Nome}</td>
                    <td>{data.Capacidade +"/"+ (data.Capacidade * (data.Alocacao/100))}</td>
                    <td>{data.Tempo_Limpeza}</td>
                    <td>{data.Centro.Nome}</td>
                    <td>{data.Motivo_Bloqueio}</td>
                    <td>{data.EstadosLimpeza.descricao}</td>
                    <td>{data.Estado.descricao}</td>
                    <td>
                        <Link className="btn btn-warning botaoAcao" to={"/home/salas/edit/" + data.id}>Editar</Link>
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

export default ListarSalas