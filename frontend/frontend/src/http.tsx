import axios, { type AxiosResponse} from 'axios'

 export function getAuthCode(state:string, codeChallenge:string) {
    var authorizationURL = `${import.meta.env.VITE_APP_REDIRECT_URL}/oauth2/authorize`;
    authorizationURL += "?client_id=client1";
    authorizationURL += "&response_type=code";
    authorizationURL += "&scope=openid read";
    authorizationURL += `&redirect_uri=${import.meta.env.VITE_APP_REDIRECT_URL}/authorized`; 
    authorizationURL += "&state=" + state;
    authorizationURL += "&code_challenge=" + codeChallenge;
    authorizationURL += "&code_challenge_method=S256";
    return authorizationURL
}

export type ExceptionErrorType = {
  message:string
  date:string
}

export type TypeToken = {
  id_token: string,
  access_token:string,
  refresh_token:string,
  token_type: "Bearer",
  expires_in: number
  scope: string
}

const AUTHORIZATION = "Basic " + window.btoa('client1:myClientService')

export function requestTokens(code:string):Promise<AxiosResponse<TypeToken>> {
    const data = {
        "grant_type": "authorization_code",
        "code": code,
        "code_verifier": localStorage.getItem("codeVerifier"),
        "redirect_uri":`${import.meta.env.VITE_APP_REDIRECT_URL}/authorized`
    };
    return axios({
      url: `${import.meta.env.VITE_APP_REDIRECT_URL}/oauth2/token`,
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': AUTHORIZATION
      },
      data,
    })
}

export function getTokenRefresh():Promise<AxiosResponse<TypeToken>> {
  const data = {
      "grant_type": "refresh_token",
      "refresh_token": localStorage.getItem("refresh_token"),
  };
  return axios({
    url: `${import.meta.env.VITE_APP_REDIRECT_URL}/oauth2/token`,
    method: 'POST',
    headers : {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
      'Authorization': AUTHORIZATION
    },
    data,
  })
}

export function getInfoFromResourceServer() {
  axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/api/health`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token")
    }})
  .then(postInfoFromAccessToken);
  function postInfoFromAccessToken(res:AxiosResponse) {
      console.log(res.data)
  }
}

export type CreditEntryType = {
  customerId: string,
  total: number
}
export function getUserPayment():Promise<AxiosResponse<CreditEntryType, any>> {
  return axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/api/payments`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }});
}

export function postUserPayment(customerId:string) {
  axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/api/payments`,
    headers: {
      "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }})
  .then(res => {
    console.log(res)
  });
}

export async function getAuthentication():Promise<AxiosResponse<boolean, any>> {
  return await axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/isAuthenticated`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }})
}

export async function getLogout():Promise<AxiosResponse<boolean, any>> {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
  localStorage.removeItem('stateValue')
  localStorage.removeItem('codeVerifier')
  return await axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/logout`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }})
}

export type SavePaymentResponse = {
  credit: number
}

export function savePayment(price:number):Promise<AxiosResponse<SavePaymentResponse>> {
  const data = {
    price,
    items: [{ quantity:20, price:10, subTotal:price }]
  }
  return axios({
    method: 'POST',
    url: `${import.meta.env.VITE_APP_REDIRECT_URL}/api/payments`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    },
    data
  })
}