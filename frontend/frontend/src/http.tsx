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

export async function requestTokens() {
  var urlParams = new URLSearchParams(window.location.search);
  if(urlParams.get('error')) {
    console.error(urlParams.get('error_description'))
  }
  var originalStateValue = localStorage.getItem("stateValue")
  if(urlParams.get('state') === originalStateValue) {
    var data = {
        "grant_type": "authorization_code",
        "client_id": "client1",
        "code": urlParams.get('code'),
        "code_verifier": localStorage.getItem("codeVerifier"),
        "redirect_uri":`${import.meta.env.VITE_APP_REDIRECT_URL}/authorized`
    };
    const resp = await axios({
      url: `${import.meta.env.VITE_APP_REDIRECT_URL}/oauth2/token`,
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      },
      data,
    })
    await localStorage.setItem("access_token", resp.data["access_token"])
  } else {
    alert("Invalid state value received");
  }
}

export function getInfoFromResourceServer() {
  axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/api/health`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
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
export async function getUserPayment():Promise<AxiosResponse<CreditEntryType, any>> {
  return await axios({
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

export async function logout():Promise<AxiosResponse<boolean, any>> {
  return await axios({
    method: 'GET',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/logout`,
    headers: {
        "Content-Type":"application/json",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }})
}