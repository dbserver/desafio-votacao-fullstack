import {
  COLOR_BACKGROUND,
  COLOR_BLACK,
  COLOR_GRAY_DARK,
  COLOR_ERROR,
  COLOR_WHITE,
  COLOR_PRIMARY,
  COLOR_ON_PRIMARY,
  COLOR_PRIMARY_LIGHT,
  COLOR_BACKGROUND_INPUT,
  COLOR_GRAY,
  COLOR_SECONDARY,
} from '@constants/colors';
import { createTheme, type ThemeOptions } from '@mui/material';
import { DEFAULT_BORDER_RADIUS } from '@constants/measurements';

export const theme: ThemeOptions = createTheme({
  palette: {
    primary: {
      light: COLOR_PRIMARY_LIGHT,
      main: COLOR_PRIMARY,
      contrastText: COLOR_ON_PRIMARY,
    },
    secondary: { main: COLOR_SECONDARY },
    error: { main: COLOR_ERROR },
    common: {
      black: COLOR_BLACK,
    },
    text: {
      primary: COLOR_BLACK,
      secondary: COLOR_GRAY_DARK,
    },
    background: {
      default: COLOR_BACKGROUND,
      paper: COLOR_WHITE,
    },
  },
  typography: {
    fontFamily: ['Roboto', 'sans-serif'].join(','),
    h1: {
      color: COLOR_GRAY_DARK,
      fontWeight: 'bold',
      fontSize: '2rem',
    },
    h2: {
      color: COLOR_GRAY_DARK,
      fontWeight: 'bold',
      fontSize: '1.5rem',
    },
    h3: {
      color: COLOR_GRAY_DARK,
      fontWeight: 'bold',
      fontSize: '1rem',
    },
    caption: {
      color: COLOR_GRAY_DARK,
      fontSize: '0.8rem',
    },
    subtitle1: {
      color: COLOR_GRAY_DARK,
      fontSize: '0.8rem',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          '& .MuiButton-startIcon': {
            marginRight: 0,
            marginLeft: 0,
          },
          borderRadius: DEFAULT_BORDER_RADIUS,
        },
      },
    },
    // MuiTextField: {
    // 	styleOverrides: {
    // 		root: {
    // 			'& .MuiInputBase-root': {
    // 				borderRadius: DEFAULT_BORDER_RADIUS,
    // 				borderColor: 'transparent',
    // 				backgroundColor: COLOR_BACKGROUND_INPUT,
    // 				padding: '8px 16px',
    // 				paddingRight: '16px',
    // 			},
    // 			'& .MuiFormLabel-root': {
    // 				color: COLOR_PRIMARY,
    // 			},
    // 			'& .MuiInputBase-input': {
    // 				color: COLOR_PRIMARY,
    // 				// padding: '8px 16px',
    // 				padding: '4px 0px 5px',
    // 			},
    // 			'& .MuiIconButton-root': {
    // 				backgroundColor: COLOR_PRIMARY,
    // 				'&:hover': {
    // 					backgroundColor: '#1565C0',
    // 				},
    // 				'& svg': {
    // 					color: '#fff',
    // 				},
    // 			},
    // 			'& .MuiOutlinedInput-notchedOutline': {
    // 				borderColor: COLOR_PRIMARY,
    // 			},
    // 		},
    // 	},
    // },
    MuiPopper: {
      styleOverrides: {
        root: {
          '& .MuiPickersPopper-paper': {
            backgroundColor: COLOR_BACKGROUND_INPUT,
          },
        },
      },
    },
    MuiCssBaseline: {
      styleOverrides: {
        '::-webkit-scrollbar': {
          width: '8px',
        },
        '::-webkit-scrollbar-track': {
          background: COLOR_WHITE,
        },
        '::-webkit-scrollbar-thumb': {
          background: COLOR_GRAY,
          borderRadius: '8px',
        },
        '::-webkit-scrollbar-thumb:hover': {
          background: COLOR_GRAY_DARK,
        },
      },
    },
  },
});
