import React, { useEffect, useState } from 'react';
import {fetchAuthData } from './store/auth-actions.ts'
import { useAuthDispatch, useAuthSelector } from './hooks/useAuthSlice.ts'
import { type AxiosError, type AxiosResponse } from 'axios';
import { getUserPayment,
    type CreditEntryType,
    type SavePaymentResponse,
    type ExceptionErrorType,
    savePayment } from './http'

export default function Home() {
    const auth = useAuthSelector((state) => state.auth)
    var [value, setValue] = useState(0)
    var [saldo, setSaldo] = useState(0)
    const dispatch = useAuthDispatch()
    useEffect(() => {
        dispatch(fetchAuthData())
        getUserPayment()
        .then((response:AxiosResponse<CreditEntryType,any>) => {
            setSaldo(response.data.total)
        })
    })
    const onChangeValue = (e:React.ChangeEvent<HTMLInputElement>) => {
        let num:string = e.target.value.replace(/[^0-9]/g, ''); 
        num = num===''? '0':num;
        setValue(parseInt(num,10))
    }
    const onClickPay = (sendValue:number) => {
      savePayment(sendValue)
        .then((res:AxiosResponse<SavePaymentResponse,any>) => {
            setSaldo(res.data.credit)
            setValue(0)
        })
        .catch((res:AxiosError<ExceptionErrorType,any>) => {
            alert(res.response?.data.message)
        })
    }
    return (<>
                <h1>Home</h1>
                <div className="card">
                    <div className="card-body">
                        <div className="input-group mb-3">
                            <span className="input-group-text">$</span>
                            <input type="text" className="form-control" aria-label="Amount (to the nearest dollar)" onChange={e => onChangeValue(e)} value={value}></input>
                        </div>
                        <p>Saldo : {saldo}</p>
                        <button type="button" className="btn btn-primary" onClick={() => onClickPay(value)}>Pay</button>
                        <a type="button" className="btn btn-danger" href="/logout">Logout</a>
                    </div>
                    <span className={`position-absolute top-0 start-100 translate-middle p-2 border border-light rounded-circle ${auth.isAuthenticated?'bg-success':'bg-danger'}`}>
                        <span className="visually-hidden">New alerts</span>
                    </span>
                </div>
            </>)
}