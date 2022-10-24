import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image'
import imagem from '../../images/undraw_Real_time_sync_re_nky7.png'
import '../../scss/LandingPage.scss'

function Hero() {
    return (
    <section className='hero'>
            <Row className='align-items-center mt-5'>
                <Col className='justify-content-start' sm={8} md={6}>
                    <Container className='mx-3'>
                        <h3 className='mb-3'>Reserve e organize os espaços da sua empresa sem dificuldades</h3>
                        <small className='text-muted '> 
                            Um software capaz de gerir os espaços,
                            as pessoas e as reservas em tempo real e com a maior facilidade.
                        </small>
                        <br />
                    </Container>
                    <Container className='d-flex justify-content-start mx-3'>
                        <Button variant="primary" size="lg" className=' my-3' href="https://drive.google.com/drive/u/0/folders/1EKe3xpFoVShbwBDmk68ZjSi2VdosQwga">Fazer download da app</Button>
                    </Container>
                </Col>
                <Col className="d-flex justify-content-center d-none d-sm-block" sm={4} md={6}>
                    <Image src={imagem} className='heroImagem mx-2'></Image>
                </Col>
            </Row>
    </section>
    )
}

export default Hero