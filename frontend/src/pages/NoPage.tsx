
import { Center, Text } from "@mantine/core";

const NoPage = () => {
    return (
        <>
            <Center mt={30}>
                <Text fz={50}>Page not found</Text>
            </Center>
            <Center mt={30}>
                <Text fz={"24px"}> <a href="/">Back to home</a> </Text>
            </Center>
        </>
    )
}

export default NoPage