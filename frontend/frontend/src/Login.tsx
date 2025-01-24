import { useState } from "react"
import { postUserPassword } from './http'

export default function Login() {
    const [user, setUser] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const onClickSubmit = function(e:React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault()
        postUserPassword(user, password)
        .then((res:any) => {
            console.log(res)
        })
    }
    const styleContainer = {
        'padding': '0 15%',
        'marginTop': '15%'
    }
    return <div className="container" style={styleContainer}>
        <main className="form-signin m-auto">
            <form>
                <h1 className="h3 mb-3 fw-normal">Please sign in</h1>
                <div className="form-floating">
                    <input type="text" className="form-control" id="floatingInput" placeholder="Username"
                        value={user} onChange={e => setUser(e.target.value)}></input>
                    <label htmlFor="floatingInput">Username</label>
                </div>
                <div className="form-floating">
                    <input type="password" className="form-control" id="floatingPassword" placeholder="Password"
                        value={password} onChange={e => setPassword(e.target.value)}></input>
                    <label htmlFor="floatingPassword">Password</label>
                </div>
                <button className="btn btn-primary w-100 py-2" onClick={e => onClickSubmit(e)}>Sign in</button>
            </form>
  </main></div>
}