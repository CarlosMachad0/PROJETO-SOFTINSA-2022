import React, {useState, useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import '../../scss/dashboard.scss'
import axios from 'axios';

function ReservasDatas(props) {

    const[data,setdata] = useState(0)
    const[DataIncio, setDataIncio] = useState("")
    const[DataFim, setDataFim] = useState("")
    var centro = props.centroid
    //console.log('o problema nao e aqui',centro)

  return (
      <Container className='ReservasporDatas align-items-center'>
        <Row className='pt-2'>
            <Col>
                <label className='py-1'>Data Inicio: </label>
                <input type="date" value={DataIncio} onChange={(value) => setDataIncio(value.target.value)} className="dashboard-input"></input>
            <br></br>
            </Col>
            <Col>
                <label className='py-1'>Data Fim: </label>
                <input type="date" value={DataFim} onChange={(value) => setDataFim(value.target.value)} className="dashboard-input"></input>
            </Col>
        </Row>
        <Row className='pt-3'>
          <Col>
            <Button className='BotaoPesquisar d-flex justify-content-center'onClick={()=>sendData()}>Procurar</Button>
          </Col>
          <Col className='d-flex justify-content-center'>
              <h6>Número total de reservas ativas: <b>{data}</b></h6>
          </Col>
        </Row>
      </Container>
  )

  function sendData(){
    if(DataIncio === ""){
      setdata(0)
    }else{
      if(DataFim === ""){
        setdata(0)
      }else{
        const dataget = {
            DataInicio: DataIncio,
            DataFim: DataFim
        }
        /* console.log(dataget)
        console.log(dataget.DataInicio)
        console.log(dataget.DataFim) */
        axios.put('reservas/entredatas/'+centro,dataget)
        .then(response=>{
          if(response.data.sucesso){
            console.log(response.data)
            setdata(response.data.data)
          }else{
            setdata(0)
          }
        })
      }
    }
  }
}

export default ReservasDatas