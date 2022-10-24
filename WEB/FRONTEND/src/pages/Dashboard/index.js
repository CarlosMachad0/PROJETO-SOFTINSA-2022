import React, {useState, useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Menu from '../../components/NavBar/SideBar';
import { useNavigate, Link, useParams } from "react-router-dom";
import {AiFillDashboard}  from 'react-icons/ai';
import { IconContext } from "react-icons";
import axios from 'axios';
import Form from 'react-bootstrap/Form';
import Utilizadores from '../../components/Utilizadores/index';
import UtilizadoresAtivos from '../../components/Utilizadores/ativos';
import UtilizadoresInativos from '../../components/Utilizadores/inativos';
import NumeroReservas from '../../components/ReservasDashboard/index';
import PieChart from '../../components/Graficos/Pie';
import CalendarChart from '../../components/Graficos/Calendar';
import SalasPorLimpar from '../../components/SalasPorLimpar/index';
import ReservasFeitas from '../../components/Reservas/index';
import '../../scss/dashboard.scss'

function Dashboard() {

  const Navigate = useNavigate();
  const [primeiroLogin, setPrimeiroLogin] = useState("");
  const [Centros, setCentros] = useState([]);
  const [CentroId, setCentroId] = useState("")
  const [UserData, setUserData] = useState({})
  const [classe,setClasse] = useState("");
  const [TiposGestor, setTipoGestor] = useState("")

  var user
  var centroid = CentroId
  //console.log(centroid)
  //console.log(id)

  {/**Verificar o log in */}
  useEffect(()=>{
        const jsonuser = localStorage.getItem('user');
        //console.log(jsonuser)
        user = JSON.parse(jsonuser)
        setPrimeiroLogin(user.PrimeiroLogin)
        //console.log(user.PrimeiroLogin)
  },[]);

  {/**Obter info do user logado */}
  useEffect(() => {
      const jsonuser = localStorage.getItem('user')
      const userdata = JSON.parse(jsonuser)
      //console.log(userdata)
        setUserData(userdata)
        setTipoGestor(UserData.TiposGestorId)
        //console.log(UserData)
        //console.log(CentroData.id)
        //console.log(UserData.TiposGestorId)

        if(TiposGestor===1){
          setClasse("py-3 invisible")
          //console.log('passei aqui 1')
        }else{
          setClasse("py-3")
          setCentroId(userdata.CentroId)
          //console.log('passei aqui 2')
        }
  },[TiposGestor]); 

  {/**Ir buscar a infor dos centros */}
  useEffect(()=>{
    axios.get('centros/list')
    .then(res=>{
      if(res.data.sucesso){
        //console.log(res.data.data)
        setCentros(res.data.data)
        //console.log(Centros)
        //console.log(CentroData)
      }//else{
        //console.log(res.data.message)
      //}
    })
  },[])

  if(primeiroLogin === 1 ){
    Navigate("/home/primeiroLogin")
  }


return (
    <>
      <Menu>
        <Container fluid>
          <Row className='mt-3'>
            <Col className='mt-2 d-flex justify-content-center' md={1}>
                <IconContext.Provider value={{ color: "white", size:'30px', style: { verticalAlign: 'middle'}}}>
                    <div className='pt-2 icon_box d-flex justify-content-center'>
                      <AiFillDashboard/>
                    </div>
                </IconContext.Provider>
            </Col>
            <Col className='px-0 d-flex justify-content-start'>
              <h1 className='Titulo_Pagina pt-2'>Dashboard</h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <Form.Group className={classe}>
                <Form.Select size="sm" value={CentroId} onChange={(value) => setCentroId(value.target.value)} className="dashboard-input  h-75 w-25 mt-2">
                    <LoadCentroData />
                </Form.Select>
              </Form.Group>
            </Col>
          </Row>
          <Row>
            <Col className='d-flex justify-content-start'>
              <Utilizadores centroid={centroid}/>
              <UtilizadoresAtivos centroid={centroid}/>
              <UtilizadoresInativos centroid={centroid}/>
              <NumeroReservas centroid={centroid}/>
            </Col>
          </Row>
          <Row className='py-5'>
            <Col md={3}>
              <PieChart centroid={centroid}/>
            </Col>
            <Col>
              <SalasPorLimpar centroid={centroid}/>
            </Col>
          </Row>
          <Row>
            <Col>
              <CalendarChart centroid={centroid}/>
            </Col>
          </Row>
          <Row className='mt-5'>
            <Col className='d-flex justify-content-start'>
              <ReservasFeitas centroid={centroid}/>
            </Col>
          </Row>
        </Container>
      </Menu>     
    </>
  )

  function LoadCentroData() {
        return Centros.map((data, index) => {
            return (
                <option key={index} value={data.id}>{data.Nome}</option>
            )
        });
  }
}

export default Dashboard