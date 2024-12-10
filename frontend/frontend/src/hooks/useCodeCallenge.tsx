import { useEffect, useState } from 'react';

export type typeUseCodeChallenge = {
    stateValue:string,
    codeChallengeValue:string
}

function useCodeChallenge(length:number):typeUseCodeChallenge {

  const [stateCRFS, setStateCRFS] = useState("")
  const [codeChallenge, setCodeChallenge] = useState("")
  function base64urlencode(sourceValue:number[]) {
    var stringValue = String.fromCharCode.apply(null, sourceValue);
    var base64Encoded = btoa(stringValue);
    var base64urlEncoded = base64Encoded.replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
    return base64urlEncoded;
  }
  useEffect(() => {
    var stateValue = "";
    var alphaNumericCharacters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var alphaNumericCharactersLength = alphaNumericCharacters.length;
    for (var i = 0; i < length; i++) {
        stateValue += alphaNumericCharacters.charAt(Math.floor(Math.random() * alphaNumericCharactersLength));
    }
    localStorage.setItem("stateValue", stateValue)
    var randomByteArray = new Uint8Array(32);
    window.crypto.getRandomValues(randomByteArray);
    var codeVerifier = base64urlencode(randomByteArray as unknown as number[]);
    localStorage.setItem("codeVerifier", codeVerifier)

    var codeChallengeValue = "";
    var textEncoder = new TextEncoder();
    var encodedValue = textEncoder.encode(codeVerifier);
    window.crypto.subtle.digest("SHA-256", encodedValue)
    .then(digest => {
      codeChallengeValue = base64urlencode(Array.from(new Uint8Array(digest)));
      setStateCRFS(stateValue)
      setCodeChallenge(codeChallengeValue)
      return {
        stateValue: stateCRFS,
        codeChallengeValue: codeChallenge
    }
    });
  }, [])
  return {
    stateValue:stateCRFS,
    codeChallengeValue: codeChallenge
    }
}

export default useCodeChallenge;