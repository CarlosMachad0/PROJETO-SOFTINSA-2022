import React from 'react';

import {Navigate, Outlet} from 'react-router-dom'

const useAuth=()=>{
    const id=localStorage.getItem('id')
    if(id){
        return true
    } else {
        return false
    }
}

const  PublicRoutes=() =>{

    const auth=useAuth()

    return auth?<Navigate to="/home/dashboard"/>: <Outlet/>
}

export default PublicRoutes;