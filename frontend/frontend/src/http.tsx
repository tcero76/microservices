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

export function requestTokens() {
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
    axios({
      url: `${import.meta.env.VITE_APP_REDIRECT_URL}/oauth2/token`,
      method: 'post',
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data,
    })
    .then(postRequestAccessToken)
  } else {
    alert("Invalid state value received");
  }
  function postRequestAccessToken(res:AxiosResponse<{access_token:string},any>) {
    localStorage.setItem("access_token", res.data["access_token"])
  }
}

export function getInfoFromResourceServer() {
  axios({
    method: 'get',
    url:`${import.meta.env.VITE_APP_REDIRECT_URL}/api/health`,
    headers: {
        "Content-type":"application/x-www-form-urlencoded; charset=UTF-8",
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
    }})
  .then(postInfoFromAccessToken);
}

function postInfoFromAccessToken(res:AxiosResponse) {
    alert(res.data)
}