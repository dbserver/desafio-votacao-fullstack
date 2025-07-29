import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadChildren: () => import('./home/home.module').then(m => m.HomeModule)
    },
    {
        path: 'pauta',
        loadChildren: () => import('./pauta/pauta.module').then(m => m.PautasModule)
    },
    { 
        path: '**',
        redirectTo: 'home',
    }
];
