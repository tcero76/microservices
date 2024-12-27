import { AxiosError, AxiosResponse } from 'axios';
import { saveAuthentication, logout, setAuthenticated, updateCreditEntry, setPayment } from './auth-slice'
import { requestTokens,
  getLogout,
  getAuthentication, 
  getUserPayment,
  type CreditEntryType,
  type SavePaymentResponse,
  type ExceptionErrorType,
  savePayment } from '../http'
import { Dispatch, UnknownAction } from '@reduxjs/toolkit';

export const fetchAuthData = () => {
    return async (dispatch:Dispatch<UnknownAction>) => {
        var urlParams = new URLSearchParams(window.location.search);
        if(urlParams.get('error')) {
          console.error(urlParams.get('error_description'))
        }
        var originalStateValue = localStorage.getItem("stateValue")
        const code = urlParams.get('code')
        let { data: isAuthenticated } = await getAuthentication()
        dispatch(setAuthenticated(isAuthenticated))
        if(urlParams.get('state') === originalStateValue && isAuthenticated && code != null) {
          let respToken = await requestTokens(code)
          dispatch(saveAuthentication(respToken.data))
        } else if(!isAuthenticated) {
          console.log('Invalid state value received');
          window.location.href = '/'
        }
        getUserPayment()
        .then((response:AxiosResponse<CreditEntryType,any>) => {
          dispatch(updateCreditEntry(response.data))
        })
    };
  };

  export const updatePayment = (total:number) => {
    return async (dispatch:Dispatch<UnknownAction>) => {
      savePayment(total)
      .then((res:AxiosResponse<SavePaymentResponse,any>) => {
        dispatch(setPayment(res.data))
      })
      .catch((res:AxiosError<ExceptionErrorType,any>) => {
        alert(res.response?.data.message)
      })
    
    }
  }

  export const sendLogout = () => {
    return async (dispatch:Dispatch<UnknownAction>) => {
      getLogout()
      .then(() => {
        dispatch(logout())
      })
    }
  }