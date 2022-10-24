import React, { useEffect, useState } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import logo from '../../images/Logo_Softinsa.png';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image'
import '../../scss/LoginPage.scss';
import { Link, useNavigate } from "react-router-dom";
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Swal from 'sweetalert2';

function Login() {

  const[loggedIn,SetloggedIn] = useState(false);
  const[email,SetEmail] = useState("");
  const[passe,SetPasse] = useState("");

  const Navigate = useNavigate();

  useEffect(()=>{
    if(loggedIn){
      const user = localStorage.getItem('user')
      //console.log(user.id)
      Navigate('/home/dashboard')
    }
  },[loggedIn])

      return(
          <Container fluid className='Login'>
            <Row >
              <Col className='d-flex justify-content-center col-12'>
                  <Link to='/'><Image src={logo} className="logo_Login"/></Link>
              </Col>
            </Row>
            <Row>
              <Col className='d-flex justify-content-center col-12 '>
                <p className='Login'>Insira as suas credenciais</p>
              </Col>
              <Col className='d-flex justify-content-center'>
                <hr className='linha' />
              </Col>
            </Row>
            <Row>
              <Col className='d-flex justify-content-center col-12'>
                <Form>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                      <Form.Label>E-mail</Form.Label>
                      <Form.Control type="email" placeholder="Insira o seu e-mail" className='inputLogin rounded' value={email} 
                      onChange={(value)=>SetEmail(value.target.value)}/>
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                      <Form.Label>Password</Form.Label>
                      <Form.Control type="password" placeholder="Password" className='inputLogin rounded' value={passe} onChange={
                        (value)=>SetPasse(value.target.value)}/>
                    </Form.Group>
                </Form>
              </Col>
            </Row>
            <Row className='my-3'>
              <Col className='d-flex justify-content-center'>
                <Button variant="primary" size="lg" type="submit" className='py-0 px-5' onClick={()=>onLogin()}>
                      Login
                  </Button>
              </Col>
            </Row>
          </Container>)

    function onLogin(){
      if (email==="") {
          alert("Insira um email!")
      }
      else if (passe==="") {
          alert("Insira uma palavra passe!")
      }
      else{
        const data={
          email: email,
          password: passe
        } 

        axios.post("auth/loginGestor",data)
        .then(response=>{
            if(response.data.sucesso===true){
              if(response.data.token){
                localStorage.setItem('user', JSON.stringify(response.data.user))
                localStorage.setItem('token', response.data.token);
                localStorage.setItem('id',response.data.user.id);
              }
              SetloggedIn(true);
              //console.log(response.data.user.CentroId)
              Navigate('/home/dashboard');
            }else{
              Swal.fire(
                    'Oops..Não foi possível fazer o log in!',
                    'Dados inválidos',
                    'error'
                )
            }
        })
        .catch(error=>{
          alert("Erro: " + error);
        })
      }    
    }
}

export default Login