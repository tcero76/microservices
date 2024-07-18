import {  } from 'react'
import useCodeChallenge, { type typeUseCodeChallenge } from '../src/hooks/useCodeCallenge'
import { getAuthCode } from '../src/http'

function App() {
  const {stateValue, codeChallengeValue }:typeUseCodeChallenge = useCodeChallenge(30)
  return (<div>
            <a href={getAuthCode(stateValue, codeChallengeValue)}>Ingresar</a>
          </div>
  )
}
export default App
