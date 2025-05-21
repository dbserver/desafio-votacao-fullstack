import * as PATHS from './paths';

export interface INavbarTabs {
  label: string;
  route?: string;
  subItens?: INavbarTabs[];
}

const navbarTabs: INavbarTabs[] = [
  {
    label: 'Pautas Abertas',
    route: '/',
  },
  {
    label: 'Resultados',
    route: PATHS.RESULTADOS,
  },
];

export default navbarTabs;
