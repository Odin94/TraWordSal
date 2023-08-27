
import { Button, Center, Group, Loader, Stack, Text, TextInput } from "@mantine/core";
import { useEffect, useState } from "react";
import { getBaseUrl } from "../globals";

const TraWordSal = () => {
    const baseUrl = getBaseUrl()

    const [goal, setGoal] = useState("")

    const [words, setWords] = useState(["asd", "bas"])
    const [latestWordNeighbours, setLatestWordNeighbours] = useState<string[]>([])
    const [latestWord, setLatestWord] = useState("")
    const [error, setError] = useState("")

    const [isLoading, setIsLoading] = useState(false)

    // TODO: Call on startWord
    const addWord = async (word: string) => {
        if (latestWordNeighbours.includes(word)) {
            setWords([...words, latestWord])
            setLatestWord("")
        }

        try {
            const response = await fetch(`${baseUrl}/getNeighbours?word=${word}`)
            const neighbours = await response.json()
            setLatestWordNeighbours(neighbours)
        } catch (e) {
            setError((e as Error).message)
        }
    }

    const back = () => {
        if (words.length > 1) {
            setWords(words.slice(0, -1))
        }
    }

    const reset = async () => {
        setIsLoading(true)
        setError("")

        try {
            const response = await fetch(`${baseUrl}/getWords`)
            const responseJson = await response.json()
            console.log(responseJson)
            setWords([responseJson.startWord])
            setGoal(responseJson.endWord)
            setLatestWordNeighbours(responseJson.startWordNeighbours)
        } catch (e) {
            setError((e as Error).message)
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        reset()
    }, [])

    // TODO: Generate and display random start/end word for user to solve (or allow custom entry) / maybe from an endpoint to make sure you get something solvable
    return (
        <>
            <hr />
            <Center>
                <Text fz={24}>Find a path from '{words[0]}' to '{goal}'</Text>
            </Center>
            <Center mt={30}>
                <Stack>
                    {words.map(w => (
                        <Text key={w}>{w}</Text>
                    ))}

                    <Group>
                        <TextInput placeholder="Next word" value={latestWord} onChange={(event) => setLatestWord(event.currentTarget.value)} />
                        <Button variant="light" color={"yellow"} onClick={back}>Back</Button>
                    </Group>

                    {isLoading ? <Loader /> : null}

                    <Button onClick={() => { addWord(latestWord) }} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>Add word</Button>

                    <Button onClick={reset} color="red" leftIcon={"🗘"} mt={200}>Reset & New Words</Button>
                </Stack>
            </Center >

            <Center mt={30}>
                <Text fz={"24px"} c={"red"}>{error}</Text>
            </Center>
        </>
    )
}

export default TraWordSal