import {  } from 'react'
import useCodeChallenge, { type typeUseCodeChallenge } from '../src/hooks/useCodeCallenge'
import { getAuthCode } from '../src/http'

function App() {
  const {stateValue, codeChallengeValue }:typeUseCodeChallenge = useCodeChallenge(30)
  return (<div>
            <h1>Index page</h1>
            <div id="stateValue"></div>
            <div id="codeVerifierValue"></div>
            <div id="codeChallengeValue"></div>
            <br/>
            <div><a href={getAuthCode(stateValue, codeChallengeValue)}>Enviar</a></div>
            <br/>
            <p>Access token: <span id="accessToken"></span></p>
            <br/>
            <div><input type="button" value="Load"/></div>
          </div>
  )
}
export default App
