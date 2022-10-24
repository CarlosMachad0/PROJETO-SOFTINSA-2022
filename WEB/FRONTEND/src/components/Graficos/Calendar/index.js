import React, {useState, useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import '../../../scss/dashboard.scss'
import MyResponsiveCalendar from './calendar'
import axios from 'axios';

function Calendar(props) {

    const [data, setdata] = useState([])

    useEffect(()=>{
        //console.log(props.centroid)
        axios.get('centros/alocacaodiaria/'+props.centroid)
        .then(res=>{
            //console.log(res)
            if(res.data.sucesso){
                //console.log(res.data.data)
                setdata(res.data.data)
            }
        })
    },[props.centroid])


  return (
    <Container className='Percentagem_Alocacao'>
        <Row>
            <Col className='d-flex justify-content-center pt-3'>
                <h6>Percentagem de alocação mensal</h6>
            </Col>
        </Row>
        <Row>
            <Col className='d-flex justify-content-start alocacao'>
                <MyResponsiveCalendar data={data}/>
            </Col>
        </Row>
    </Container>
  )
}

export default Calendar