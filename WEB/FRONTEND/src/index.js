import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import axios from 'axios';

//"https://softinsa.herokuapp.com/ http://localhost:4000/"
axios.defaults.baseURL = 'https://softinsa.herokuapp.com/'
//console.log(localStorage.getItem('token'))
axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);


