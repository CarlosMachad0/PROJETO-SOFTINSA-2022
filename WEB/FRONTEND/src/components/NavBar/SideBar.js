import React ,{useEffect, useState} from "react";
import { AnimatePresence, motion } from "framer-motion";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../scss/Nav.scss';
import SidebarMenu from "./SideBarMenu";
import Container from 'react-bootstrap/Container';
import { FaBars}  from 'react-icons/fa';
import {AiFillDashboard} from 'react-icons/ai';
import {BsFillHouseDoorFill}  from 'react-icons/bs';
import {HiLocationMarker}  from 'react-icons/hi';
import {FiUsers}  from 'react-icons/fi';
import {CgProfile}  from 'react-icons/cg';
import {BiLogOut}  from 'react-icons/bi';
import { NavLink, Outlet } from "react-router-dom";
import SoftinsaLogo from '../../images/Logo_Softinsa_branco.png';
import axios from 'axios';
import { IconContext } from "react-icons";
import { Link } from "react-router-dom";

{/**
    Menu com icons e rotas definidas
*/}

const routes = [
    {
        path: "/home/dashboard",
        name: "Dashboard",
        icon: <AiFillDashboard/>
    },
    {
        path: "/home/centros/list",
        name: "Centros",
        icon: <HiLocationMarker/>
    },
    {
        path: "/home/salas/list",
        name: "Salas",
        icon: <BsFillHouseDoorFill/>
    },
    {
        path: "/home/utilizadores/list",
        name: "Utilizadores",
        icon: <FiUsers/>
    },
    {
        path: "/home/perfil",
        name: "Perfil",
        icon: <CgProfile/>
    },
    {
        path: "/home/leave",
        name: "Log Out",
        icon: <BiLogOut/>
    },
]

const SideBar = ({ children }) => {

    const [isOpen, setIsOpen] = useState(false);
    const [foto, setFoto] = useState();
    const [PrimeiroNome, setPNome] = useState("");
    const [UltimoNome, setUNome] = useState("");
    const [Cargo, setCargo] = useState("");

    {/**
        Obter os dados do utilizador.
        Nota: Tentar ir buscar ao local storage 
    */}

    useEffect(()=>{
        const jsonuser = localStorage.getItem('user')
        //console.log(jsonuser)
        var user = JSON.parse(jsonuser)
        setPNome(user.Pnome)
        setUNome(user.Unome)
        setCargo(user.TiposGestor.descricao)
        /* console.log(user.Pnome)
        console.log(user.Unome)
        console.log(user.TiposGestor.descricao) */
    });

    const toggle = () => setIsOpen(!isOpen);

    const showAnimation = {
        hidden: {
            width: 0,
            opacity: 0,
            transition: {
                duration: 0.5,
            },
        },
        show: {
            opacity: 1,
            width: "auto",
            transition: {
                duration: 0.5,
            },
        },
    };

    return (
    <>
        <div className="main-container">
            <motion.div
                animate={{
                    width: isOpen ? "340px" : "50px",
                    height: "230vh",
                    transition: {
                        duration: 0.5,
                        type: "spring",
                        damping: 10,
                    },
                }} className={'sidebar'}>

                {/**Logo da softinsa */}

                <div className="top_section pb-3">
                    <Link to={'/home/dashboard'}>
                        <AnimatePresence>
                            {isOpen && (
                                <motion.h1
                                    variants={showAnimation}
                                    initial="hidden"
                                    animate="show"
                                    exit="hidden"
                                    className="logo">
                                    <img src={SoftinsaLogo} alt="logo" className="logoSoftinsa" />
                                </motion.h1>
                            )}
                        </AnimatePresence>
                    </Link>

                {/**Icon do menu*/}
                <IconContext.Provider value={{ color: "white", size:'25px'}}>            
                    <div className="bars"  >
                        <FaBars onClick={toggle}/>
                    </div>
                </IconContext.Provider>
                
                {/**Imagem de perfil, nome e cargo */}

                </div>
                    <div className="search">
                        {/* <div className="frame_imagem_perfil">
                            <img src={''} alt="" className="Imagemperfil"/>
                        </div> */}
                    <AnimatePresence>
                        {isOpen && (
                            <motion.h6
                                variants={showAnimation}
                                initial="hidden"
                                animate="show"
                                exit="hidden">    
                                <p className="NomePerfil">{PrimeiroNome + " " + UltimoNome}</p>
                                <p className="Cargo">{Cargo}</p>
                            </motion.h6>
                        )}
                    </AnimatePresence>
                </div>

                <section className="routes">
                    {routes.map((route, index) => {
                        if (route.subRoutes) {
                            return (
                            <SidebarMenu
                                setIsOpen={setIsOpen}
                                route={route}
                                showAnimation={showAnimation}
                                isOpen={isOpen}/>
                            );
                        }
                        return (
                            <NavLink
                                to={route.path}
                                key={index}
                                className="link"
                                activeClassName="active">
                                <IconContext.Provider value={{ color: "white", size:'25px'}}>
                                    <div className="icon">{route.icon}</div>
                                </IconContext.Provider>
                                <AnimatePresence>
                                    {isOpen && (
                                        <motion.div
                                            variants={showAnimation}
                                            initial="hidden"
                                            animate="show"
                                            exit="hidden"
                                            className="link_text">
                                            {route.name}
                                        </motion.div>
                                    )}
                                </AnimatePresence>
                            </NavLink>
                        );
                    })}
                </section>
            </motion.div>
            
            <Container fluid>{children}</Container>
        </div>
    </>
    );
};

export default SideBar;