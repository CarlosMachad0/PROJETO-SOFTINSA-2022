import React ,{useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import logo from '../../images/Logo_Softinsa.png';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image'
import '../../scss/LoginPage.scss';
import { useNavigate, Link } from "react-router-dom";
import Form from 'react-bootstrap/Form'
import axios from 'axios';
import Swal from 'sweetalert2';


function PrmeiroLogin () {

    const id = localStorage.getItem("id")
    console.log(id)
    const Navigate = useNavigate();
    const [novaPasse, setNovaPasse] = useState("")
    const [antigaPasse, setantigaPaase] = useState("")

    return (
        <Container fluid className='Login'>
            <Row >
                <Col className='d-flex justify-content-center col-12'>
                        <Image src={logo} className="logo_Login"/>
                </Col>
            </Row>
            <Row>
                <Col className='d-flex justify-content-center col-12 '>
                    <p className='Login'>Alterar Palavra passe</p>
                </Col>
                <Col className='d-flex justify-content-center mt-3'>
                    <p className='Login'><b>Nota:</b> Para continuar altere a palavra passe fornecida</p>
                </Col>
            </Row>
            <Row>
                <Form>
                <Row>
                    <Col className='d-flex justify-content-center col-12 '>
                        <Form.Group>
                        <Form.Label className='formulario-label-input'>Palavra Passe atual</Form.Label>
                        <br/>
                        <Form.Control size="sm" type="password" value={antigaPasse} onChange={(value) => setantigaPaase(value.target.value)} className='inputLogin'/>
                        </Form.Group>
                    </Col>
                    <Col className='d-flex justify-content-center col-12 '>
                        <Form.Group>
                        <Form.Label className='formulario-label-input'>Nova Palavra Passe</Form.Label>
                        <br/>
                        <Form.Control size="sm" type="password" value={novaPasse} onChange={(value) => setNovaPasse(value.target.value)} className='inputLogin'/>
                        </Form.Group>
                    </Col>
                </Row>
            </Form>
            </Row>
            <Row className='my-5'>
                <Col className='d-flex justify-content-center'>
                    <Button variant="primary" size="lg" type="submit" className='py-0 px-5' 
                    onClick={()=>SendPasse()}>Confirmar</Button>
                </Col>
            </Row>
        </Container>
    )

    function onSucesso(){
        localStorage.removeItem("user")
        axios.get("utilizadores/get/" + id)
        .then(response=>{
            if (response.data.sucesso===true) {
                localStorage.setItem('user', JSON.stringify(response.data.data))
                Navigate("/home/dashboard")
            }
            else {
                Swal.fire(
                    'Erro',
                    response.data.message,
                    'error'
                )
            }
            }).catch(error=>{
                alert(error)
            })
    }


    function SendPasse(){
        if (antigaPasse==="") {
            Swal.fire('Insira a palavra passe atual')
        }
        else if (novaPasse==="") {
            Swal.fire('Insira uma nova palavra passe')
        }
        else if (novaPasse===antigaPasse) {
            Swal.fire('Insira uma palavra passe diferente')
        }
        else {
            const datapost = {
                PalavraPasseAntiga : antigaPasse,
                PalavraPasseNova : novaPasse
            }
            axios.put("utilizadores/editpasse/" + id,datapost)
            .then(response=>{
            if (response.data.sucesso===true) {
                Swal.fire(
                        'Sucesso!',
                        'A palavra passe foi atualizada com sucesso',
                        'success'
                );
                onSucesso()
            }
            else {
                Swal.fire(
                    'Erro',
                    response.data.message,
                    'error'
                )
            }
            }).catch(error=>{
                alert(error)
            })
        }
    }

}

export default PrmeiroLogin