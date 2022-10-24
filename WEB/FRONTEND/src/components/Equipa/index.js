import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Card from 'react-bootstrap/Card';
import Eduardo from '../../images/Eduardo.png';
import Carlos from '../../images/Carlos.png';
import Joao from '../../images/Joao.png';
import Francisco from '../../images/Francisco.jpeg';
import Nuno from '../../images/Nuno.png';

import '../../scss/LandingPage.scss';

function Equipa() {
    return (
    <>
            <Row>
                <Col className='d-flex justify-content-center'>
                    <p className='h2 py-4'>Equipa</p>
                    <hr className='border-primary border-2' />
                </Col>
            </Row>
            <Row className='pb-5'>
                <Col className='d-flex justify-content-center mt-4'>
                <Card style={{ width: '13rem' }}>
                    <Card.Img  variant="top" src={Nuno} />
                        <Card.Body>
                        <Card.Title className= "text-center">Nuno Martinho</Card.Title>
                        <Card.Text className= "text-center">
                                Software Developer
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col className='d-flex justify-content-center mt-4'>
                    <Card   style={{ width: '13rem' }}>
                        <Card.Img  variant="top" src={Carlos} />
                        <Card.Body>
                        <Card.Title className= "text-center">Carlos Machado</Card.Title>
                            <Card.Text className= "text-center">
                                Software Developer
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col className='d-flex justify-content-center mt-4'>
                <Card  style={{ width: '13rem' }}>
                        <Card.Img variant="top" src={Eduardo} />
                        <Card.Body >
                        <Card.Title className= "text-center">Eduardo Silva</Card.Title>
                        <Card.Text className= "text-center">
                                Software Developer
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col className='d-flex justify-content-center mt-4'>
                <Card  style={{ width: '13rem'}}>
                        <Card.Img variant="top" src={Joao}/>
                        <Card.Body>
                        <Card.Title className= "text-center">João Lopes</Card.Title>
                        <Card.Text className= "text-center">
                                Software Developer
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col className='d-flex justify-content-center mt-4'>
                <Card  style={{ width: '13rem' }}> 
                        <Card.Img variant="top" src={Francisco}/>
                        <Card.Body>
                        <Card.Title className= "text-center">Francisco Lopes</Card.Title>
                        <Card.Text className= "text-center">
                                Software Developer
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row >
    </>
    )
}

export default Equipa