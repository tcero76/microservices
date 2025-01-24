import { setAuthenticated} from './auth-slice'
import { getAuthentication } from '../http'
import { Dispatch, UnknownAction } from '@reduxjs/toolkit';

export const fetchAuthData = () => {
    return async (dispatch:Dispatch<UnknownAction>) => {
        let { data: isAuthenticated } = await getAuthentication()
        dispatch(setAuthenticated(isAuthenticated))
    };
  };