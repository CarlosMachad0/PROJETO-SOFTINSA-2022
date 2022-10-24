import React, {useState, useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import '../../scss/dashboard.scss'
import axios from 'axios';

export default function Utilizadores(props) {

    const[data,setdata] = useState("")

    var centro = props.centroid
    //console.log('o problema nao e aqui',centro)

  useEffect(()=>{
    axios.get('centros/utilizadores/'+centro)
    .then(response=>{
      if(response.data.sucesso){
        //console.log(response.data.data)
        setdata(response.data.data)
      }
    })
  })


  return (
      <Container className='numeroUtilizadores align-items-center'>
        <Row className='pt-3'>
          <Col className='d-flex justify-content-center'>
            <h6> Total utilizadores registados</h6>
          </Col>
        </Row>
        <Row>
          <Col className='d-flex justify-content-center'>
            <h5><b>{data}</b></h5>
          </Col>
        </Row>
      </Container>
  )
}
