import { Container } from '@mantine/core';
import {
  BrowserRouter,
  Route,
  Routes
} from "react-router-dom";
import './App.css';
import AppHeader from './components/AppHeader';
import NoPage from './pages/NoPage';
import PathFinder from './pages/PathFinder';
import TraWordSal from './pages/TraWordSal';
import PickYourOwn from './pages/PickYourOwn';

function App() {
  return (
    <Container mt={30}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AppHeader />}>
            <Route index element={<TraWordSal />} />
            <Route path="pathFinder" element={<PathFinder />} />
            <Route path="pickYourOwn" element={<PickYourOwn />} />
            <Route path="*" element={<NoPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </Container>
  );
}

export default App;
