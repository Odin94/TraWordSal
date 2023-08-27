import { Button, Center, Group, Loader, Text, TextInput } from "@mantine/core";
import { useState } from "react";
import { getBaseUrl } from "../globals";


const PathFinder = () => {
    const baseUrl = getBaseUrl()
    const [startWord, setStartWord] = useState('')
    const [endWord, setEndWord] = useState('')

    const [path, setPath] = useState<string[]>([])
    const [error, setError] = useState("")

    const [isLoading, setIsLoading] = useState(false)

    const getPath = async () => {
        setError("")
        setIsLoading(true)

        if (startWord.length === 0) {
            setError("Words can't be empty")
            return
        }
        if (startWord.length !== endWord.length) {
            setError("Words must have same length");
            return
        }

        try {
            const response = await fetch(`${baseUrl}/getPath?startWord=${startWord}&endWord=${endWord}`)
            const responseJson = await response.json()

            if (!response?.ok) {
                throw new Error(`Error: ${response.status} - ${responseJson.message}`)
            }
            setPath(responseJson)
        } catch (e) {
            setError((e as Error).message)
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <>
            <Center mt={30}>
                <Group>
                    <TextInput placeholder="Start word" value={startWord} onChange={(event) => setStartWord(event.currentTarget.value)} />
                    <TextInput placeholder="End word" value={endWord} onChange={(event) => setEndWord(event.currentTarget.value)} />
                    <Button onClick={getPath} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>Get Solution</Button>
                </Group>
            </Center>

            <Center mt={30}>
                {isLoading
                    ? <Loader />
                    :
                    <Text fz={"24px"}>{path.join(" -> ")}</Text>
                }
            </Center>


            <Center mt={30}>
                <Text fz={"24px"} c={"red"}>{error}</Text>
            </Center>
        </>
    )
}

export default PathFinder