import * as dotenv from 'dotenv'
dotenv.config()

export const getBaseUrl = () => {
    if (process.env.REACT_APP_BASE_URL) {
        return process.env.REACT_APP_BASE_URL
    }
    throw Error("REACT_APP_BASE_URL not defined in .env")
}