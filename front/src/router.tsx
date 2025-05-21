import BaseLayout from '@components/BaseLayout';
import { createBrowserRouter } from 'react-router-dom';
import * as PATHS from '@constants/paths';
import Pautas from '@pages/pautas';
import Resultados from '@pages/resultados';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <BaseLayout />,
    children: [
      {
        path: '',
        element: <Pautas />,
      },
      {
        path: PATHS.RESULTADOS,
        element: <Resultados />,
      },
    ],
  },
]);
