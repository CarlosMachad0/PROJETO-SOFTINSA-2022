import React,{useEffect, useState} from 'react';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import '../../scss/dashboard.scss'

function Reservas(props) {

    const [data, setdata] = useState([])


    useEffect(()=>{
        console.log(props.centroid)
        axios.get('centros/reservasfeitas/'+props.centroid)
            .then(res=>{
                console.log(res)
                if(res.data.sucesso){
                    console.log(res.data.data)
                    setdata(res.data.data)
                }
            })
    },[])

    useEffect(()=>{
        const timer = setInterval(()=>{
            axios.get('centros/reservasfeitas/'+props.centroid)
            .then(res=>{
                console.log(res)
                if(res.data.sucesso){
                    console.log(res.data.data)
                    setdata(res.data.data)
                }
            })
            .catch((err=>{console.log(err)}))
    },10000)
        LoadTableData();
        return () => clearInterval(timer)
    })

    useEffect(() => {
        LoadTableData();
    },[]);

  return (
    <Container className='ReservasFeitas'>
        <Row>
            <Col className='d-flex justify-content-center'>
                <p className='pt-4'>Reservas Marcadas</p>
            </Col>
        </Row>
        <Row>
            <Col>
                <Table className="tabelaSalasPorLimpar" striped bordered hover variant="light" responsive="sm" size="sm">
                    <thead>
                        <tr>
                            <th scope="col">Sala</th>
                            <th scope="col">Data da Reserva</th>
                            <th scope="col">Hora Inicio</th>
                            <th scope="col">Hora Fim</th>
                            <th scope="col">Utilizador</th>
                        </tr>
                    </thead>
                    <tbody>
                        <LoadTableData/>
                    </tbody>
                </Table>
            </Col>
        </Row>
    </Container>
  )

  function LoadTableData(){

        return data.map((data, index)=>{

            return(
                <tr key={index}>
                    <td>{data.SalaNome}</td>
                    <td>{data.DataReserva}</td>
                    <td>{data.HoraInicio}</td>
                    <td>{data.HoraFim}</td>
                    <td>{data.UtilizadorNome}</td>
                </tr>
            )
        });
    }

}

export default Reservas