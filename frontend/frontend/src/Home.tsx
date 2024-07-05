import { useEffect } from 'react';
import { requestTokens, getInfoFromResourceServer } from '../src/http.tsx'

export default function Home() {
    useEffect(() => {
        requestTokens()
    })
    return (<div>
                <div>Home</div>
                <button onClick={getInfoFromResourceServer}>GetData</button>
            </div>)
}