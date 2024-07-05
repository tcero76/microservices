import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import Home from './Home'
import Login from './Login'
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App/>
  }, {
    path: "/authorized",
    element: <Home/>
  }, {
    path: "/login",
    element: <Login/>
  }
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
)
