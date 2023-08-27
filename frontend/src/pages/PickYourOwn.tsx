
import { Button, Center, Group, Loader, Stack, Text, TextInput } from "@mantine/core";
import { useEffect, useState, KeyboardEvent } from "react";
import { getBaseUrl } from "../globals";

const PickYourOwn = () => {
    const baseUrl = getBaseUrl()

    const [start, setStart] = useState("")
    const [end, setEnd] = useState("")


    const [goal, setGoal] = useState("")
    const [words, setWords] = useState<string[]>([])

    const [latestWordNeighbours, setLatestWordNeighbours] = useState<string[]>([])
    const [latestWord, setLatestWord] = useState("")

    const [error, setError] = useState("")

    const [isLoading, setIsLoading] = useState(false)

    const addWord = async (word: string) => {
        if (latestWordNeighbours.includes(word)) {
            setWords([...words, latestWord])
            setLatestWord("")
        } else {
            setError(`${word} is not a known neighbour of ${words[0]}`)
            return
        }

        // Get neighbours
        try {
            const response = await fetch(`${baseUrl}/getNeighbours?word=${word}`)
            const neighboursOrError = await response.json()
            if (!response?.ok) {
                throw new Error(`Error: ${response.status} - ${neighboursOrError.message}`)
            }
            console.log(neighboursOrError)
            setLatestWordNeighbours(neighboursOrError)
        } catch (e) {
            setError((e as Error).message)
        }
    }

    const addWordEnter = (e: KeyboardEvent) => {
        if (e.key === "Enter") {
            addWord(latestWord)
        }
    }

    const back = () => {
        if (words.length > 1) {
            setWords(words.slice(0, -1))
        }
    }

    const reset = async () => {
        setIsLoading(false)
        setError("")

        setWords([])
        setGoal("")
        setLatestWordNeighbours([])
    }

    const startGame = async () => {
        // Check if path is possible
        try {
            const response = await fetch(`${baseUrl}/getPath?startWord=${start}&endWord=${end}`)
            const responseJson = await response.json()

            if (!response?.ok) {
                throw new Error(`Error: ${response.status} - ${responseJson.message}`)
            }
            console.log(responseJson)
        } catch (e) {
            setError((e as Error).message)
            return
        } finally {
            setIsLoading(false)
        }

        reset()
        setWords([start])
        setGoal(end)

        // Get neighbours
        try {
            const response = await fetch(`${baseUrl}/getNeighbours?word=${start}`)
            const neighboursOrError = await response.json()
            if (!response?.ok) {
                console.log(response.ok)
                throw new Error(`Error: ${response.status} - ${neighboursOrError.message}`)
            }
            console.log(neighboursOrError)
            setLatestWordNeighbours(neighboursOrError)
        } catch (e) {
            setError((e as Error).message)
        }
    }

    return (
        <>
            <hr />
            <Center>
                <Text fz={24}>Find a path from '{words[0]}' to '{goal}'</Text>
            </Center>

            <Center>
                <Group>
                    <TextInput placeholder="Band" value={start} onChange={(event) => setStart(event.currentTarget.value.trim())} />
                    <TextInput placeholder="Bore" value={end} onChange={(event) => setEnd(event.currentTarget.value.trim())} />

                    <Button onClick={startGame} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>(Re)Start</Button>
                </Group>
            </Center>

            <Center mt={80}>
                <Stack>
                    {words.map(w => (
                        <Text key={w}>{w}</Text>
                    ))}

                    <Group>
                        <TextInput onKeyUp={(e) => { addWordEnter(e) }} placeholder="Next word" value={latestWord} onChange={(event) => setLatestWord(event.currentTarget.value.trim())} />
                        <Button variant="light" color={"yellow"} onClick={back}>Back</Button>
                    </Group>

                    {isLoading ? <Loader /> : null}

                    {words.at(-1) === goal
                        ? <Text fz={50} c={"green"}>🎉🎉🎉🎉 <br />Path found!<br /> 🎉🎉🎉🎉</Text>
                        : <Button onClick={() => { addWord(latestWord) }} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>Add word</Button>}
                </Stack>
            </Center >

            <Center mt={30}>
                <Text fz={"24px"} c={"red"}>{error}</Text>
            </Center>
        </>
    )
}

export default PickYourOwn