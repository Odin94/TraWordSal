import { AppShell, Center, Group, Header, Text } from "@mantine/core"
import { Link, Outlet } from "react-router-dom"


const AppHeader = () => {

    return (
        <AppShell
            header={
                <Header withBorder={false} height={60} p="xs">{
                    <Center>
                        <Group>
                            <Link to="/" style={{ textDecoration: "none" }}><Text fz={20} c={"black"}>Game</Text></Link>
                            <Link to="/pathFinder" style={{ textDecoration: "none" }}><Text fz={20} c={"black"}>Find paths</Text></Link>
                        </Group>
                    </Center>
                }</Header>}
        >
            <Center>
                <Text fz={"50px"} fw={"bold"} italic>TraWordSal</Text>
            </Center>

            <Center>
                <Text fz={"18px"} color="gray" italic>Find a path from one word to another using only valid words and changing only one letter at a time</Text>
            </Center>

            <Outlet />
        </AppShell>
    )
}

export default AppHeader