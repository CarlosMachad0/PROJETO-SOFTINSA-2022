import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container'
import logo from '../../images/Logo_Softinsa.png';
import Navbar from 'react-bootstrap/Navbar';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image'
import {motion} from 'framer-motion'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import '../../scss/LandingPage.scss'
import { Link } from "react-router-dom";

function NavBarLandingPage() {

    return (
    <Row className='Menu'>
        <Col>
        <Container>
            <Link to="/">
                <Image className='Brand' src={logo}></Image>
            </Link>
        </Container>
    </Col>
    <Col className='d-flex align-items-center justify-content-end '>        
            <Button href="/login" variant="primary" size="lg" className='mx-5 px-5 py-1'>Login</Button>
    </Col>
    </Row>
    )
}

export default NavBarLandingPage