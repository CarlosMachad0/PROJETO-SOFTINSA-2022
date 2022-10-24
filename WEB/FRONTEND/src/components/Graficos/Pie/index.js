import React, {useState, useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import '../../../scss/dashboard.scss'
import axios from 'axios';
import MyResponsivePie from './piechar'

function Pie(props) {

    const [data, setdata] = useState([])

    useEffect(()=>{
        //console.log(props.centroid)
        axios.get('centros/salasmaisutilizadas/'+props.centroid)
        .then(res=>{
            //console.log(res)
            if(res.data.sucesso){
                //console.log(res.data.data)
                setdata(res.data.data)
            }
        })
    },[props.centroid])

  return (
    <Container className='piechart'>
        <Row>
            <Col className='d-flex justify-content-center pt-3'>
                <p>Percentagem de salas mais utilizadas face à capacidade</p>
            </Col>
        </Row>
        <Row>
            <Col className='piecharparent'>
                <MyResponsivePie data={data}/>
            </Col>
        </Row>
    </Container>
  )
}

export default Pie