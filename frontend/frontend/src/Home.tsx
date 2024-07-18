import { useEffect, useState } from 'react';
import { getInfoFromResourceServer,
    getUserPayment,
    getAuthentication,
    requestTokens,
    logout,
    type CreditEntryType } from '../src/http.tsx'
import { AxiosResponse } from 'axios';

export default function Home() {
    var [state, setState] = useState({total:0, isAuthenticated:false})
    useEffect(() => {
        console.log("entra acá?")
        getAuthentication()
        .then((res:AxiosResponse<boolean,any>) => {
            console.log(`ISAUTHENTICATED: la respuesta es: ${res.data}`)
            if (!res.data){
                getUserPayment()
                .then((res:AxiosResponse<CreditEntryType, any>) => {
                    setState({...state, total:res.data.total, isAuthenticated: true})
                })
            } else {
                getUserPayment()
                .then((res:AxiosResponse<CreditEntryType, any>) => {
                    setState({...state, total:res.data.total, isAuthenticated: true})
                })
            }
        })
        .catch(res => {
            requestTokens()
            .then(getUserPayment)
            .then((res:AxiosResponse<CreditEntryType, any>) => {
                setState({...state, total:res.data.total, isAuthenticated: true})
            })
        })
        return () => console.log("USEFFECT: ejecutó return")
    }, [])
    const onClickLogout = (e: React.MouseEvent) => {
        logout()
    }
    return (<>
                <h1>Home</h1>
                <div className="card">
                    <div className="card-body">
                        <div className="input-group mb-3">
                            <span className="input-group-text">$</span>
                            <input type="text" className="form-control" aria-label="Amount (to the nearest dollar)"></input>
                        </div>
                        <p>This is some text within a card body: ${state.total}</p>
                        <button type="button" className="btn btn-primary" onClick={getInfoFromResourceServer}>GetData</button>
                        <button type="button" className="btn btn-secondary" onClick={onClickLogout}>logout</button>
                    </div>
                </div>
            </>)
}