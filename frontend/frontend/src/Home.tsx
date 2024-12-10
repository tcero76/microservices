import React, { useEffect, useState } from 'react';
import { getInfoFromResourceServer, getAuthentication } from './http'
import {fetchAuthData, sendLogout, updatePayment } from './store/auth-actions.ts'
import { useAuthDispatch, useAuthSelector } from './hooks/useAuthSlice.ts'

export default function Home() {
    const auth = useAuthSelector((state) => state.auth)
    var [value, setValue] = useState(0)
    const dispatch = useAuthDispatch()
    const onChangeValue = (e:React.ChangeEvent<HTMLInputElement>) => {
        let num:string = e.target.value.replace(/[^0-9]/g, ''); 
        num = num===''? '0':num;
        setValue(parseInt(num,10))
    }
    useEffect(() => {
        dispatch(fetchAuthData())
    }, [auth.isAuthenticated])
    return (<>
                <h1>Home</h1>
                <div className="card">
                    <div className="card-body">
                        <div className="input-group mb-3">
                            <span className="input-group-text">$</span>
                            <input type="text" className="form-control" aria-label="Amount (to the nearest dollar)" onChange={e => onChangeValue(e)} value={value}></input>
                        </div>
                        <p>This is some text within a card body : {auth.creditEntry}</p>
                        <button type="button" className="btn btn-primary" onClick={getInfoFromResourceServer}>GetData</button>
                        <button type="button" className="btn btn-secondary" onClick={() => dispatch(sendLogout())}>logout</button>
                        <button type="button" className="btn btn-success" onClick={getAuthentication}>isAuthenticated</button>
                        <button type="button" className="btn btn-danger" onClick={() => dispatch(updatePayment(value))}>Pay</button>
                    </div>
                    <span className={`position-absolute top-0 start-100 translate-middle p-2 border border-light rounded-circle ${auth.isAuthenticated?'bg-success':'bg-danger'}`}>
                        <span className="visually-hidden">New alerts</span>
                    </span>
                </div>
            </>)
}