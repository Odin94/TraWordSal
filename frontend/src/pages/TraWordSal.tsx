
import { Button, Center, Group, Loader, Stack, Text, TextInput } from "@mantine/core";
import { useEffect, useState, KeyboardEvent } from "react";
import { getBaseUrl } from "../globals";

const TraWordSal = () => {
    const baseUrl = getBaseUrl()

    const [goal, setGoal] = useState("..")
    const [words, setWords] = useState<string[]>(["..."])

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

        // Make sure a valid path still exists
        // try {
        //     const response = await fetch(`${baseUrl}/getPath?startWord=${latestWord}&endWord=${goal}`)
        //     const pathOrError = await response.json()
        //     if (!response?.ok) {
        //         throw new Error(`Error: ${response.status} - ${pathOrError.message}`)
        //     }
        //     console.log(pathOrError)
        //     if (!pathOrError) {
        //         setError(`Warning: No known valid path to ${goal} from ${latestWord}`)
        //     }
        // } catch (e) {
        //     setError((e as Error).message)
        // }

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
                        <TextInput onKeyUp={(e) => { addWordEnter(e) }} placeholder="Next word" value={latestWord} onChange={(event) => setLatestWord(event.currentTarget.value.trim())} />
                        <Button variant="light" color={"yellow"} onClick={back}>Back</Button>
                    </Group>

                    {isLoading ? <Loader /> : null}

                    {words.at(-1) === goal
                        ? <Text fz={50} c={"green"}>🎉🎉🎉🎉 <br />Path found!<br /> 🎉🎉🎉🎉</Text>
                        : <Button onClick={() => { addWord(latestWord) }} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>Add word</Button>}

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