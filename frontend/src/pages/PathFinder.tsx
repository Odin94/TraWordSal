import { Container, Text, Center, TextInput, Button, Group } from "@mantine/core"
import { useState } from "react";


const PathFinder = () => {
    const [startWord, setStartWord] = useState('')
    const [endWord, setEndWord] = useState('')

    const [path, setPath] = useState([])
    const [error, setError] = useState("")

    const getPath = async () => {
        setError("")

        if (startWord.length === 0) {
            setError("Words can't be empty")
            return
        }
        if (startWord.length !== endWord.length) {
            setError("Words must have same length");
            return
        }

        console.log(startWord)
        console.log(endWord)

        // TODO: Put base-path in env var
        try {
            const response = await fetch(`http://localhost:8080/getPath?startWord=${startWord}&endWord=${endWord}`)
            const path = await response.json()
            setPath(path)
        } catch (e) {
            setError((e as Error).message)
        }
    }

    return (
        <Container mt={30}>
            <Center>
                <Text fz={"50px"} fw={"bold"} italic>TraWordSal</Text>
            </Center>

            <Center>
                <Text fz={"18px"} color="gray" italic>Find a path from one word to another using only valid words and changing only one letter at a time</Text>
            </Center>

            <Center mt={30}>
                <Group>
                    <TextInput placeholder="Start word" value={startWord} onChange={(event) => setStartWord(event.currentTarget.value)} />
                    <TextInput placeholder="End word" value={endWord} onChange={(event) => setEndWord(event.currentTarget.value)} />
                    <Button onClick={getPath} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>Get Path</Button>
                </Group>
            </Center>

            <Center mt={30}>
                <Text fz={"24px"}>{path.join(" -> ")}</Text>
            </Center>

            <Center mt={30}>
                <Text fz={"24px"} c={"red"}>{error}</Text>
            </Center>
        </Container>
    )
}

export default PathFinder