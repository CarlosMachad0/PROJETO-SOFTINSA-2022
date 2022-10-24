import React,{useEffect, useState} from 'react';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import '../../scss/dashboard.scss'

function Limpeza(props) {

    const [dataSala, setdataSala] = useState([])
    const [centroID, setcentroID] = useState("")
    console.log(props)
    useEffect(() => {
        const timer = setInterval(()=>{
            setcentroID(props.centroid)
            console.log("Ainda nao entrei no if", centroID)
            if(centroID.length !== 0){
                console.log("Entrei")
                /**O problema esta nesta merda nao esta a ir buscar bem o id do centro */
                console.log(props.centroid)
                axios.get('centros/salasporlimpar/'+props.centroid)
                .then(res=>{
                    console.log("Data: ",res.data)
                    if(res.data.sucesso){
                        console.log("Data: ",res.data.data)
                        setdataSala(res.data.data)
                        console.log("Variavel data:", dataSala)
                    }else{
                        setdataSala([])
                    }
                })
                .catch((err=>{console.log("Erro: ",err)}))
            }
        },15000)
        LoadTableData()
        return () => clearInterval(timer)
    });

  return (
    <Container className='TabelasSalasPorLimpar'>
        <Row>
            <Col className='d-flex justify-content-center'>
                <p className='pt-4'>Salas por Limpar</p>
            </Col>
        </Row>
        <Row>
            <Col>
                <Table className="tabelaSalasPorLimpar" striped bordered hover variant="light" responsive="sm" size="sm">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Nome</th>
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
        return dataSala.map((data, index)=>{
            return(
                <tr key={index}>
                    <td>{index+1}</td>
                    <td>{data.Nome}</td>
                </tr>
            )
        });
    }
}

export default Limpeza