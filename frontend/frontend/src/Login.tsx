import { ReactEventHandler } from "react"

export default function Login() {
    const onClickSubmit = function(e:React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault()
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
                    <input type="email" className="form-control" id="floatingInput" placeholder="name@example.com"></input>
                    <label htmlFor="floatingInput">Email address</label>
                </div>
                <div className="form-floating">
                    <input type="password" className="form-control" id="floatingPassword" placeholder="Password"></input>
                    <label htmlFor="floatingPassword">Password</label>
                </div>
                <button className="btn btn-primary w-100 py-2" onClick={e => onClickSubmit(e)}>Sign in</button>
            </form>
  </main></div>
}