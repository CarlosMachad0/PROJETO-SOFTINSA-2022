import React from 'react'
import NavBar from '../../components/NavBarLandingPage/NavMenuNoLog';
import Hero from '../../components/Hero/index';
import Equipa from '../../components/Equipa/index';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import '../../scss/LandingPage.scss';

function LandingPage() {
  return (
    <Container fluid>
          <NavBar/>
          <Hero/>
      <Row>
        <Col className='Equipa'>
          <Equipa/>
        </Col>
      </Row>
    </Container>)
}

export default LandingPage