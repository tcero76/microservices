import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import { type TypeToken, type CreditEntryType, type SavePaymentResponse } from '../http'

type AuthState = {
  isAuthenticated: boolean
  creditEntry: number
};

const initialState: AuthState = {
  isAuthenticated: false,
  creditEntry: 0
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setAuthenticated(state, action:PayloadAction<boolean>) {
      state.isAuthenticated = action.payload
    },
    setPayment(state, action:PayloadAction<SavePaymentResponse>) {
      state.creditEntry = action.payload.credit
    },
    saveAuthentication(_, action:PayloadAction<TypeToken>) {
      if ( action.payload.access_token != null) {
        localStorage.setItem('access_token', action.payload.access_token)
        localStorage.setItem('refresh_token', action.payload.refresh_token)
      }
    },
    updateCreditEntry(state, action:PayloadAction<CreditEntryType>) {
      state.creditEntry = action.payload.total
    },
    refresh(state, action:PayloadAction<TypeToken>) {
      if (action.payload.access_token != null) {
        localStorage.setItem('access_token', action.payload.access_token)
        localStorage.setItem('refresh_token', action.payload.refresh_token)
        state.isAuthenticated = true
      }
    },
    logout(state) {
      state.isAuthenticated = false
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      window.location.replace('/')
    }
}});

export const { saveAuthentication, refresh, logout, setAuthenticated, updateCreditEntry, setPayment } = authSlice.actions;
